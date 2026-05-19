package com.yourdomain.common.validation.constraint;

import com.yourdomain.common.validation.annotation.Max;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class MaxValidator implements ConstraintValidator<Max, Object> {
    private BigDecimal max;

    @Override
    public void initialize(Max constraintAnnotation) {
        this.max = new BigDecimal(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        BigDecimal number = toBigDecimal(value);
        if (number == null) {
            return false;
        }
        return number.compareTo(max) <= 0;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return new BigDecimal(number.toString());
        }
        try {
            return new BigDecimal(value.toString().trim());
        } catch (RuntimeException ex) {
            return null;
        }
    }
}
