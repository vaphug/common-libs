package com.yourdomain.common.validation.constraint;

import com.yourdomain.common.validation.annotation.FullWidth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.nio.charset.StandardCharsets;

public class FullWidthValidator implements ConstraintValidator<FullWidth, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        for (char ch : value.toCharArray()) {
            if (String.valueOf(ch).getBytes(StandardCharsets.UTF_8).length < 2) {
                return false;
            }
        }
        return true;
    }
}
