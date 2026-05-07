package com.yourdomain.common.validation.annotation;

import com.yourdomain.common.validation.constraint.FullWidthValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FullWidthValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FullWidth {
    String message() default "{validation.text.fullwidth}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
