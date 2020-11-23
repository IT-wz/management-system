package com.itheima.domain.system;

import lombok.Data;

import java.io.Serializable;

@Data
public class Module implements Serializable {
    private String id;
    private String parentId;
    private String parentName;
    private String name;
    private int layerNum;
    private int isLeaf;
    private String ico;
    private String cpermission;
    private String curl;
    private String ctype;//主菜单 二级菜单 按钮
    private String state;
    private String belong;//属于SaaS还是企业
    private String cwhich;
    private int quoteNum;
    private String remark;
    private int orderNo;
}