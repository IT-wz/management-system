package com.itheima.domain.company;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


//只要是实体类,就必须实现Serializable接口
@Data
public class Company implements Serializable {
    private String id;
    private String name;
    //类型转换器:
    //1 自己实现类  只需要实现一次
    //2 注解  简单   每个实体类中需要的地方都要加
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
    private String address;
    private String licenseId;
    private String representative;
    private String phone;
    private String companySize;
    private String industry;
    private String remarks;
    private Integer state;
    private Double balance;
    private String city;
}
