package com.model.diet;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DietTypeForm {
    private String type;
    private List<DietFoodForm> foodList;
}
