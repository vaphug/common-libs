package com.yourdomain.common.ngword.model;

/**
 * Kết quả tổng hợp của pipeline check NG gồm validate và so khớp.
 *
 * @param status trạng thái nghiệp vụ cuối cùng
 * @param validation kết quả validate input
 * @param checkResult kết quả so khớp NG token
 */
public record NgWordCheckOutcome(
        CheckStatus status,
        InputValidationResult validation,
        NgWordCheckResult checkResult
) {
}
