package com.enums;

public enum ResponseCode {
    OK(200), BAD_REQUEST(400);
    private final int code;
    ResponseCode(int code) {this.code = code;}
    public int getCode(){return this.code;}
}
