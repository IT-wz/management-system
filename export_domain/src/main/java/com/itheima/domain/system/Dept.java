package com.itheima.domain.system;

import lombok.Data;
import java.io.Serializable;

@Data
public class Dept implements Serializable {
    private String id;
    private String deptName;
    private Dept parent;//注意这个字段, 代表的是父部门
    private Integer state;
    private String companyId;
    private String companyName;
}