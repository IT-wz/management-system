package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ContractProduct;
import com.itheima.domain.cargo.ContractProductExample;
import com.itheima.domain.cargo.Factory;
import com.itheima.domain.cargo.FactoryExample;
import com.itheima.service.cargo.ContractProductService;
import com.itheima.service.cargo.FactoryService;
import com.itheima.utils.FileUploadUtil;
import com.itheima.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;

    @Reference
    private FactoryService factoryService;

    @Autowired
    private FileUploadUtil fileUploadUtil;


    @RequestMapping(value = "/list", name = "货物列表查询")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String contractId
    ) {

        //1 查询当前合同下的所有货物
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdEqualTo(contractId);
        PageInfo pageInfo = contractProductService.findByPage(pageNum, pageSize, contractProductExample);
        request.setAttribute("page", pageInfo);

        //2 查询所有生产货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        //3 回显合同id
        request.setAttribute("contractId", contractId);
        return "/cargo/product/product-list";
    }


    @RequestMapping(value = "/toUpdate", name = "跳转货物修改页面")
    public String toUpdate(String id) {
        //1 查询当前货物信息
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct", contractProduct);


        //2 查询所有生产货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        return "/cargo/product/product-update";
    }

    @RequestMapping(value = "/edit", name = "货物新增或修改")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) throws Exception {

        //调用七牛云存储起来, 将访问路径保存到数据库
        String imagePath = fileUploadUtil.upload(productPhoto);
        contractProduct.setProductImage(imagePath);

        if (StringUtils.isEmpty(contractProduct.getId())) {
            //1 设置id
            contractProduct.setId(UUID.randomUUID().toString());

            //2. 设置企业信息
            contractProduct.setCompanyId(getCompanyId());
            contractProduct.setCompanyName(getCompanyName());

            //3. 调用service保存操作
            contractProductService.save(contractProduct);
        } else {
            contractProductService.update(contractProduct);

        }
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractProduct.getContractId();
    }

    @RequestMapping(value = "/delete", name = "货物删除")
    public String delete(String id, String contractId) {
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }


    @RequestMapping(value = "/toImport", name = "跳转货物批量上传的页面")
    public String toImport(String contractId) {

        request.setAttribute("contractId", contractId);

        return "/cargo/product/product-import";
    }


    @RequestMapping(value = "/import", name = "货物批量上传")
    public String imports(String contractId, MultipartFile file) throws IOException {
        //1. 已经知道了合同id 和  货物的一些信息(文件)

        //2. 解析file文件,获取到一个List<ContractProduct>
        //2-1) 从file获取一个工作簿(WorkBook)
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        //2-2) 从工作簿获取工作表(Sheet)
        Sheet sheet = workbook.getSheetAt(0);

        //2-3) 从工作表中开始读取行(Row)[1 ---- last+1]
        List<ContractProduct> list = new ArrayList<>();
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            //2-4) 从Row中获取单元格信息(Cell) [1 --- last]
            Object[] objs = new Object[9];
            for (int j = 1; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                //2-5) 从单元格中获取值
                Object cellValue = getCellValue(cell);

                //每读取到一个值, 然后将值赋值到数组中
                objs[j - 1] = cellValue;
            }
            //2-6) 将得到的值封装进一个货物对象
            //准备好一个数组, 就能通过这个数组,得的一个对象
            ContractProduct contractProduct = new ContractProduct(objs);

            //2-7) 补全一些属性
            contractProduct.setId(UUID.randomUUID().toString());
            contractProduct.setContractId(contractId);
            contractProduct.setCompanyId(getCompanyId());
            contractProduct.setCompanyName(getCompanyName());

            //设置工厂对应的id
            FactoryExample factoryExample = new FactoryExample();
            factoryExample.createCriteria().andFactoryNameEqualTo(contractProduct.getFactoryName());
            List<Factory> factories = factoryService.findAll(factoryExample);
            contractProduct.setFactoryId(factories.get(0).getId());

            list.add(contractProduct);
        }

        //4. 调用service进行货物的保存
        contractProductService.pathSave(list);

        //5. 跳转页面(合同列表)
        return "redirect:/cargo/contract/list.do";
    }


    //解析每个单元格的数据
    public static Object getCellValue(Cell cell) {
        Object obj = null;
        CellType cellType = cell.getCellType(); //获取单元格数据类型
        switch (cellType) {
            case STRING: {
                obj = cell.getStringCellValue();//字符串
                break;
            }
            //excel默认将日期也理解为数字
            case NUMERIC: {
                if (DateUtil.isCellDateFormatted(cell)) {
                    obj = cell.getDateCellValue();//日期
                } else {
                    obj = cell.getNumericCellValue(); // 数字
                }
                break;
            }
            case BOOLEAN: {
                obj = cell.getBooleanCellValue(); // 布尔
                break;
            }
            default: {
                break;
            }
        }
        return obj;
    }

}
