package com.yourdomain.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation cho annotation {@link NotBlank}.
 */
public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Trả false để framework sinh violation dựa trên message key ở annotation.
        return value != null && !value.trim().isEmpty();
    }
}
