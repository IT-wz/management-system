package com.itheima.web.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;
import com.itheima.service.system.DeptService;
import com.itheima.service.system.RoleService;
import com.itheima.service.system.UserService;
import com.itheima.utils.MailUtil;
import com.itheima.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Reference
    private UserService userService;
    @Reference
    private DeptService deptService;
    @Reference
    private RoleService roleService;

    @Autowired
    private AmqpTemplate template;

    @RequestMapping(value = "/list", name = "用户列表查询")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageInfo<User> pageInfo = userService.findByPage(getCompanyId(), pageNum, pageSize);
        request.setAttribute("page", pageInfo);

        return "/system/user/user-list";
    }


    @RequestMapping(value = "/toAdd", name = "跳转用户新增页面")
    public String toAdd() {
        //1 查询所有的部门信息
        List<Dept> deptList = deptService.findAll(getCompanyId());
        request.setAttribute("deptList", deptList);

        return "/system/user/user-add";
    }

    @RequestMapping(value = "/toUpdate", name = "跳转用户修改页面")
    public String toUpdate(String id) {
        //1 查询当前用户信息
        User user = userService.findById(id);
        user.setPassword(null);
        request.setAttribute("user", user);

        //2 查询所有的部门信息
        List<Dept> deptList = deptService.findAll(getCompanyId());
        request.setAttribute("deptList", deptList);

        return "/system/user/user-update";
    }

    @RequestMapping(value = "/edit", name = "用户新增或修改")
    public String edit(User user) {
        //密码加密
        String oldPassword = user.getPassword();
        if (StringUtils.isNotEmpty(oldPassword)) {
            String password = new Md5Hash(oldPassword, user.getEmail(), 2).toString();
            user.setPassword(password);
        }

        if (StringUtils.isEmpty(user.getId())) {
            //1 设置id
            user.setId(UUID.randomUUID().toString());

            //2. 设置企业信息
            user.setCompanyId(getCompanyId());
            user.setCompanyName(getCompanyName());

            //3. 调用service保存操作
            userService.save(user);

            //==========当用户成功保存到数据库之后,我们向中间件投递一个消息==============//
            String to = user.getEmail();//收件人邮箱
            String title = "saasExport平台----新建用户成功";
            String content = "saasExport平台----用户创建成功,登录邮箱为当前邮箱,密码为:" + oldPassword;

            Map<String, Object> map = new HashMap<>();
            map.put("to", to);
            map.put("title", title);
            map.put("content", content);

            template.convertAndSend("mail.send", map);

            //==========当用户成功保存到数据库之后,我们给当前用户的邮箱发送一封邮件==============//
            //            String to = user.getEmail();//收件人邮箱
            //            String title = "saasExport平台----新建用户成功";
            //            String content = "saasExport平台----用户创建成功,登录邮箱为当前邮箱,密码为:" + oldPassword;
            //            MailUtil.sendMail(to, title, content);

        } else {
            userService.update(user);

        }
        return "redirect:/system/user/list.do";
    }


    @RequestMapping(value = "/delete", name = "用户删除")
    public String delete(String id) {
        userService.delete(id);
        return "redirect:/system/user/list.do";
    }

    @RequestMapping(value = "/roleList", name = "跳转用户分配角色页面")
    public String roleList(String id) {
        //1. 显示出用户名称(查询用户表)
        User user = userService.findById(id);
        request.setAttribute("user", user);

        //2. 显示出所有的角色, 等待勾选( 查询所有角色)
        List<Role> roleList = roleService.findAll(getCompanyId());
        request.setAttribute("roleList", roleList);

        //3. 回显当前用户已经分配了的角色 ( 查询中间表 )
        List<String> roleIds = userService.findRoleIdsByUserId(id);
        String userRoleStr = Arrays.toString(roleIds.toArray());
        System.out.println("===============>" + userRoleStr);
        request.setAttribute("userRoleStr", userRoleStr);//3 4   ------>   "3,4"

        //4. 跳转到给用户分配角色页面
        return "/system/user/user-role";
    }


    @RequestMapping(value = "/changeRole", name = "用户分配角色")
    public String changeRole(@RequestParam("userid") String userId, String[] roleIds) {
        //1. 改变用户角色
        userService.changeRole(userId, roleIds);

        //2. 跳转查询list
        return "redirect:/system/user/list.do";
    }

}
