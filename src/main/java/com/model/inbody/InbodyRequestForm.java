package com.model.inbody;

import com.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InbodyRequestForm {
    private Gender gender;
    private double weight;
    private double height;
    private double bodyFatWeight;
    private double physiqueWeight;

    //단위환산 (cm -> m)
    public double getHeight() {
        return height/100.0;
    }
}
