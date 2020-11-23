package com.itheima.domain.cargo;

import com.itheima.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 合同下货物的实体类
 */
@Data
public class ContractProduct1 implements Serializable {

    private String createBy;//创建者的id
    private String createDept;//创建者所在部门的id
    private Date createTime;//创建时间
    private String updateBy;//修改者的用户id
    private Date updateTime;//更新时间
    private String companyId;
    private String companyName;

    private String id;
    private String productNo;        //货号
    private String productImage;    //图片路径
    private String productDesc;        //货描
    private String loadingRate;        //报运：装率    1/3
    private Integer boxNum;            //报运：箱数    100
    private String packingUnit;        //包装单位：PCS/SETS   支/箱
    private Integer cnumber;        //数量                            300
    private Integer outNumber;        //报运：出货数量            200
    private Integer finished;        //报运：是否完成		no
    private String productRequest;    //要求
    private Double price;            //单价
    private Double amount;            //总金额，冗余
    private Integer orderNo;        //排序号
    private String contractId;      //合同号
    private String factoryName;        //厂家名称，冗余字段
    private String factoryId;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDept() {
        return createDept;
    }

    public void setCreateDept(String createDept) {
        this.createDept = createDept;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getLoadingRate() {
        return loadingRate;
    }

    public void setLoadingRate(String loadingRate) {
        this.loadingRate = loadingRate;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public String getPackingUnit() {
        return packingUnit;
    }

    public void setPackingUnit(String packingUnit) {
        this.packingUnit = packingUnit;
    }

    public Integer getCnumber() {
        return cnumber;
    }

    public void setCnumber(Integer cnumber) {
        this.cnumber = cnumber;
    }

    public Integer getOutNumber() {
        return outNumber;
    }

    public void setOutNumber(Integer outNumber) {
        this.outNumber = outNumber;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public String getProductRequest() {
        return productRequest;
    }

    public void setProductRequest(String productRequest) {
        this.productRequest = productRequest;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public ContractProduct1() {
    }

    public ContractProduct1(Object[] objs) {
        this.factoryName = objs[0].toString();
        this.productNo = objs[1].toString();
        this.cnumber = ((Double) objs[2]).intValue();
        this.packingUnit = objs[3].toString();
        this.loadingRate = objs[4].toString();
        this.boxNum = ((Double) objs[5]).intValue();
        this.price = (Double) objs[6];            //单价
        this.productRequest = objs[7].toString();
        this.productDesc = objs[8].toString();
    }
}
