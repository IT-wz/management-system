package com.itheima.web.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Module;
import com.itheima.service.system.ModuleService;
import com.itheima.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    @Reference
    private ModuleService moduleService;

    @RequestMapping(value = "/list", name = "模块列表查询")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageInfo<Module> pageInfo = moduleService.findByPage(pageNum, pageSize);
        request.setAttribute("page", pageInfo);

        return "/system/module/module-list";
    }


    @RequestMapping(value = "/toAdd", name = "跳转模块新增页面")
    public String toAdd() {
        //1 查询所有的模块信息
        List<Module> moduleList = moduleService.findAll();
        request.setAttribute("menus",moduleList);

        return "/system/module/module-add";
    }

    @RequestMapping(value = "/toUpdate", name = "跳转模块修改页面")
    public String toUpdate(String id) {
        //1 查询当前模块信息
        Module module = moduleService.findById(id);
        request.setAttribute("module", module);

        //2 查询所有的模块信息
        List<Module> moduleList = moduleService.findAll();
        request.setAttribute("menus",moduleList);

        return "/system/module/module-update";
    }

    @RequestMapping(value = "/edit", name = "模块新增或修改")
    public String edit(Module module) {
        //1. 处理顶级模块不能新增和修改的问题
        if (StringUtils.isEmpty(module.getId())) {
            //1 设置id
            module.setId(UUID.randomUUID().toString());

            //2. 调用service保存操作
            moduleService.save(module);
        } else {
            moduleService.update(module);

        }
        return "redirect:/system/module/list.do";
    }


    @RequestMapping(value = "/delete", name = "模块删除")
    public String delete(String id) {
        moduleService.delete(id);
        return "redirect:/system/module/list.do";
    }


}
