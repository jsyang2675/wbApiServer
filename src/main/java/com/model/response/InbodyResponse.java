package com.model.response;

import com.enums.inbody.InbodyBodyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InbodyResponse {
    private Double weightPercentage;
    private Double physiqueWeightPercentage;
    private Double bodyFatWeightPercentage;
    private Integer inbodyScore;
    private InbodyBodyType inbodyBodyType;
}
