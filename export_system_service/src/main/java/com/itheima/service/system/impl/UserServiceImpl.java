package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.ModuleDao;
import com.itheima.dao.system.UserDao;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(String id) {
        userDao.delete(id);
    }

    @Override
    public PageInfo<User> findByPage(String companyId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userDao.findAll(companyId);
        return new PageInfo<>(list, 10);
    }

    @Override
    public List<String> findRoleIdsByUserId(String id) {
        return userDao.findRoleIdsByUserId(id);
    }

    @Override
    public void changeRole(String userId, String[] roleIds) {
        //1. 删除中间表中用户的现有角色对应的id
        userDao.deleteUserRoleByUserId(userId);

        //2. 重新向中间表插入新的用户和角色id
        if (roleIds != null && roleIds.length > 0) {
            for (String roleId : roleIds) {
                userDao.saveUserRole(userId, roleId);
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public List<Module> findModuleByUser(User user) {
        //登录之后，根据登录的用户信息确定身份, 根据身份确定权限
        Integer degree = user.getDegree();
        if (degree == 0) {//saas管理员
            return moduleDao.findByBelong(degree);
        } else if (degree == 1) {//企业管理员
            return moduleDao.findByBelong(degree);
        } else {//企业普通员工
            return moduleDao.findByUserId(user.getId());
        }
    }
}
