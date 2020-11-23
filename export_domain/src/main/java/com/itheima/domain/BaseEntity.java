package com.itheima.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    protected String createBy;//创建者的id
    protected String createDept;//创建者所在部门的id
    protected Date createTime;//创建时间
    protected String updateBy;//修改者的用户id
    protected Date updateTime;//更新时间
    protected String companyId;
    protected String companyName;

}
