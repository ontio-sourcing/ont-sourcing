package com.github.ontio.sourcing.model.dto.ddo.identity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OntidPojo {

    @SerializedName("controls")
    @Expose
    private List<Control> controls = null;
    @SerializedName("isDefault")
    @Expose
    private Boolean isDefault;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("lock")
    @Expose
    private Boolean lock;
    @SerializedName("ontid")
    @Expose
    private String ontid;

    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getOntid() {
        return ontid;
    }

    public void setOntid(String ontid) {
        this.ontid = ontid;
    }

}