package com.model.inbody;

import com.enums.Gender;
import com.validator.AllowedEnum;
import lombok.Getter;


import javax.validation.constraints.*;

@Getter
public class InbodyRequestForm {
    @NotNull
    @AllowedEnum(enumClass = Gender.class)
    private String gender;

    @Positive
    private double weight;

    @Positive
    private double height;

    @PositiveOrZero
    private double bodyFatWeight;

    @PositiveOrZero
    private double physiqueWeight;

    //단위환산 (cm -> m)
    public double getHeight() {
        return height/100.0;
    }
}
