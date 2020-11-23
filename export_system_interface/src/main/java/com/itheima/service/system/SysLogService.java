package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.SysLog;

public interface SysLogService {
    void save(SysLog sysLog);

    PageInfo<SysLog> findByPage(String companyId, int pageNum, int pageSize);
}