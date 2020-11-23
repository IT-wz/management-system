package com.itheima.domain.system;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Role implements Serializable {

    private String id;
    private String name;
    private String remark;
    private String companyId;
    private String companyName;
    private Integer orderNo;
    private String createBy;
    private String createDept;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
}