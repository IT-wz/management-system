package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ContractProductDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ContractProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public void save(ContractProduct contractProduct) {
        //===========================================查询=======================================//
        //1. 知道的是货物对象,查询的是合同对象
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());

        //========================================附件无影响=====================================//
        //========================================货物增加======================================//
        //1 计算出当前货物的小计金额
        Double amount = contractProduct.getPrice() * contractProduct.getCnumber();
        contractProduct.setAmount(amount);

        //2 执行保存操作
        contractProductDao.insertSelective(contractProduct);

        //========================================合同修改======================================//
        //1. 修改货物的种数 + 1
        contract.setProNum(contract.getProNum() + 1);

        //2. 修改合同总金额 + 货物的小计金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);

        //3.执行修改
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ContractProduct contractProduct) {

        //===========================================查询=======================================//
        //1 查询原有的合同信息
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());

        //2 查询原有货物信息
        ContractProduct contractProductOld = contractProductDao.selectByPrimaryKey(contractProduct.getId());

        //========================================附件无影响=====================================//
        //========================================货物修改======================================//
        //1 计算出当前货物的小计金额
        Double amount = contractProduct.getPrice() * contractProduct.getCnumber();
        contractProduct.setAmount(amount);

        //2 执行修改
        contractProductDao.updateByPrimaryKeySelective(contractProduct);

        //========================================合同修改======================================//
        //1 修改合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() - contractProductOld.getAmount() + amount);

        //2 执行修改
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //===========================================查询=======================================//
        //1 知道的是货物id  查询当前货物下的所有附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);

        //2 根据货物id查询当前货物信息(合同id)
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);

        //3.根据上一步得到的合同id查询合同信息
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());


        //========================================附件删除=====================================//
        for (ExtCproduct extCproduct : extCproducts) {
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }

        //========================================货物删除======================================//
        contractProductDao.deleteByPrimaryKey(id);

        //========================================合同修改======================================//
        //1 修改货物种数  - 1
        contract.setProNum(contract.getProNum() - 1);

        //2 修改附件种数  - 当前货物对应附件的集合的大小
        contract.setExtNum(contract.getExtNum() - extCproducts.size());

        //3 修改总金额  -  (当前货物金额 + 当前货物下的附件的总金额)
        Double d = contractProduct.getAmount();
        for (ExtCproduct extCproduct : extCproducts) {
            d += extCproduct.getAmount();
        }

        contract.setTotalAmount(contract.getTotalAmount() - d);

        //4 执行修改
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }


    @Override
    public PageInfo findByPage(int pageNum, int pageSize, ContractProductExample example) {
        PageHelper.startPage(pageNum, pageSize);
        List<ContractProduct> list = contractProductDao.selectByExample(example);
        return new PageInfo(list, 10);
    }

    @Override
    public void pathSave(List<ContractProduct> list) {
        for (ContractProduct contractProduct : list) {
            this.save(contractProduct);
        }
    }
}
