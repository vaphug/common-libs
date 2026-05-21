package com.yourdomain.common.ngword.model;

/**
 * Kết quả so khớp NG word sau khi normalize.
 *
 * @param ng true nếu input chứa NG word
 * @param normalizedInput input sau chuẩn hóa
 * @param matchedRawNgWord NG token gốc đã khớp
 * @param matchedNormalizedNgWord NG token sau chuẩn hóa đã khớp
 */
public record NgWordCheckResult(
        boolean ng,
        String normalizedInput,
        String matchedRawNgWord,
        String matchedNormalizedNgWord
) {
}
