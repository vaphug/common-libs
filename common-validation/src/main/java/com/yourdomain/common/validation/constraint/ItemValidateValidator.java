package com.yourdomain.common.validation.constraint;

import com.yourdomain.common.validation.annotation.ItemValidate;
import com.yourdomain.common.validation.config.DomainValidationDefinition;
import com.yourdomain.common.validation.config.DomainValidationRepository;
import com.yourdomain.common.validation.validator.DomainValidator;
import com.yourdomain.common.validation.validator.ValidationType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.math.BigDecimal;

public class ItemValidateValidator implements ConstraintValidator<ItemValidate, Object> {
    private static final DomainValidationRepository REPOSITORY = DomainValidationRepository.load();

    private String itemFromAnnotation;

    @Override
    public void initialize(ItemValidate constraintAnnotation) {
        this.itemFromAnnotation = constraintAnnotation.item();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String itemKey = resolveItemKey(context);
        if (itemKey == null || itemKey.isBlank()) {
            return true;
        }

        DomainValidationDefinition definition = REPOSITORY.find(itemKey);
        if (definition == null) {
            return true;
        }

        if (!definition.getMaxLengthConstraint().isValid(value)) {
            return buildViolation(context, resolveMessageId(definition));
        }

        ValidationType validationType = definition.getKind().validationType();
        boolean valid = DomainValidator.validate(value, validationType, definition.getParams());
        if (!valid) {
            return buildViolation(context, resolveMessageId(definition));
        }

        if (definition.getParams().containsKey("allowed") && isMoneyKind(validationType)) {
            boolean denominationValid = DomainValidator.isAllowedMoneyDenomination(
                    new BigDecimal(value.toString()),
                    definition.getParams()
            );
            if (!denominationValid) {
                return buildViolation(context, "validation.currency.denomination");
            }
        }

        return true;
    }

    private boolean buildViolation(ConstraintValidatorContext context, String messageId) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{" + messageId + "}")
                .addConstraintViolation();
        return false;
    }

    private String resolveItemKey(ConstraintValidatorContext context) {
        if (itemFromAnnotation != null && !itemFromAnnotation.isBlank()) {
            return itemFromAnnotation;
        }

        try {
            Method method = context.getClass().getMethod("getPropertyPath");
            Object path = method.invoke(context);
            if (path != null) {
                return path.toString();
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    private String resolveMessageId(DomainValidationDefinition definition) {
        if (definition.getMessageId() != null && !definition.getMessageId().isBlank()) {
            return definition.getMessageId();
        }
        return "validation.item.invalid";
    }

    private boolean isMoneyKind(ValidationType type) {
        return type == ValidationType.AMOUNT
                || type == ValidationType.AMOUNT_WITH_MINUS
                || type == ValidationType.AMOUNT_MINUS_ONLY
                || type == ValidationType.AMOUNT_GT_ZERO;
    }
}
