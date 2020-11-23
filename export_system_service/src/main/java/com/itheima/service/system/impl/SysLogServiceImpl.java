package com.itheima.service.system.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.SysLogDao;
import com.itheima.domain.system.SysLog;
import com.itheima.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    //保存日志
    public void save(SysLog sysLog) {
        sysLogDao.save(sysLog);
    }

    //分页查询日志
    public PageInfo findByPage(String companyId, int page, int size) {
        PageHelper.startPage(page, size);
        List<SysLog> list =   sysLogDao.findAll(companyId);
        return new PageInfo(list,10);
    }
}