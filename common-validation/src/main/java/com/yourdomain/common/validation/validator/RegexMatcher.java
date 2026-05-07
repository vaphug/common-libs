package com.yourdomain.common.validation.validator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Utility regex hiệu năng cao: compile một lần, dùng lại nhiều lần.
 */
public final class RegexMatcher {
    private static final ConcurrentHashMap<String, Pattern> CACHE = new ConcurrentHashMap<>();

    private RegexMatcher() {
    }

    public static boolean isValid(String value, String regex) {
        if (value == null) {
            return true;
        }
        if (regex == null || regex.isBlank()) {
            return false;
        }
        try {
            Pattern pattern = CACHE.computeIfAbsent(regex, Pattern::compile);
            return pattern.matcher(value).matches();
        } catch (PatternSyntaxException ex) {
            return false;
        }
    }
}
