package com.ontology.sourcing.dao.contract;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Contract {

    private String ontid;

    private String companyOntid = "";

    private String detail;

    private String type;

    private Date timestamp;

    private String timestampSign;

    private String filehash;

    private String txhash;

    private Date createTime;

    private Date updateTime;

    private Integer status;  //

    private Integer height;  // 跨表

    private String revokeTx;

    public String getRevokeTx() {
        return revokeTx;
    }

    public void setRevokeTx(String revokeTx) {
        this.revokeTx = revokeTx;
    }

    public String getCompanyOntid() {
        return companyOntid;
    }

    public void setCompanyOntid(String companyOntid) {
        this.companyOntid = companyOntid;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getOntid() {
        return ontid;
    }

    public void setOntid(String ontid) {
        this.ontid = ontid == null ? null : ontid.trim();
    }

    public String getTxhash() {
        return txhash;
    }

    public void setTxhash(String txhash) {
        this.txhash = txhash == null ? null : txhash.trim();
    }

    public String getFilehash() {
        return filehash;
    }

    public void setFilehash(String filehash) {
        this.filehash = filehash == null ? null : filehash.trim();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestampSign() {
        return timestampSign;
    }

    public void setTimestampSign(String timestampSign) {
        this.timestampSign = timestampSign == null ? null : timestampSign.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}