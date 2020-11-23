package com.itheima.web.filters;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class MyPermissionsAuthorizationFilter extends AuthorizationFilter {
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);  //封装当前用户信息, 后面会将查询到的权限放在里面 {"部门管理","查看部门","新增部门"}
        String[] perms = (String[]) mappedValue; // 从配置文件中读取到的访问资源所需要的权限 {"新增部门","删除部门"}

        boolean isPermitted = false;
        if (perms != null && perms.length > 0) {//当配置了权限要求时,才去做判断
            for (String perm : perms) {
                if (subject.isPermitted(perm)) {
                    isPermitted = true;
                    break;
                }
            }
        } else {
            isPermitted = true;
        }
        return isPermitted;
    }
}