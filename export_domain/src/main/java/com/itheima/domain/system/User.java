package com.itheima.domain.system;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private String id; // 用户id
    private String deptId; // 部门id
    private String email; // 邮箱
    private String userName; //用户名
    private String station; // 职务名称
    private String password; // 密码
    private String state; // 状态  1：可用、0：不可用
    private String companyId; // 企业id
    private String companyName; // 企业名称
    private String deptName;  // 部门名称
    private String managerId; // 上级id
    private String gender; // 性别
    private String telephone; // 手机号
    private String birthday; // 生日

    /*
       0-saas管理员
       1-企业管理员
       2-总经理
       3-部门经理
       4-普通员工
   */
    private Integer degree; // 等级
    private Double salary; // 工资
    private String joinDate; // 入职日期
    private Integer orderNo; // 排序号
    private String createBy; // 创建人
    private String createDept; // 创建人所在部门
    private Date createTime; // 创建时间
    private String updateBy; // 更新人
    private Date updateTime; // 更新时间
    private String remark; // 备注
}