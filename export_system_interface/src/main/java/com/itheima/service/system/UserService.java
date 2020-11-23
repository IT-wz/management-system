package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;

import java.util.List;

public interface UserService {

    List<User> findAll(String companyId);

    void save(User user);

    User findById(String id);

    void update(User user);

    void delete(String id);

    PageInfo<User> findByPage(String companyId, Integer pageNum, Integer pageSize);

    List<String> findRoleIdsByUserId(String id);

    void changeRole(String userId, String[] roleIds);

    User findByEmail(String email);

    List<Module> findModuleByUser(User user);

}
