package com.ontology.sourcing.dao.ddo;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Action {

    private String ontid;

    private String control;

    private String txhash;

    private Integer type;

    private Date createTime;

    private Date updateTime;

    private String detail;

    public String getOntid() {
        return ontid;
    }

    public void setOntid(String ontid) {
        this.ontid = ontid == null ? null : ontid.trim();
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control == null ? null : control.trim();
    }

    public String getTxhash() {
        return txhash;
    }

    public void setTxhash(String txhash) {
        this.txhash = txhash == null ? null : txhash.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
}