package com.model.diet;

import com.enums.diet.ActiveMetabolism;
import com.enums.diet.DietType;
import com.enums.Gender;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DietRequestForm {
    @ApiParam(value = "식단유형", required = true) private DietType dietType;
    @ApiParam(value="성별", required = true) private Gender gender;
    @ApiParam(value="나이", required = true) private int age;
    @ApiParam(value="키(cm)", required = true) private double height;
    @ApiParam(value="체중(kg)", required = true) private double weight;
    @ApiParam(value="골격근량(kg)", required = true) private double physiqueWeight;
    @ApiParam(value="체지방량(kg)", required = true) private double bodyFatWeight;
    @ApiParam(value="활동대사량<br>" +
            "ACTIVE_LV0 : 움직임이 거의 없음<br>" +
            "ACTIVE_LV1 : 산책정도의 가벼운 활동<br>" +
            "ACTIVE_LV2 : 적당한 운동이나 스포츠 활동<br>" +
            "ACTIVE_LV3 : 매우 강렬한 운동이나 스포츠 활동<br>", required = true) private ActiveMetabolism activeMetabolism;

    //단위환산 (cm -> m)
    public double getHeight() {
        return height/100.0;
    }
}
