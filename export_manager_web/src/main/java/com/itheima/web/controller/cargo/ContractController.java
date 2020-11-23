package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ContractExample;
import com.itheima.service.cargo.ContractService;
import com.itheima.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;

    @RequestMapping(value = "/list", name = "合同列表查询")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        ContractExample contractExample = new ContractExample();
        ContractExample.Criteria criteria = contractExample.createCriteria();//用于封装查询条件
        criteria.andCompanyIdEqualTo(getCompanyId());

        //开始合同的细粒度权限控制
        Integer degree = getLoginUser().getDegree();
        if (degree == 4) {//4--普通员工：只能看自己的合同数据 --  create_by   ----   select * from co_contract where create_by = '登录人的id'
            criteria.andCreateByEqualTo(getLoginUser().getId());
        } else if (degree == 3) {//3--部门经理: 可以看到本部们所有员工的合同数据--   create_dept ---- select * from co_contract where create_dept = '登录人的部门id'
            criteria.andCreateDeptEqualTo(getLoginUser().getDeptId());
        } else if (degree == 2) {//2--总经理:可以看到本部门及所有子部门的合同数据 --create_dept ----  select * from co_contract where create_dept like '登录人的部门id  %  '
            criteria.andCreateDeptLike(getLoginUser().getDeptId() + "%");
        }

        contractExample.setOrderByClause("create_time desc");//按照创建时间倒序排
        PageInfo<Contract> pageInfo = contractService.findByPage(pageNum, pageSize, contractExample);
        request.setAttribute("page", pageInfo);

        return "/cargo/contract/contract-list";
    }


    @RequestMapping(value = "/toAdd", name = "跳转合同新增页面")
    public String toAdd() {
        return "/cargo/contract/contract-add";
    }

    @RequestMapping(value = "/toUpdate", name = "跳转合同修改页面")
    public String toUpdate(String id) {
        //1 查询当前合同信息
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);
        return "/cargo/contract/contract-update";
    }

    @RequestMapping(value = "/edit", name = "合同新增或修改")
    public String edit(Contract contract) {
        if (StringUtils.isEmpty(contract.getId())) {
            //1 设置id
            contract.setId(UUID.randomUUID().toString());

            //2. 设置企业信息
            contract.setCompanyId(getCompanyId());
            contract.setCompanyName(getCompanyName());

            //3. 设置 创建人id\创建部门id\创建时间
            contract.setCreateBy(getLoginUser().getId());
            contract.setCreateDept(getLoginUser().getDeptId());
            contract.setCreateTime(new Date());

            contract.setState(0);//草稿 0

            //4. 调用service保存操作
            contractService.save(contract);
        } else {
            contractService.update(contract);

        }
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping(value = "/delete", name = "合同删除")
    public String delete(String id) {
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    //提交
    @RequestMapping(value = "/submit", name = "合同提交")
    public String submit(String id) {
        //将合同状态改为1
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(1);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    //取消
    @RequestMapping(value = "/cancel", name = "合同取消")
    public String cancel(String id) {
        //将合同状态改为0
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(0);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }


}
