package com.itheima.domain.cargo;

import com.itheima.domain.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 购销合同实体类
 */
@Data
public class Contract extends BaseEntity implements Serializable {


    private String id;
    private String offeror;            //收购方
    private String contractNo;        //合同号，订单号
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date signingDate;        //签单日期
    private String inputBy;            //签单人
    private String checkBy;            //审单人
    private String inspector;        //验货员
    private Double totalAmount;        //总金额=货物的总金额+附件的总金额    冗余字段，为了进行分散计算
    private String crequest;        //要求
    private String customName;        //客户名称
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shipTime;            //船期
    private Integer importNum;        //重要程度
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deliveryPeriod;    //交货期限
    private Integer oldState;        //旧的状态，报运
    private Integer outState;        //出货状态，报运
    private String tradeTerms;        //贸易条款
    private String printStyle;        //打印板式，1打印一个货物2打印两个货物
    private String remark;            //备注
    private Integer state;            //状态：0草稿 1已上报待报运	2 已报运
    private Integer proNum; //货物数量
    private Integer extNum; //附件数量

    public Contract() {
    }

}
