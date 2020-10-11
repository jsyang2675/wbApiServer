package com.controller;

import com.model.diet.DietFoodForm;
import com.model.inbody.InbodyRequestForm;
import com.model.inbody.InbodyResponseForm;
import com.service.InbodyService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InbodyApiController {

    @Autowired
    private InbodyService inbodyService;

    @ApiOperation(value="인바디 점수 및 체형정보 조회", notes = "")
    @PostMapping("/api/inbodyResult")
    public Result inbodyResult(@RequestBody @Valid InbodyRequestForm inbodyRequestForm) {
        InbodyResponseForm inbodyResponseForm = inbodyService.calcInbodyResult(inbodyRequestForm);
        return new Result(inbodyResponseForm);
    }

    @Data
    @AllArgsConstructor
    public class Result<T> {
        private T inbodyResult;
    }
}
