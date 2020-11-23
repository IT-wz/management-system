package com.itheima.web.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController {

    @Reference
    private UserService userService;


//    @RequestMapping(value = "/login")
//    public String login(String email, String password) {
//        //1. 根据Email查询用户信息
//        User user = userService.findByEmail(email);
//
//        //如果没查到, 直接报错,返回
//        if (user == null) {
//            request.setAttribute("error", "当前用户不存在");
//            return "forward:/login.jsp";//强制不去拼接前后缀
//        }
//
//        //2. 如果查到了, 开始比对密码
//        //如果密码不一致, 直接报错, 返回
//        if (!StringUtils.equals(new Md5Hash(password, email, 2).toString(), user.getPassword())){
//            request.setAttribute("error", "密码错误");
//            return "forward:/login.jsp";//强制不去拼接前后缀
//        }
//
//        //如果一致,代表登录成功了, 保存用户信息到session中,跳转主页面
//        session.setAttribute("loginUser",user);
//
//        //登录成功之后开始获取当前用户的权限
//        List<Module> moduleList = userService.findModuleByUser(user);
//        session.setAttribute("modules",moduleList);
//
//        return "redirect:/home/main.do";
//    }


    @RequestMapping(value = "/login")
    public String login(String email, String password) {

        //1 封装email和password为Token
        AuthenticationToken authenticationToken = new UsernamePasswordToken(email, new Md5Hash(password, email, 2).toString());

        //2 调用subject的login方法登录
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(authenticationToken);

            //4 获取登录信息
            User loginUser = (User) subject.getPrincipal();

            session.setAttribute("loginUser", loginUser);
            List<Module> moduleList = userService.findModuleByUser(loginUser);
            session.setAttribute("modules", moduleList);
            return "redirect:/home/main.do";

        } catch (Exception e) {
            e.printStackTrace();
            //3 捕获异常
            request.setAttribute("error", "用户名或者密码错误");
            return "forward:/login.jsp";
        }
    }

    @RequestMapping("/home/main")
    public String main() {
        return "home/main";
    }

    @RequestMapping("/home/home")
    public String home() {
        return "home/home";
    }

    //退出
    @RequestMapping(value = "/logout", name = "用户登出")
    public String logout() {
        SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }
}
