package com.yourdomain.common.validation.validator;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Locale;

public enum ValidationKind {
    STRING_ALLOW_FULL_HALF_WIDTH(ValidationType.ANY_TEXT),
    STRING_HALF_WIDTH_ONLY(ValidationType.HALF_WIDTH),
    STRING_FULL_WIDTH_ONLY(ValidationType.FULL_WIDTH),
    STRING_FULL_AND_HALF_WIDTH(ValidationType.FULL_AND_HALF_WIDTH),
    STRING_DIGITS_ONLY(ValidationType.DIGITS_ONLY),
    NUMERIC(ValidationType.NUMBER),
    NUMERIC_ALLOW_MINUS(ValidationType.NUMBER_WITH_MINUS),
    NUMERIC_POSITIVE(ValidationType.NUMBER_GT_ZERO),
    AMOUNT(ValidationType.AMOUNT),
    AMOUNT_ALLOW_MINUS(ValidationType.AMOUNT_WITH_MINUS),
    AMOUNT_MINUS_ONLY(ValidationType.AMOUNT_MINUS_ONLY),
    AMOUNT_POSITIVE(ValidationType.AMOUNT_GT_ZERO),
    DATE_YYYY(ValidationType.DATE_YYYY),
    DATE_YYYYMM(ValidationType.DATE_YYYYMM),
    DATE_YYYYMMDD(ValidationType.DATE_YYYYMMDD),
    DATETIME(ValidationType.DATETIME_YYYYMMDDHHMM),
    CATEGORY(ValidationType.ENUM_IN_SET);

    private final ValidationType validationType;

    ValidationKind(ValidationType validationType) {
        this.validationType = validationType;
    }

    public ValidationType validationType() {
        return validationType;
    }

    @JsonCreator
    public static ValidationKind fromJson(String raw) {
        if (raw == null || raw.isBlank()) {
            return STRING_ALLOW_FULL_HALF_WIDTH;
        }
        String normalized = normalizeAlias(raw.trim())
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toUpperCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(kind -> kind.name().equals(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported validation kind: " + raw));
    }

    private static String normalizeAlias(String raw) {
        return switch (raw) {
            case "numericGtZero" -> "numericPositive";
            case "money" -> "amount";
            case "moneyAllowMinus" -> "amountAllowMinus";
            case "moneyMinusOnly" -> "amountMinusOnly";
            case "moneyGtZero" -> "amountPositive";
            case "datetimeYYYYMMDDHHmm" -> "datetime";
            case "enumSet" -> "category";
            default -> raw;
        };
    }
}
