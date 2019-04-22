package com.ontology.sourcing.model.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventPojo {

    @SerializedName("GasConsumed") @Expose private Integer gasConsumed;
    @SerializedName("event") @Expose private List<Notify> notify = null;
    @SerializedName("TxHash") @Expose private String txHash;
    @SerializedName("State") @Expose private Integer state;

    public Integer getGasConsumed() {
        return gasConsumed;
    }

    public void setGasConsumed(Integer gasConsumed) {
        this.gasConsumed = gasConsumed;
    }

    public List<Notify> getNotify() {
        return notify;
    }

    public void setNotify(List<Notify> notify) {
        this.notify = notify;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}