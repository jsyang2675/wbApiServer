package com.model.request;

import com.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InbodyRequest {
    @NotNull
    private Gender gender;

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
