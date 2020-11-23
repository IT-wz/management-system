package com.itheima.web.controller;

import com.itheima.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;

    protected User getLoginUser() {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            request.setAttribute("error", "登录过期, 请重新登录");
            try {
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                throw new RuntimeException();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    protected String getCompanyId() {
        return getLoginUser().getCompanyId();
    }

    protected String getCompanyName() {
        return getLoginUser().getCompanyName();
    }
}
