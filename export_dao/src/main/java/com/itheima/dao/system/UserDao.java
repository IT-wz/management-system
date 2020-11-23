package com.itheima.dao.system;

import com.itheima.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    
    List<User> findAll(String companyId);

    void save(User user);

    User findById(String id);

    void update(User user);

    void delete(String id);

    List<String> findRoleIdsByUserId(String id);

    void deleteUserRoleByUserId(String userId);

    void saveUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    User findByEmail(String email);

}
