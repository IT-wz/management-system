package com.itheima.vo;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class ContractProductVo implements Serializable {

    private String customName;		//客户名称
    private String contractNo;		//合同号，订单号
    private String productNo;		//货号
    private Integer cnumber;		//数量
    private String factoryName;		//厂家名称
    private Date deliveryPeriod;	//交货期限
    private Date shipTime;			//船期
    private String tradeTerms;		//贸易条款
}