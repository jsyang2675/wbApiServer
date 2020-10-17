package com.model.inbody;

import com.enums.inbody.InbodyBodyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class InbodyResponseForm {
    private Double weightPercentage;
    private Double physiqueWeightPercentage;
    private Double bodyFatWeightPercentage;
    private Integer inbodyScore;
    private InbodyBodyType inbodyBodyType;

    public InbodyResponseForm(Double weightPercentage, Double physiqueWeightPercentage,
                              Double bodyFatWeightPercentage, Integer inbodyScore, InbodyBodyType inbodyBodyType) {
        this.weightPercentage = weightPercentage;
        this.physiqueWeightPercentage = physiqueWeightPercentage;
        this.bodyFatWeightPercentage = bodyFatWeightPercentage;
        this.inbodyScore = inbodyScore;
        this.inbodyBodyType = inbodyBodyType;
    }
}
