package com.github.mpacala00.forum.model.constant;

public enum ResponseMessage {

    NOT_FOUND("Resource not found");

    private String value;

    ResponseMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
