package com.yourdomain.common.validation.constraint;

import com.yourdomain.common.validation.annotation.Min;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class MinValidator implements ConstraintValidator<Min, Object> {
    private BigDecimal min;

    @Override
    public void initialize(Min constraintAnnotation) {
        this.min = new BigDecimal(constraintAnnotation.value());
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
        return number.compareTo(min) >= 0;
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
