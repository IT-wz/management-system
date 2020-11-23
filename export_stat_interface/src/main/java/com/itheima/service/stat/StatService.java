package com.itheima.service.stat;

import java.util.List;
import java.util.Map;

/*
    统计分析
 */
public interface StatService {

    // 厂家销售统计 （统计每个厂家的销售额）
    List<Map> findFactoryCharts(String companyId);

    // 产品的销量排行榜（统计产品销售数量最高的前10名）
    List<Map> findSellCharts(String companyId);

    // 系统访问压力图（每个小时访问系统的人员数量）
    List<Map> findOnlineCharts(String companyId);
}
