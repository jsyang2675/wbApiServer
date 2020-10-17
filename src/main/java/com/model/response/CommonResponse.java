package com.model;

import com.enums.ResponseCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommonResponseForm {
    private int code;
    private boolean status;
    private String message;

    //실패시
    public CommonResponseForm(String message) {
        this.code = ResponseCode.BAD_REQUEST.getCode();
        this.status = false;
        this.message = message;
    }
    //성공시
    public CommonResponseForm() {
        this.code = ResponseCode.OK.getCode();
        this.status = true;
        this.message = "성공";
    }
}
