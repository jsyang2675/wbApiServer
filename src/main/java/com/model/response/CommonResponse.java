package com.model.response;

import com.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse {
    private int code;
    private boolean status;
    private String errorField;
    private String errorMessage;

    //실패시
    public CommonResponse(String errorField, String errorMessage) {
        this.code = ResponseCode.BAD_REQUEST.getCode();
        this.errorField = errorField;
        this.status = false;
        this.errorMessage = errorMessage;
    }
    //성공시
    public CommonResponse() {
        this.code = ResponseCode.OK.getCode();
        this.status = true;
    }

}
