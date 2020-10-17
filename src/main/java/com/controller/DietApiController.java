package com.controller;

import com.model.response.CommonResponse;
import com.model.response.DietResponse;
import com.model.response.DietTypeDto;
import com.model.request.DietRequest;
import com.service.DietService;
import com.validator.CommonValidator;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class DietApiController {

    private final DietService dietService;
    private final CommonValidator commonValidator;

    public DietApiController(DietService dietService, CommonValidator commonValidator) {
        this.dietService = dietService;
        this.commonValidator = commonValidator;
    }

    @GetMapping("/")
    public String main() {
        return "api.wellbeeing";
    }

    @ApiOperation(value="한 주 식단리스트 조회", notes = "")
    @PostMapping("/api/week-diet")
    public Result weekDietList(@RequestBody @Valid DietRequest dietRequest, BindingResult bindingResult) {
        double oneDayCalorieAmount = 0.0;
        double oneDayTakeCalorieAmount = 0.0;

        //요청데이터검증
        CommonResponse existError = requestBodyValidate(bindingResult, dietRequest);
        if(existError != null) new Result(existError,
                oneDayCalorieAmount,
                oneDayTakeCalorieAmount,
                null);

        try {
            List<DietResponse> dietResultList = new ArrayList<>();
            //하루필요에너지량 구하기
            oneDayCalorieAmount = dietService.calcOneDayCalorieAmount(dietRequest);
            //하루섭취칼로리량 구하기 ( 하루필요에너지량 - 1000 )
            oneDayTakeCalorieAmount = oneDayCalorieAmount - 1000;

            LocalDate date = LocalDate.now();
            LocalDate finDate = date.plusDays(7);

            while(true) {
                //하루치 식단리스트 담기
                DietResponse dietResponse = oneDayDietList(date, oneDayTakeCalorieAmount);
                dietResultList.add(dietResponse);
                date = date.plusDays(1);
                if(date.compareTo(finDate) == 0) break;
            }
            log.info("일주일치 식단리스트 조회 정상 요청");

            return new Result(new CommonResponse(),
                    oneDayCalorieAmount,
                    oneDayTakeCalorieAmount,
                    dietResultList);
        }
        catch (Exception e) {
            e.printStackTrace();

            return new Result(new CommonResponse("", "일주일치 식단리스트 조회 오류"),
                    oneDayCalorieAmount,
                    oneDayTakeCalorieAmount,
                    null);
        }
    }

    @ApiOperation(value="당일 식단리스트 조회", notes = "")
    @PostMapping("/api/today-diet")
    public Result todayDietList(@RequestBody @Valid DietRequest dietRequest, BindingResult bindingResult) {
        double oneDayCalorieAmount = 0.0;
        double oneDayTakeCalorieAmount = 0.0;

        //요청데이터검증
        CommonResponse existError = requestBodyValidate(bindingResult, dietRequest);
        if(existError != null) new Result(existError,
                oneDayCalorieAmount,
                oneDayTakeCalorieAmount,
                null);

        try {
            List<DietResponse> dietResultList = new ArrayList<>();
            //하루필요에너지량 구하기
            oneDayCalorieAmount = dietService.calcOneDayCalorieAmount(dietRequest);

            //하루섭취칼로리량 구하기 ( 하루필요에너지량 - 1000 )
            oneDayTakeCalorieAmount = oneDayCalorieAmount - 1000;

            LocalDate baseDate = LocalDate.now();

            //하루치 식단 리스트 담기
            DietResponse dietResponse = oneDayDietList(baseDate, oneDayTakeCalorieAmount);
            dietResultList.add(dietResponse);

            log.info("당일 식단리스트 조회 정상 요청");

            return new Result(new CommonResponse(),
                    oneDayCalorieAmount,
                    oneDayTakeCalorieAmount,
                    dietResultList);
        }
        catch (Exception e) {
            e.printStackTrace();

            return new Result(new CommonResponse("", "당일 식단리스트 조회 오류"),
                    oneDayCalorieAmount,
                    oneDayTakeCalorieAmount,
                    null);
        }
    }

    /**
     * 요청데이터검증
     * @param bindingResult
     * @param dietRequest
     * @return 통과시 null반환
     */
    private CommonResponse requestBodyValidate(BindingResult bindingResult, DietRequest dietRequest) {
        //데이터타입검증
        if(bindingResult.hasErrors()) {
            String errorField = "";
            String errorMessage = "";
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                errorField = error.getField();
                errorMessage = error.getDefaultMessage();
                break;
            }
            return new CommonResponse(errorField, errorMessage);
        }

        //데이터조건검증
        return commonValidator.allowedRangeValidate(dietRequest.getAge(),
                dietRequest.getHeight(),
                dietRequest.getWeight(),
                dietRequest.getPhysiqueWeight(),
                dietRequest.getBodyFatWeight());
    }

    /**
     * 식단 담기
     * 최종식단리스트 object 구조
     * List<@DietListForm>(최종식단리스트) [
     *      {
     *          date (날짜),
     *          List<DietTypeForm>(날짜별식단구성) [
     *              {
     *                  type (아침,점심,저녁구분),
     *                  List<DietFoodForm>(아침,점심,저녁별 식단구성) [
     *                      {
     *                          음식정보..
     *                      }
     *                  ]
     *              }
     *          [
     *      }
     * ]
     * @param date 날짜
     * @param oneDayTakeCalorieAmount 하루섭취칼로리량
     */
    private DietResponse oneDayDietList(LocalDate date, double oneDayTakeCalorieAmount) {
        //아침,점심,저녁 식단(하루치)을 담기 위한 list
        List<DietTypeDto> dietTypeDtoList = new ArrayList<>();
        //아침식단구성
        DietTypeDto breakFastTimeDiet = dietService.constructBreakfastTimeDiet(oneDayTakeCalorieAmount);
        dietTypeDtoList.add(breakFastTimeDiet);
        //점심식단구성
        DietTypeDto launchTimeDiet = dietService.constructLaunchTimeDiet(oneDayTakeCalorieAmount);
        dietTypeDtoList.add(launchTimeDiet);
        //저녁식단구성
        DietTypeDto dinnerTimeDiet = dietService.constructDinnerTimeDiet(oneDayTakeCalorieAmount);
        dietTypeDtoList.add(dinnerTimeDiet);
        return new DietResponse(date, dietTypeDtoList);
    }

    @Getter
    @NoArgsConstructor
    static class Result<T> {
        private CommonResponse response;
        private Double oneDayCalorieAmount;
        private Double oneDayTakeCalorieAmount;
        private T dietList;

        public Result(CommonResponse response, Double oneDayCalorieAmount, Double oneDayTakeCalorieAmount, T dietList) {
            this.response = response;
            this.oneDayCalorieAmount = Math.floor(oneDayCalorieAmount*100)/100.0;
            this.oneDayTakeCalorieAmount = Math.floor(oneDayTakeCalorieAmount*100)/100.0;
            this.dietList = dietList;
        }
    }

}
