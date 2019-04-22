package com.ontology.sourcing.dao.ddo;

public enum ActionTypes {

    //
    ADD(0, "ADD"),
    DELETE(1, "DELETE");

    //
    private final int id;
    private final String message;

    ActionTypes(int id, String message) {
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