package com.itheima.web.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.service.system.DeptService;
import com.itheima.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Reference
    private DeptService deptService;

    @RequestMapping(value = "/list", name = "部门列表查询")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageInfo<Dept> pageInfo = deptService.findByPage(getCompanyId(), pageNum, pageSize);
        request.setAttribute("page", pageInfo);

        return "/system/dept/dept-list";
    }


    @RequestMapping(value = "/toAdd", name = "跳转部门新增页面")
    public String toAdd() {
        //1 查询所有的部门信息
        List<Dept> deptList = deptService.findAll(getCompanyId());
        request.setAttribute("deptList", deptList);

        return "/system/dept/dept-add";
    }

    @RequestMapping(value = "/toUpdate", name = "跳转部门修改页面")
    public String toUpdate(String id) {
        //1 查询当前部门信息
        Dept dept = deptService.findById(id);
        request.setAttribute("dept", dept);

        //2 查询所有的部门信息
        List<Dept> deptList = deptService.findAll(getCompanyId());
        request.setAttribute("deptList", deptList);

        return "/system/dept/dept-update";
    }

    @RequestMapping(value = "/edit", name = "部门新增或修改")
    public String edit(Dept dept) {
        //1. 处理顶级部门不能新增和修改的问题
        if (StringUtils.isEmpty(dept.getParent().getId())) {
            dept.getParent().setId(null);
        }

        if (StringUtils.isEmpty(dept.getId())) {
            //1 设置id
            dept.setId(UUID.randomUUID().toString());

            //2. 设置企业信息
            dept.setCompanyId(getCompanyId());
            dept.setCompanyName(getCompanyName());

            //3. 调用service保存操作
            deptService.save(dept);
        } else {
            deptService.update(dept);

        }
        return "redirect:/system/dept/list.do";
    }

    @RequestMapping(value = "/preDelete", name = "删除部门判断")
    @ResponseBody
    public List<Dept> preDelete(String id) {
        List<Dept> childrenDeptList = deptService.findChildrenDept(id);
        return childrenDeptList;
    }

    @RequestMapping(value = "/delete", name = "部门删除")
    public String delete(String id) {
        deptService.delete(id);
        return "redirect:/system/dept/list.do";
    }


}
