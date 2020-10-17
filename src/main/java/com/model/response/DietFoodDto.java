package com.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DietFood {
    private String id;
    private String name;
    private Integer quantity;
    private Double weight;
    private Double calorie;
    private Double protein;
    private Double fat;
    private Double carbohydrate;

    public void addQuantity() {
        this.quantity = quantity++;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
