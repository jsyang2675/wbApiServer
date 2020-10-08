package com.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="food_detail")
@Getter @Setter
public class FoodDetail {
    @Id
    private String food_id;
    private String food_type;
    private String food_name;

    @Column(name="one_time_amount")
    private Double oneTimeAmount;

    @Column(name="calorie_amount")
    private Double calorieAmount;

    @Column(name="protein_amount")
    private Double proteinAmount;

    @Column(name="fat_amount")
    private Double fatAmount;

    @Column(name="carbohydrate_amount")
    private Double carbohydrateAmount;

}
