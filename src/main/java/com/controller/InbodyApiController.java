package com.controller;

import com.model.response.CommonResponse;
import com.model.request.InbodyRequest;
import com.model.response.InbodyResponse;
import com.service.InbodyService;
import com.validator.CommonValidator;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class InbodyApiController {

    private final InbodyService inbodyService;
    private final CommonValidator commonValidator;

    public InbodyApiController(InbodyService inbodyService, CommonValidator commonValidator) {
        this.inbodyService = inbodyService;
        this.commonValidator = commonValidator;
    }

    @ApiOperation(value="인바디 점수 및 체형정보 조회", notes = "")
    @PostMapping("/api/inbody-result")
    public Result inbodyResult(@RequestBody @Valid InbodyRequest inbodyRequest, BindingResult bindingResult) {

        String errorMessage = "";
        String errorField = "";
        if(bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                errorField = error.getField();
                errorMessage = error.getDefaultMessage();
                break;
            }
            return new Result(new CommonResponse(errorField, errorMessage),
                    null);
        }

        CommonResponse existError = commonValidator.allowedRangeValidate(null,
                inbodyRequest.getHeight(),
                inbodyRequest.getWeight(),
                inbodyRequest.getPhysiqueWeight(),
                inbodyRequest.getBodyFatWeight());

        if(existError != null) return new Result(existError, null);

        try {
            InbodyResponse inbodyResponse = inbodyService.calcInbodyResult(inbodyRequest);
            log.info("인바디 점수 및 체형정보 조회 정상 요청");

            return new Result(new CommonResponse(), inbodyResponse);
        }
        catch (Exception e) {
            e.printStackTrace();

            return new Result(new CommonResponse(errorField,"인바디 점수 및 체형정보 조회 오류"),
                    null);
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Result<T> {
        private CommonResponse response;
        private T inbodyResult;
    }

}
