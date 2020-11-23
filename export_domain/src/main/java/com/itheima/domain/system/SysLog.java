package com.itheima.domain.system;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SysLog implements Serializable {
    private String id;
    private String userName;
    private String ip;
    private Date time;
    private String method;
    private String action;
    private String companyId;
    private String companyName;
}