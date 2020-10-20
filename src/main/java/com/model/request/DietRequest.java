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
    private DietType dietType;

    @NotNull
    private Gender gender;

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
    private ActiveMetabolism activeMetabolism;

    //단위환산 (cm -> m)
    public double getHeight() {
        return height/100.0;
    }

}
