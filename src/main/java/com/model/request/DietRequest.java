package com.model.diet;

import com.enums.diet.ActiveMetabolism;
import com.enums.diet.DietType;
import com.enums.Gender;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DietRequestForm {
    @NotNull
    private DietType dietType;
    @NotNull
    private Gender gender;
    @NotNull
    private int age;
    @NotNull
    private double height;
    @NotNull
    private double weight;
    @NotNull
    private double physiqueWeight;
    @NotNull
    private double bodyFatWeight;
    @NotNull
    private ActiveMetabolism activeMetabolism;

    //단위환산 (cm -> m)
    public double getHeight() {
        return height/100.0;
    }
}
