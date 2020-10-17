package com.model.diet;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class DietResponseForm {
    private LocalDate date;
    private List<DietTypeForm> oneDayDietList;
}
