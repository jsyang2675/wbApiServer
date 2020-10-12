package com.controller;

import com.model.diet.DietResponseForm;
import com.model.diet.DietTypeForm;
import com.model.diet.DietRequestForm;
import com.service.DietService;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DietApiController {

    @Autowired
    private DietService dietService;

    @GetMapping("/")
    public String main() {
        return "api.wellbeeing";
    }

    @ApiOperation(value="한 주 식단리스트 조회", notes = "")
    @PostMapping("/api/weekDietList")
    public Result weekDietList(@RequestBody @Valid DietRequestForm dietRequestForm) {

        //하루필요에너지량 구하기
        double oneDayCalorieAmount = dietService.calcOneDayCalorieAmount(dietRequestForm);

        //하루섭취칼로리량 구하기 ( 하루필요에너지량 - 1000 )
        double oneDayTakeCalorieAmount = oneDayCalorieAmount - 1000;

        LocalDate date = LocalDate.now();
        LocalDate finDate = date.plusDays(7);

        //7일치 식단 리스트 담기
        List<DietResponseForm> dietResultList = new ArrayList<>();
        while(true) {
            //하루치 식단리스트 담기
            oneDayDietList(date, oneDayTakeCalorieAmount, dietResultList);
            date = date.plusDays(1);
            if(date.compareTo(finDate) == 0) break;
        }

        return new Result(oneDayCalorieAmount,
                oneDayTakeCalorieAmount,
                dietResultList);
    }

    @ApiOperation(value="당일 식단리스트 조회", notes = "")
    @PostMapping("/api/todayDietList")
    public Result todayDietList(@RequestBody @Valid DietRequestForm dietRequestForm) {

        //하루필요에너지량 구하기
        double oneDayCalorieAmount = dietService.calcOneDayCalorieAmount(dietRequestForm);

        //하루섭취칼로리량 구하기 ( 하루필요에너지량 - 1000 )
        double oneDayTakeCalorieAmount = oneDayCalorieAmount - 1000;

        LocalDate baseDate = LocalDate.now();

        //하루치 식단 리스트 담기
        List<DietResponseForm> dietResultList = new ArrayList<>();
        oneDayDietList(baseDate, oneDayTakeCalorieAmount, dietResultList);

        return new Result(oneDayCalorieAmount,
                oneDayTakeCalorieAmount,
                dietResultList);
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
     * @param dietList 최종식단리스트
     */
    private void oneDayDietList(LocalDate date, double oneDayTakeCalorieAmount, List<DietResponseForm> dietList) {
        DietResponseForm dietResponseForm = new DietResponseForm();
        dietResponseForm.setDate(date);
        //아침,점심,저녁 식단(하루치)을 담기 위한 list
        List<DietTypeForm> dietTypeList = new ArrayList<>();
        //아침식단구성
        DietTypeForm breakFastTimeDiet = dietService.constructBreakfastTimeDiet(oneDayTakeCalorieAmount);
        dietTypeList.add(breakFastTimeDiet);
        //점심식단구성
        DietTypeForm launchTimeDiet = dietService.constructLaunchTimeDiet(oneDayTakeCalorieAmount);
        dietTypeList.add(launchTimeDiet);
        //저녁식단구성
        DietTypeForm dinnerTimeDiet = dietService.constructDinnerTimeDiet(oneDayTakeCalorieAmount);
        dietTypeList.add(dinnerTimeDiet);
        //날짜별 하루치 식단 담기
        dietResponseForm.setOneDayDietList(dietTypeList);
        //식단리스트에 날짜별 식단 추가
        dietList.add(dietResponseForm);
    }

    @Data
    static class Result<T> {
        private Double oneDayCalorieAmount;
        private Double oneDayTakeCalorieAmount;
        private T dietList;

        public Result(Double oneDayCalorieAmount, Double oneDayTakeCalorieAmount, T dietList) {
            this.oneDayCalorieAmount = Math.floor(oneDayCalorieAmount*100)/100.0;
            this.oneDayTakeCalorieAmount = Math.floor(oneDayTakeCalorieAmount*100)/100.0;
            this.dietList = dietList;
        }
    }

}
