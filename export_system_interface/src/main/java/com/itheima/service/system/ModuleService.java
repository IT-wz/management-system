package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Module;

import java.util.List;

public interface ModuleService {

    List<Module> findAll();

    void save(Module module);

    Module findById(String id);

    void update(Module module);

    void delete(String id);

    PageInfo<Module> findByPage(int pageNum, int pageSize);
}