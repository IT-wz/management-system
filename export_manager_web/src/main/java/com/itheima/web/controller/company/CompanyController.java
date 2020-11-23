package com.itheima.web.controller.company;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import com.itheima.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Reference
    private CompanyService companyService;

/*    @RequestMapping(value = "/list", name = "企业列表查询")
    public String list() {
        List<Company> list = companyService.findAll();
        request.setAttribute("list", list);

        return "/company/company-list";
    }*/


    //@RequiresPermissions  访问当前资源必须要有   "企业管理"   权限   相当于XML中的 /company/list.do = perms["企业管理"]
    /*@RequiresPermissions("企业管理")*/
    @RequestMapping(value = "/list", name = "企业列表查询")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "1") Integer pageSize
    ) {

        PageInfo<Company> pageInfo =  companyService.findByPage(pageNum, pageSize);
        request.setAttribute("page",pageInfo);

        return "/company/company-list";
    }


    @RequestMapping(value = "/toAdd", name = "跳转企业新增页面")
    public String toAdd() {
        return "/company/company-add";
    }

    @RequestMapping(value = "/toUpdate", name = "跳转企业修改页面")
    public String toUpdate(String id) {
        //1. 调用service根据id查询企业信息
        Company company = companyService.findById(id);

        //2. 通过request域传递到前台jsp
        request.setAttribute("company", company);

        //3. 转发到修改页面
        return "/company/company-update";
    }

    @RequestMapping(value = "/edit", name = "企业新增或修改")
    public String edit(Company company) {

        //根据实体是否有id判断执行新增还是修改
        if (StringUtils.isEmpty(company.getId())) {//空,---新增
            //1 设置id
            company.setId(UUID.randomUUID().toString());

            //2. 调用service保存操作
            companyService.save(company);
        } else {//修改

            //1 调用service修改操作
            companyService.update(company);

        }

        //3. 重定向到列表请求路径
        return "redirect:/company/list.do";
    }


    @RequestMapping(value = "/delete", name = "企业删除")
    public String delete(String id) {
        //1. 调用service删除
        companyService.delete(id);

        //2. 重定向到列表请求路径
        return "redirect:/company/list.do";
    }


}
