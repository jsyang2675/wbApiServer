package com.service;

import com.domain.FoodDetail;
import com.enums.diet.FoodType;
import com.enums.Gender;
import com.model.diet.DietFoodForm;
import com.model.diet.DietTypeForm;
import com.model.diet.DietRequestForm;
import com.repository.FoodDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DietService {

    @Autowired
    private FoodDetailRepository foodDetailRepository;

    private double calorie;

    /**
     * 아침 식단 구성
     * @param oneDayTakeCalorieAmount
     * @return
     */
    public DietTypeForm constructBreakfastTimeDiet(double oneDayTakeCalorieAmount) {
        //아침에 할당된 칼로리 비율 3
        calorie = oneDayTakeCalorieAmount/10.0*3.0;

        DietTypeForm dietTypeForm = new DietTypeForm();
        dietTypeForm.setType("아침");
        List<DietFoodForm> foodFormList = new ArrayList<>();
        //음식정보 전체 조회
        List<FoodDetail> foodListAll = foodDetailRepository.findAll();

        //유제품 선택
        boolean flag = dietConditionFlag(foodListAll, FoodType.유제품, foodFormList);
        //견과류 선택
        if(flag) flag = dietConditionFlag(foodListAll, FoodType.견과류, foodFormList);
        //단백질 선택
        if(flag) flag = dietConditionFlag(foodListAll, FoodType.단백질, foodFormList);
        //채소 선택
        if(flag) flag = dietConditionFlag(foodListAll, FoodType.채소, foodFormList);
        //탄수화물 선택
        if(flag) flag = dietConditionFlag(foodListAll, FoodType.탄수화물, foodFormList);

        if(foodFormList.isEmpty()) return dietTypeForm;

        //선택된 음식들 수량 증가
        while(true) {
            int cnt = 0;
            for(DietFoodForm food : foodFormList) {
                cnt += foodAddQuantity(food);
            }
            if(cnt == 0) break;
        }
        dietTypeForm.setFoodList(foodFormList);

        return dietTypeForm;
    }

    /**
     * 점심 식단 구성
     * @param oneDayTakeCalorieAmount
     * @return
     */
    public DietTypeForm constructLaunchTimeDiet(double oneDayTakeCalorieAmount) {
        //점심에 할당된 칼로리 비율 5
        calorie = oneDayTakeCalorieAmount/10.0*5.0;

        DietTypeForm dietTypeForm = new DietTypeForm();
        dietTypeForm.setType("점심");
        List<DietFoodForm> foodFormList = new ArrayList<>();
        //음식정보 전체 조회
        List<FoodDetail> foodListAll = foodDetailRepository.findAll();

        //밥 선택
        boolean flag = dietConditionFlag(foodListAll, FoodType.밥, foodFormList);
        //닭가슴살 선택
        if(flag) flag = dietConditionFlag(foodListAll, FoodType.닭가슴살, foodFormList);
        //단백질 선택
        if(flag) flag = dietConditionFlag(foodListAll, FoodType.단백질, foodFormList);
        //채소 선택
        if(flag) flag = dietConditionFlag(foodListAll, FoodType.채소, foodFormList);

        if(foodFormList.isEmpty()) return dietTypeForm;

        //선택된 음식들 수량 증가
        while(true) {
            int cnt = 0;
            for(DietFoodForm food : foodFormList) {
                cnt += foodAddQuantity(food);
            }
            if(cnt == 0) break;
        }
        dietTypeForm.setFoodList(foodFormList);

        return dietTypeForm;
    }

    /**
     * 저녁 식단 구성
     * @param oneDayTakeCalorieAmount
     * @return
     */
    public DietTypeForm constructDinnerTimeDiet(double oneDayTakeCalorieAmount) {
        //저녁에 할당된 칼로리 비율 2
        calorie = oneDayTakeCalorieAmount/10.0*2.0;

        DietTypeForm dietTypeForm = new DietTypeForm();
        dietTypeForm.setType("저녁");
        List<DietFoodForm> foodFormList = new ArrayList<>();
        //음식정보 전체 조회
        List<FoodDetail> foodListAll = foodDetailRepository.findAll();

        //과일 선택
        boolean flag = dietConditionFlag(foodListAll, FoodType.과일, foodFormList);
        //단백질 선택
        if(flag) flag = dietConditionFlag(foodListAll, FoodType.단백질, foodFormList);
        //채소 선택
        if(flag) flag = dietConditionFlag(foodListAll, FoodType.채소, foodFormList);

        if(foodFormList.isEmpty()) return dietTypeForm;

        //선택된 음식들 수량 증가
        while(true) {
            int cnt = 0;
            for(DietFoodForm food : foodFormList) {
                cnt += foodAddQuantity(food);
            }
            if(cnt == 0) break;
        }
        dietTypeForm.setFoodList(foodFormList);

        return dietTypeForm;
    }

    /**
     * 선택 된 음식 수량 증가
     * @param food
     * @return
     */
    private int foodAddQuantity(DietFoodForm food) {
        if(calorie - food.getCalorie() >= 0) {
            calorie -= food.getCalorie();
            int quantity = food.getQuantity();
            food.setQuantity(++quantity);
            return 1;
        }
        return 0;
    }

    /**
     * 식단유형별 음식 중 랜덤하게 선택 후 잔여칼로리와 비교 후 식단구성에 포함 및 여부 판단
     * @param foodListAll
     * @param foodType
     * @param foodFormList
     * @return
     */
    private boolean dietConditionFlag(List<FoodDetail> foodListAll, FoodType foodType, List<DietFoodForm> foodFormList) {
        List<FoodDetail> shuffleList = new ArrayList<>();
        for(FoodDetail food : foodListAll) {
            if(foodType.name().equals(food.getFood_type())) {
                shuffleList.add(food);
            }
        }
        Collections.shuffle(shuffleList);
        FoodDetail food = null;
        for(FoodDetail shuffle : shuffleList) {
            if(calorie - shuffle.getCalorieAmount() >= 0) {
                food = shuffle;
                break;
            }
        }

        if(food != null) {
            DietFoodForm foodForm = new DietFoodForm();
            foodForm.setId(food.getFood_id());
            foodForm.setName(food.getFood_name());
            foodForm.setQuantity(1);
            foodForm.setWeight(food.getOneTimeAmount());
            foodForm.setCalorie(food.getCalorieAmount());
            foodForm.setProtein(food.getProteinAmount());
            foodForm.setFat(food.getFatAmount());
            foodForm.setCarbohydrate(food.getCarbohydrateAmount());
            foodFormList.add(foodForm);
            calorie -= food.getCalorieAmount();
            return true;
        }
        return false;
    }

    /**
     * 남자,여자별 하루에너지필요량 반환
     * @param DietRequestForm
     * @return
     */
    public Double calcOneDayCalorieAmount(DietRequestForm DietRequestForm) {
        if(Gender.MAN.equals(DietRequestForm.getGender())) {
            return calcOneDayCalorieAmountByMan(DietRequestForm);
        }
        if(Gender.WOMAN.equals(DietRequestForm.getGender())) {
            return calcOneDayCalorieAmountByWoMan(DietRequestForm);
        }
        return 0.0;
    }

    /**
     * 남자 하루 에너지필요량 계산
     * @param DietRequestForm
     * @return 662-(9.53*나이)+PA*((15.91*체중)+(539.6*키/100))
     */
    private Double calcOneDayCalorieAmountByMan(DietRequestForm DietRequestForm) {
        double PA = 0.0;
        switch (DietRequestForm.getActiveMetabolism()){
            case ACTIVE_LV0: PA = 1.0; break;
            case ACTIVE_LV1: PA = 1.11; break;
            case ACTIVE_LV2: PA = 1.25; break;
            case ACTIVE_LV3: PA = 1.48; break;
        }
        double a = 662.0-(9.53* DietRequestForm.getAge());
        double b = 15.91* DietRequestForm.getWeight();
        double c = 539.6* DietRequestForm.getHeight();
        return a+PA*(b+c);
    }

    /**
     * 여자 하루 에너지필요량 계산
     * @param DietRequestForm
     * @return 354-(6.91*나이)+PA*((9.36*체중)+(726*키/100))
     */
    private Double calcOneDayCalorieAmountByWoMan(DietRequestForm DietRequestForm) {
        double PA = 0.0;
        switch (DietRequestForm.getActiveMetabolism()){
            case ACTIVE_LV0: PA = 1.0; break;
            case ACTIVE_LV1: PA = 1.12; break;
            case ACTIVE_LV2: PA = 1.27; break;
            case ACTIVE_LV3: PA = 1.45; break;
        }
        double a = 354.0-(6.91* DietRequestForm.getAge());
        double b = 9.36* DietRequestForm.getWeight();
        double c = 726.0* DietRequestForm.getHeight();
        return a+PA*(b+c);
    }

}
