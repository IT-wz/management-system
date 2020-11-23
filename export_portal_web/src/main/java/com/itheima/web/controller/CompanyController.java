package com.itheima.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class CompanyController {

    @Reference
    private CompanyService companyService;

    @RequestMapping("/apply")
    @ResponseBody
    public String apply(Company company) {
        try {
            //设置id和state
            company.setId(UUID.randomUUID().toString());
            company.setState(0);

            //调用service保存
            companyService.save(company);
            return "1";

        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
}
