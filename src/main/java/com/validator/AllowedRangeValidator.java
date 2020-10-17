package com.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AllowedRangeValidator implements ConstraintValidator<AllowedRange, String> {

    private double min;
    private double max;

    @Override
    public void initialize(AllowedRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Double requestValue = Double.parseDouble(value);
        if(requestValue.compareTo(min) < 0) return false;
        if(requestValue.compareTo(max) > 0) return false;
        return true;
    }
}
