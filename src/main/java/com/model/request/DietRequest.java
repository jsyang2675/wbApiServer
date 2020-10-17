package com.model.request;

import com.enums.diet.ActiveMetabolism;
import com.enums.diet.DietType;
import com.enums.Gender;
import com.validator.AllowedEnum;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DietRequest {
    @NotNull
    @AllowedEnum(enumClass = DietType.class)
    private String dietType;

    @NotNull
    @AllowedEnum(enumClass = Gender.class)
    private String gender;

    @Positive
    private int age;

    @Positive
    private double height;

    @Positive
    private double weight;

    @PositiveOrZero
    private double physiqueWeight;

    @PositiveOrZero
    private double bodyFatWeight;

    @NotNull
    @AllowedEnum(enumClass = ActiveMetabolism.class)
    private String activeMetabolism;

    //단위환산 (cm -> m)
    public double getHeight() {
        return height/100.0;
    }
}
