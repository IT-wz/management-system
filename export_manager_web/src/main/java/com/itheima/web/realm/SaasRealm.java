package com.itheima.web.realm;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.UserService;
import com.itheima.web.utils.SpringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//同时继承认证和授权
public class SaasRealm extends AuthorizingRealm {

//    @Reference
//    private UserService userService;


    //认证逻辑
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("=============================认证===========================");

        //1. 根据email查询用户信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String email = token.getUsername();

        UserService userService = (UserService) SpringUtil.getBean("userService");
        //2. 根据Email查询用户信息
        User user = userService.findByEmail(email);
        if (user == null) {
            return new SimpleAuthenticationInfo();
        } else {
            //3. 返回用户信息
            //参数1: 主角    参数2: 密码   参数3: 当前realm的名字
            return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
        }
    }

    //授权逻辑
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("=============================授权===========================");
        //1 查询当前用户拥有的权限
        User loginUser = (User) principalCollection.getPrimaryPrincipal();
        UserService userService = (UserService) SpringUtil.getBean("userService");
        List<Module> moduleList = userService.findModuleByUser(loginUser);

        //2 将结果告诉shiro
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Module module : moduleList) {
            info.addStringPermission(module.getName());
        }

        System.out.println(info.getStringPermissions());

        //3 shiro底层自己比对
        return info;
    }
}
