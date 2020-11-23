package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll(String companyId);

    void save(Role role);

    Role findById(String id);

    void update(Role role);

    void delete(String id);

    PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize);

    List<String> findModuleIdsByRoleId(String roleId);

    void changeModule(String roleId, String moduleIds);
}