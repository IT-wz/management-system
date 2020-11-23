package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.*;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ExportService;
import com.itheima.vo.ExportProductResult;
import com.itheima.vo.ExportProductVo;
import com.itheima.vo.ExportResult;
import com.itheima.vo.ExportVo;
import org.apache.cxf.jaxrs.client.WebClient;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao; // 报运dao

    @Autowired
    private ExportProductDao exportProductDao;//报运单商品dao

    @Autowired
    private ExtEproductDao extEproductDao;  //报运单附件Dao

    @Autowired
    private ContractDao contractDao; // 合同dao

    @Autowired
    private ContractProductDao contractProductDao; //合同货物dao

    @Autowired
    private ExtCproductDao extCproductDao;  //合同附件Dao

    //保存
    public void save(Export export) {
        //查询当前报运单下的合同信息(in)
        List<String> contractIdList = Arrays.asList(export.getContractIds().split(","));

        //=========================================查询合同信息,生成报运单信息============================================//

        //1. 查询合同信息
        ContractExample contractExample = new ContractExample();
        contractExample.createCriteria().andIdIn(contractIdList);
        List<Contract> contractList = contractDao.selectByExample(contractExample);

        StringBuilder sb = new StringBuilder();//合同号
        Integer proNum = 0;//货物数
        Integer extNum = 0;//附件数

        //2. 拼接数据
        for (Contract contract : contractList) {
            sb.append(contract.getContractNo() + " ");
            proNum += contract.getProNum();
            extNum += contract.getExtNum();
        }

        export.setCustomerContract(sb.toString());
        export.setProNum(proNum);
        export.setExtNum(extNum);

        //3. 执行保存操作
        exportDao.insertSelective(export);

        //=========================================查询合同下的货物信息,生成报运单下的货物信息============================================//
        //1. 根据合同id集合查询合同下的货物信息
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdIn(contractIdList);
        List<ContractProduct> contractProductList = contractProductDao.selectByExample(contractProductExample);

        //2. 利用查询到的货物对象生成报运单下货物对象
        //contractProduct---------------->exportProduct
        for (ContractProduct contractProduct : contractProductList) {
            ExportProduct exportProduct = new ExportProduct();
            //注意$$$$$$$$$$$$$$$$:1. 设置id的过程需要复制属性之后 2. BeanUtils使用的是Spring提供的包
            //从contractProduct复制一部分到exportProduct
            BeanUtils.copyProperties(contractProduct, exportProduct);
            //自己生成id
            exportProduct.setId(UUID.randomUUID().toString());
            //设置exportId
            exportProduct.setExportId(export.getId());
            //执行保存
            exportProductDao.insertSelective(exportProduct);
        }


        //=========================================查询合同下的附件信息,生成报运单下的附件信息============================================//
        //1. 根据合同id集合查询合同下的附件信息
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractIdIn(contractIdList);
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);


        //2. 利用查询到的附件对象生成报运单下附件对象
        //extCproduct------------->extEproduct
        for (ExtCproduct extCproduct : extCproductList) {
            ExtEproduct extEproduct = new ExtEproduct();
            //复制属性
            BeanUtils.copyProperties(extCproduct, extEproduct);
            //设置id
            extEproduct.setId(UUID.randomUUID().toString());
            //设置exportId
            extEproduct.setExportId(export.getId());
            // 执行保存
            extEproductDao.insertSelective(extEproduct);
        }
    }

    //更新
    public void update(Export export) {
        //=================================修改报运单中的信息===================================//
        exportDao.updateByPrimaryKeySelective(export);

        //=================================修改报运单中的货物信息===================================//
        List<ExportProduct> exportProducts = export.getExportProducts();
        if (exportProducts != null && exportProducts.size() > 0) {
            for (ExportProduct exportProduct : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    //删除
    public void delete(String id) {

    }

    //根据id查询
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    //分页
    public PageInfo findByPage(int pageNum, int pageSize, ExportExample example) {
        PageHelper.startPage(pageNum, pageSize);
        List<Export> list = exportDao.selectByExample(example);
        return new PageInfo(list);
    }

    @Override
    public void exportE(String id) {
        //1. 查询export信息,封装ExportVo对象
        Export export = exportDao.selectByPrimaryKey(id);
        ExportVo exportVo = new ExportVo();
        BeanUtils.copyProperties(export, exportVo);//复制属性
        exportVo.setExportId(export.getId());//设置报运单id
        exportVo.setExportDate(new Date());//设置申批时间

        //2. 查询exportProduct信息,封装ExportProductVo对象
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProductList = exportProductDao.selectByExample(exportProductExample);
        for (ExportProduct exportProduct : exportProductList) {
            ExportProductVo exportProductVo = new ExportProductVo();
            BeanUtils.copyProperties(exportProduct, exportProductVo);//复制属性
            exportProductVo.setExportProductId(exportProduct.getId());//设置报运单货物id

            exportVo.getProducts().add(exportProductVo);//将货物封装进报运单
        }

        //3. 调用海关平台程序,发送数据
        WebClient.create("http://localhost:5003/ws/export/user").post(exportVo);

        //4. 修改当前报运单状态(0---->1)
        export.setState(1);
        exportDao.updateByPrimaryKeySelective(export);
    }

    @Override
    public void findExportResult(String id) {
        try {
            //1. 发送请求到海关平台,查询到结果进行解析
            ExportResult exportResult = WebClient.create("http://localhost:5003/ws/export/user/" + id).get(ExportResult.class);
            System.out.println(exportResult);

            //2. 将状态信息---export
            Export export = new Export();
            export.setId(exportResult.getExportId());
            export.setRemark(exportResult.getRemark());
            export.setState(exportResult.getState());
            exportDao.updateByPrimaryKeySelective(export);

            //3. 将税务信息---exportProduct
            Set<ExportProductResult> products = exportResult.getProducts();
            if (products != null && products.size() > 0) {
                for (ExportProductResult product : products) {
                    ExportProduct exportProduct = new ExportProduct();
                    exportProduct.setId(product.getExportProductId());
                    exportProduct.setTax(product.getTax());
                    exportProductDao.updateByPrimaryKeySelective(exportProduct);
                }
            }
        } catch (Exception e) {
            System.out.println("未查到相关信息");
        }

    }

}
