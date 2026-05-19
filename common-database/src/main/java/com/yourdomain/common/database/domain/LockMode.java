package com.yourdomain.common.database.domain;

/**
 * Chế độ khoá khi đọc/cập nhật dữ liệu.
 */
public enum LockMode {
    NONE,
    PESSIMISTIC_WRITE,
    PESSIMISTIC_READ
}
