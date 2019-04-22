package com.ontology.sourcing.utils.exp;

public enum ErrorCode {

    //
    SUCCESSS(0, "SUCCESS"),
    //
    PARAMS(61001, "INVALID_PARAMS"),
    //
    ONTID_NOT_EXIST(71001, "ONTID_NOT_EXIST"),
    ONTID_PubKey_Already_EXIST(71002, "ONTID_PubKey_Already_EXIST"),
    //
    BLOCKCHAIN(81001, "BLOCKCHAIN_ERROR"),
    BLOCKCHAIN_CONFIRM_TIMEOUT(81002, "BLOCKCHAIN_CONFIRM_TIMEOUT");

    //
    private final int id;
    private final String message;

    ErrorCode(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}