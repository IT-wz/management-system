package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ExtCproduct;
import com.itheima.domain.cargo.ExtCproductExample;
import com.itheima.service.cargo.ExtCproductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ContractDao contractDao;

    @Override
    public void save(ExtCproduct extCproduct) {
        //=====================================查询=============================================//
        //1.查询原有合同信息
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());


        //=====================================附件新增=============================================//
        //1. 计算小计金额
        Double amount = extCproduct.getPrice() * extCproduct.getCnumber();
        extCproduct.setAmount(amount);

        //2. 执行新增
        extCproductDao.insertSelective(extCproduct);

        //=====================================货物无影响=============================================//
        //=====================================合同修改============================================//
        //1. 修改合同中附件种数 + 1
        contract.setExtNum(contract.getExtNum() + 1);

        //2. 修改合同中的总金额 + amount
        contract.setTotalAmount(contract.getTotalAmount() + amount);

        //3. 执行修改
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ExtCproduct extCproduct) {
        //=====================================查询=============================================//
        //1.查询原有合同信息
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());

        //2.查询附件的原有信息
        ExtCproduct extCproductOld = extCproductDao.selectByPrimaryKey(extCproduct.getId());


        //=====================================附件修改=============================================//
        //1. 计算小计金额
        Double amount = extCproduct.getPrice() * extCproduct.getCnumber();
        extCproduct.setAmount(amount);

        //2. 执行修改
        extCproductDao.updateByPrimaryKeySelective(extCproduct);

        //=====================================货物无影响=============================================//
        //=====================================合同修改============================================//
        //1. 修改合同的总金额 - 原有附件价格  +  新价格
        contract.setTotalAmount(contract.getTotalAmount() - extCproductOld.getAmount() + amount);

        //2. 执行修改
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {

        //=====================================查询=============================================//
        //1. 查询到附件的信息(合同id)
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);

        //2.查询合同的原有信息
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());

        //=====================================附件删除=============================================//
        //1 执行删除
        extCproductDao.deleteByPrimaryKey(id);

        //=====================================货物无影响=============================================//
        //=====================================合同修改============================================//
        //1. 修改合同中附件种数 - 1
        contract.setExtNum(contract.getExtNum() - 1);
        //2. 修改合同中的总金额 - 当前附件的小计金额
        contract.setTotalAmount(contract.getTotalAmount() - extCproduct.getAmount());
        //3. 执行修改
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ExtCproduct> findAll(ExtCproductExample example) {
        return extCproductDao.selectByExample(example);
    }

    @Override
    public PageInfo findByPage(int pageNum, int pageSize, ExtCproductExample example) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExtCproduct> list = extCproductDao.selectByExample(example);
        return new PageInfo(list, 10);
    }
}
