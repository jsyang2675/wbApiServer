package com.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DietResponse {
    private LocalDate date;
    private List<DietTypeDto> oneDayDietList;
}
