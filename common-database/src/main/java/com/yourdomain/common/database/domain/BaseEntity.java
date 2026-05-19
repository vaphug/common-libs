package com.yourdomain.common.database.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Base entity dùng chung cho các service khi tích hợp common-database.
 *
 * <p>Entity nghiệp vụ (ví dụ Order, Product, InventoryItem) có thể kế thừa lớp này
 * để tái sử dụng nhóm cột audit chuẩn trên mọi bảng chính.
 */
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * Thời điểm tạo bản ghi.
     */
    private LocalDateTime createdAt;

    /**
     * Người tạo bản ghi.
     */
    private String createdUser;

    /**
     * Thời điểm cập nhật gần nhất; cũng có thể dùng làm optimistic lock bằng cơ chế compare-and-set.
     */
    private LocalDateTime modifiedAt;

    /**
     * Người cập nhật gần nhất.
     */
    private String modifiedUser;

    /**
     * Cờ xoá mềm.
     */
    private Boolean isDeleted;
}
