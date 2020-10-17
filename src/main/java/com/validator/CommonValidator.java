package com.validator;

import com.model.response.CommonResponse;
import org.springframework.stereotype.Component;

@Component
public class CommonValidator {

    public CommonResponse allowedRangeValidate(Integer age,
                                               Double height,
                                               Double weight,
                                               Double physiqueWeight,
                                               Double bodyFatWeight) {
        if(age != null && (age < 17 || age > 80))
            return new CommonResponse("age", "17세부터 80세까지 입력 가능합니다.");

        if(height != null && (height.compareTo(1.0) < 0 || height.compareTo(2.2) > 0))
            return new CommonResponse("height", "100cm부터 220cm까지 입력 가능합니다.");

        if(weight != null && (weight.compareTo(30.0) < 0 || weight.compareTo(200.0) > 0))
            return new CommonResponse("weight", "30kg부터 200kg까지 입력 가능합니다.");

        if(physiqueWeight != null && physiqueWeight.compareTo(150.0) > 0)
            return new CommonResponse("physiqueWeight", "150kg까지 입력 가능합니다.");

        if(bodyFatWeight != null && bodyFatWeight.compareTo(100.0) > 0)
            return new CommonResponse("bodyFatWeight", "100kg까지 입력 가능합니다.");

        return null;
    }

}
