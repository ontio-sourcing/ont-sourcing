package com.ontology.sourcing.model.utils;

import com.github.ontio.network.exception.RestfulException;
import com.google.gson.Gson;
import com.ontology.sourcing.utils.exp.ErrorCode;
import com.ontology.sourcing.utils.GlobalVariable;
import lombok.Data;

@Data
public class Result {

    //
    public Result() {
        this.setVersion(GlobalVariable.API_VERSION);
        this.setError(0);
        this.setDesc("SUCCESS");
        this.setAction("");
        this.setResult("");
    }

    public Result(String action) {
        this.setVersion(GlobalVariable.API_VERSION);
        this.setError(0);
        this.setDesc("SUCCESS");
        this.setAction(action);
        this.setResult("");
    }

    //
    private String Action;
    private int Error;
    private String Desc;
    private Object Result;
    private String Version;

    //
//    public void setErrorAndDesc(Exception e){
//        ExceptionMsg msg = new Gson().fromJson(e.getMessage(), ExceptionMsg.class);
//        this.setError(msg.getError());
//        this.setDesc(msg.getDesc());
//    }
    public void setErrorAndDesc(Exception e){
        this.setError(ErrorCode.BLOCKCHAIN.getId());
        this.setDesc(e.getMessage());
    }

    public void setErrorAndDesc(RestfulException e){
        RestfulExceptionMsg msg = new Gson().fromJson(e.getMessage(), RestfulExceptionMsg.class);
        this.setError(msg.getError());
        this.setDesc(msg.getDesc());
        this.setResult(msg.getResult());
    }

    //
    public void setErrorAndDesc(ErrorCode error){
        this.setError(error.getId());
        this.setDesc(error.getMessage());
    }
}
