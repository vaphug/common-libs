package com.yourdomain.common.validation.ngword;

public record NgWordCheckResult(
        boolean blocked,
        String normalizedInput,
        String matchedWord,
        String normalizedMatchedWord
) {
}
