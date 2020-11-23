package com.itheima.dao.system;

import com.itheima.domain.system.Module;
import java.util.List;

public interface ModuleDao {
    List<Module> findAll();

    void save(Module module);

    Module findById(String id);

    void update(Module module);

    void delete(String id);

    List<Module> findByBelong(Integer belong);

    List<Module> findByUserId(String id);
}