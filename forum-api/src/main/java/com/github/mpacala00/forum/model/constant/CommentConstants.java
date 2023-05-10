package com.github.mpacala00.forum.model.constant;

public enum CommentConstants {

    DELETED_BODY("[deleted]");

    private String value;

    CommentConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
