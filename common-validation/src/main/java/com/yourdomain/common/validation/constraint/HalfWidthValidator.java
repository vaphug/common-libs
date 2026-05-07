package com.yourdomain.common.validation.constraint;

import com.yourdomain.common.validation.annotation.HalfWidth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class HalfWidthValidator implements ConstraintValidator<HalfWidth, String> {
    private static final Pattern HALF_WIDTH_PATTERN = Pattern.compile("^[\\x20-\\x7E]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return HALF_WIDTH_PATTERN.matcher(value).matches();
    }
}
