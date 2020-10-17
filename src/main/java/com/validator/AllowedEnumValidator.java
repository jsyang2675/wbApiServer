package com.validator;

import com.google.common.base.Enums;
import org.thymeleaf.util.ArrayUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AllowedValueValidator implements ConstraintValidator<AllowedValue, String> {

    private Enums values;

    @Override
    public void initialize(AllowedValue constraintAnnotation) {
        //어노테이션 통해 전달 받은 value값 set
        this.values = constraintAnnotation.values();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return ArrayUtils.contains(values, value);
    }
}
