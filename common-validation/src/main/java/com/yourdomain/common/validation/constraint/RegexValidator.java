package com.yourdomain.common.validation.constraint;

import com.yourdomain.common.validation.annotation.Regex;
import com.yourdomain.common.validation.validator.RegexMatcher;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegexValidator implements ConstraintValidator<Regex, String> {
    private String pattern;

    @Override
    public void initialize(Regex constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return RegexMatcher.isValid(value, pattern);
    }
}
