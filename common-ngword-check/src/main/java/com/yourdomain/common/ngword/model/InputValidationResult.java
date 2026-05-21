package com.yourdomain.common.ngword.model;

import java.util.List;

/**
 * Kết quả validate input trước bước normalize/check NG.
 *
 * @param valid true nếu input hợp lệ theo toàn bộ rule scope
 * @param issues danh sách lỗi validate chi tiết
 */
public record InputValidationResult(
        boolean valid,
        List<InputValidationIssue> issues
) {
}
