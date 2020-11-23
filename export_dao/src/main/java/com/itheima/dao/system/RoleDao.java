package com.itheima.dao.system;

import com.itheima.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {

    List<Role> findAll(String companyId);

    void save(Role role);

    Role findById(String id);

    void update(Role role);

    void delete(String id);

    List<String> findModuleIdsByRoleId(String roleId);

    void deleteRoleModuleByRoleId(String roleId);

    void saveRoleModule(@Param("roleId") String roleId, @Param("moduleId") String moduleId);

}