package com.ontology.sourcing.model.dto.sfl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SFLResponse {

    @SerializedName("responseData")
    @Expose
    private String responseData;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("errMessage")
    @Expose
    private String errMessage;
    @SerializedName("codeAddr")
    @Expose
    private String code;
    @SerializedName("notarySuc")
    @Expose
    private Boolean notarySuc;
    @SerializedName("indentifySuc")
    @Expose
    private Boolean indentifySuc;
    @SerializedName("downloadSuc")
    @Expose
    private Boolean downloadSuc;
    @SerializedName("suc")
    @Expose
    private Boolean suc;

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getNotarySuc() {
        return notarySuc;
    }

    public void setNotarySuc(Boolean notarySuc) {
        this.notarySuc = notarySuc;
    }

    public Boolean getIndentifySuc() {
        return indentifySuc;
    }

    public void setIndentifySuc(Boolean indentifySuc) {
        this.indentifySuc = indentifySuc;
    }

    public Boolean getDownloadSuc() {
        return downloadSuc;
    }

    public void setDownloadSuc(Boolean downloadSuc) {
        this.downloadSuc = downloadSuc;
    }

    public Boolean getSuc() {
        return suc;
    }

    public void setSuc(Boolean suc) {
        this.suc = suc;
    }

}