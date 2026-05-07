package com.yourdomain.common.validation.validator;

import java.util.regex.Pattern;

public final class MaxLengthConstraint {
    private static final Pattern NUMBER_FORMAT = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    private final Integer maxLength;
    private final Integer integerLength;
    private final Integer decimalLength;

    private MaxLengthConstraint(Integer maxLength, Integer integerLength, Integer decimalLength) {
        this.maxLength = maxLength;
        this.integerLength = integerLength;
        this.decimalLength = decimalLength;
    }

    public static MaxLengthConstraint parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return new MaxLengthConstraint(null, null, null);
        }
        if (raw.contains(",")) {
            String[] parts = raw.split(",");
            return new MaxLengthConstraint(null, Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
        }
        return new MaxLengthConstraint(Integer.parseInt(raw.trim()), null, null);
    }

    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        String text = value.toString();
        if (maxLength != null) {
            return text.length() <= maxLength;
        }
        if (integerLength != null && decimalLength != null) {
            if (!NUMBER_FORMAT.matcher(text).matches()) {
                return false;
            }
            String[] numberParts = text.replaceFirst("^-", "").split("\\.");
            int intPartLen = numberParts[0].length();
            int decPartLen = numberParts.length > 1 ? numberParts[1].length() : 0;
            return intPartLen <= integerLength && decPartLen <= decimalLength;
        }
        return true;
    }
}
