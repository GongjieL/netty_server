package com.gjie.netty.constant;

public enum Formatter {
    XML("application/json;charset=UTF-8"), JSON("application/json;charset=UTF-8");
    private String contentType;

    Formatter(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
