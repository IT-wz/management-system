package com.itheima.web.controller.stat;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.stat.StatService;
import com.itheima.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stat")
public class StatController extends BaseController {

    @Reference
    private StatService statService;


    @RequestMapping(value = "/toCharts", name = "跳转统计页面")
    public String toCharts(String chartsType) {

        return "/stat/stat-" + chartsType;
    }


    @RequestMapping(value = "/factoryCharts", name = "厂家销售统计")
    @ResponseBody
    public List<Map> factoryCharts() {
        return statService.findFactoryCharts(getCompanyId());
    }


    @RequestMapping(value = "/sellCharts", name = "产品的销量排行榜")
    @ResponseBody
    public List<Map> sellCharts() {
        return statService.findSellCharts(getCompanyId());
    }


    @RequestMapping(value = "/onlineCharts", name = "在线人数统计")
    @ResponseBody
    public List<Map> onlineCharts() {
        return statService.findOnlineCharts(getCompanyId());
    }

}
