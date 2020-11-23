package com.itheima.dao.system;

import com.itheima.domain.system.SysLog;
import java.util.List;

public interface SysLogDao {
    List<SysLog> findAll(String companyId);
    
    void save(SysLog log);
}