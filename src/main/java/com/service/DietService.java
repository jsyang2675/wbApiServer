package com.service;

import com.domain.FoodDetail;
import com.enums.diet.ActiveMetabolism;
import com.enums.diet.FoodType;
import com.enums.Gender;
import com.model.response.DietFoodDto;
import com.model.response.DietTypeDto;
import com.model.request.DietRequest;
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

    /**
     * 아침 식단 구성
     * 음식구성우선순위 유제품 -> 견과류 -> 단백질 -> 채소 -> 탄수화물
     * @param oneDayTakeCalorieAmount
     * @return DietTypeDto
     */
    public DietTypeDto constructBreakfastTimeDiet(double oneDayTakeCalorieAmount) {
        //아침에 할당된 칼로리 비율 3
        double calorie = oneDayTakeCalorieAmount/10.0*3.0;

        List<DietFoodDto> foodList = new ArrayList<>();


        // TODO: calcCalorieAndPutInFood 함수 -> 잔량 calorie 계산 함수, 음식 담아서 DietFoodDto 리턴 함수 나누기


        //유제품 담기
        double remainCalorie = calcCalorieAndPutInFood(FoodType.유제품, foodList, calorie);
        //견과류 담기
        if(remainCalorie > 0) remainCalorie = calcCalorieAndPutInFood(FoodType.견과류, foodList, remainCalorie);
        //단백질 담기
        if(remainCalorie > 0) remainCalorie = calcCalorieAndPutInFood(FoodType.단백질, foodList, remainCalorie);
        //채소 담기
        if(remainCalorie > 0) remainCalorie = calcCalorieAndPutInFood(FoodType.채소, foodList, remainCalorie);
        //탄수화물 담기
        if(remainCalorie > 0) remainCalorie = calcCalorieAndPutInFood(FoodType.탄수화물, foodList, remainCalorie);

        //음식이 1개도 담기지 않았으면 빈 채로 반환
        if(foodList.isEmpty()) return new DietTypeDto("아침", null);

        //남은 칼로리가 있는 경우 담긴 음식들 수량 증가
        if(remainCalorie > 0) {
            increaseFoodQuantity(remainCalorie, foodList);
        }
        return new DietTypeDto("아침", foodList);
    }

    /**
     * 점심 식단 구성
     * 음식구성우선순위 밥 -> 닭가슴살 -> 단백질 -> 채소
     * @param oneDayTakeCalorieAmount
     * @return DietTypeDto
     */
    public DietTypeDto constructLaunchTimeDiet(double oneDayTakeCalorieAmount) {
        //점심에 할당된 칼로리 비율 5
        double calorie = oneDayTakeCalorieAmount/10.0*5.0;

        List<DietFoodDto> foodList = new ArrayList<>();

        //밥 담기
        double remainCalorie = calcCalorieAndPutInFood(FoodType.밥, foodList, calorie);
        //닭가슴살 담기
        if(remainCalorie > 0) remainCalorie = calcCalorieAndPutInFood(FoodType.닭가슴살, foodList, remainCalorie);
        //단백질 담기
        if(remainCalorie > 0) remainCalorie = calcCalorieAndPutInFood(FoodType.단백질, foodList, remainCalorie);
        //채소 담기
        if(remainCalorie > 0) remainCalorie = calcCalorieAndPutInFood(FoodType.채소, foodList, remainCalorie);

        //음식이 1개도 담기지 않았으면 빈 채로 반환
        if(foodList.isEmpty()) return new DietTypeDto("점심", null);

        //남은 칼로리가 있는 경우 담긴 음식들 수량 증가
        if(remainCalorie > 0) {
            increaseFoodQuantity(remainCalorie, foodList);
        }
        return new DietTypeDto("점심", foodList);
    }

    /**
     * 저녁 식단 구성
     * 음식구성우선순위 과일 -> 단백질 -> 채소
     * @param oneDayTakeCalorieAmount
     * @return DietTypeDto
     */
    public DietTypeDto constructDinnerTimeDiet(double oneDayTakeCalorieAmount) {
        //저녁에 할당된 칼로리 비율 2
        double calorie = oneDayTakeCalorieAmount/10.0*2.0;

        List<DietFoodDto> foodList = new ArrayList<>();

        //과일 담기
        double remainCalorie = calcCalorieAndPutInFood(FoodType.과일, foodList, calorie);
        //단백질 담기
        if(remainCalorie > 0) remainCalorie = calcCalorieAndPutInFood(FoodType.단백질, foodList, remainCalorie);
        //채소 담기
        if(remainCalorie > 0) remainCalorie = calcCalorieAndPutInFood(FoodType.채소, foodList, remainCalorie);

        //음식이 1개도 담기지 않았으면 빈 채로 반환
        if(foodList.isEmpty()) return new DietTypeDto("저녁", null);

        //남은 칼로리가 있는 경우 담긴 음식들 수량 증가
        if(remainCalorie > 0) {
            increaseFoodQuantity(remainCalorie, foodList);
        }
        return new DietTypeDto("저녁", foodList);
    }

    /**
     * 식단유형별 음식 중 랜덤하게 선택 후 잔여칼로리와 비교 후 식단구성에 포함 및 여부 판단
     * @param foodType
     * @param foodFormList
     * @return 칼로리를 초과해 더이상 담을 음식이 없을 경우 음수 반환
     */
    private double calcCalorieAndPutInFood(FoodType foodType, List<DietFoodDto> foodFormList, double calorie) {
        List<FoodDetail> foodListAll = foodDetailRepository.findAll();

        List<FoodDetail> shuffleList = new ArrayList<>();
        for(FoodDetail food : foodListAll) {
            if(foodType.name().equals(food.getFood_type())) {
                shuffleList.add(food);
            }
        }
        Collections.shuffle(shuffleList);
        FoodDetail food = null;
        for(FoodDetail shuffle : shuffleList) {
            // 남은 칼로리를 초과하지 않는 음식 1개 담기
            if(calorie - shuffle.getCalorieAmount() >= 0) {
                food = shuffle;
                break;
            }
        }

        if(food != null) {
            foodFormList.add(new DietFoodDto(food.getFood_id(),
                    food.getFood_name(),
                    1,
                    food.getOneTimeAmount(),
                    food.getCalorieAmount(),
                    food.getProteinAmount(),
                    food.getFatAmount(),
                    food.getCarbohydrateAmount()));
            calorie -= food.getCalorieAmount();
            return calorie;
        }
        else return -1;
    }

    /**
     * 구성된 음식들 남은칼로리량 비교 후 수량 증가
     * @param remainCalorie
     * @param foodFormList
     */
    private void increaseFoodQuantity(double remainCalorie, List<DietFoodDto> foodFormList) {
        while(true) {
            double compareCalorie = remainCalorie;
            for(DietFoodDto food : foodFormList) {
                if(remainCalorie < food.getCalorie()) continue;

                food.setQuantity(food.getQuantity());
                food.addQuantity();

                remainCalorie -= food.getCalorie();
            }
            //잔여칼로리가 변동이 없을 종료
            if (compareCalorie == remainCalorie) break;
        }
    }

    /**
     * 남자,여자별 하루에너지필요량 반환
     * @param DietRequest
     * @return
     */
    public Double calcOneDayCalorieAmount(DietRequest DietRequest) {
        if(Gender.MAN.name().equals(DietRequest.getGender())) {
            return calcOneDayCalorieAmountByMan(DietRequest);
        }
        if(Gender.WOMAN.name().equals(DietRequest.getGender())) {
            return calcOneDayCalorieAmountByWoMan(DietRequest);
        }
        return 0.0;
    }

    /**
     * 남자 하루 에너지필요량 계산
     * @param DietRequest
     * @return 662-(9.53*나이)+PA*((15.91*체중)+(539.6*키/100))
     */
    private Double calcOneDayCalorieAmountByMan(DietRequest DietRequest) {
        double PA = 0.0;
        if(ActiveMetabolism.ACTIVE_LV0.name().equals(DietRequest.getActiveMetabolism())) PA = 1.0;
        else if(ActiveMetabolism.ACTIVE_LV1.name().equals(DietRequest.getActiveMetabolism())) PA = 1.11;
        else if(ActiveMetabolism.ACTIVE_LV2.name().equals(DietRequest.getActiveMetabolism())) PA = 1.25;
        else if(ActiveMetabolism.ACTIVE_LV3.name().equals(DietRequest.getActiveMetabolism())) PA = 1.48;

        double a = 662.0-(9.53* DietRequest.getAge());
        double b = 15.91* DietRequest.getWeight();
        double c = 539.6* DietRequest.getHeight();
        return a+PA*(b+c);
    }

    /**
     * 여자 하루 에너지필요량 계산
     * @param DietRequest
     * @return 354-(6.91*나이)+PA*((9.36*체중)+(726*키/100))
     */
    private Double calcOneDayCalorieAmountByWoMan(DietRequest DietRequest) {
        double PA = 0.0;
        if(ActiveMetabolism.ACTIVE_LV0.name().equals(DietRequest.getActiveMetabolism())) PA = 1.0;
        else if(ActiveMetabolism.ACTIVE_LV1.name().equals(DietRequest.getActiveMetabolism())) PA = 1.12;
        else if(ActiveMetabolism.ACTIVE_LV2.name().equals(DietRequest.getActiveMetabolism())) PA = 1.27;
        else if(ActiveMetabolism.ACTIVE_LV3.name().equals(DietRequest.getActiveMetabolism())) PA = 1.45;

        double a = 354.0-(6.91* DietRequest.getAge());
        double b = 9.36* DietRequest.getWeight();
        double c = 726.0* DietRequest.getHeight();
        return a+PA*(b+c);
    }

    /**
     * 하루 에너지섭취량 조건
     * 구간 1. 에너지필요량 1000 이하 : 뺄 살이 없음으로 판단
     * 구간 2. 에너지필요량 1000 초과 1500 이하 :
     * @param calcOneDayCalorieAmount
     * @return
     */
    public Double oneDayTakeCalorieAmountCondition(double calcOneDayCalorieAmount) {


        return 0.0;
    }

}
