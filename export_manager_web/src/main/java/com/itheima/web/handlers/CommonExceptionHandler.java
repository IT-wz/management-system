package com.itheima.web.handlers;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

//全局异常处理器
@ControllerAdvice
public class CommonExceptionHandler {

    //捕获一个授权的异常, 跳转一个提示无权访问的页面
    @ExceptionHandler(AuthorizationException.class)
    public String authorizationExceptionHandler(Exception e, HttpServletRequest request) {

        //1. 记录异常信息
        e.printStackTrace();

        //2. 返回一个异常页面
        return "redirect:/unauthorized.jsp";
    }


    //实现一个处理异常的方法
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, HttpServletRequest request) {

        //1. 记录异常信息
        e.printStackTrace();
        request.setAttribute("errorMsg", e.getMessage());

        //2. 返回一个异常页面
        return "error";
    }
}
