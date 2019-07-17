package com.github.ontio.sourcing.exception;


public enum ErrorInfo {

    SUCCESS(0, "SUCCESS"),

    //
    SUCCESSS(0, "SUCCESS"),
    //
    PARAMS(61001, "INVALID_PARAMS"),
    //
    ONTID_NOT_EXIST(71001, "ONTID_NOT_EXIST"),
    ONTID_EXIST(71002, "ONTID_EXIST"),
    ONTID_PubKey_EXIST(71003, "ONTID_PubKey_EXIST"),
    //
    BLOCKCHAIN(81001, "BLOCKCHAIN_ERROR"),
    FILEHASH_NOT_EXIST(81002, "FILEHASH_NOT_EXIST"),
    TXHASH_NOT_EXIST(81003, "TXHASH_NOT_EXIST"),
    //
    SFL_ERROR(90001, "司法链接口调用失败"),
    //
    INTERNAL_SERVER_ERROR(100000, ""),

    //
    EXCEPTION(63002, "FAIL, exception.");

    private Integer code;
    private String msg;

    ErrorInfo(Integer code,
              String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return code;
    }

    public String desc() {
        return msg;
    }


}
