package com.model.diet;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DietFoodForm {
    private String id;
    private String name;
    private Integer quantity;
    private Double weight;
    private Double calorie;
    private Double protein;
    private Double fat;
    private Double carbohydrate;
}
