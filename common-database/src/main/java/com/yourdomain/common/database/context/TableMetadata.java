package com.yourdomain.common.database.context;

/**
 * Metadata mô tả bảng dữ liệu nghiệp vụ để repository generic hoạt động dynamic.
 *
 * @param tableName tên bảng
 * @param idColumn tên cột khoá chính
 * @param modifiedAtColumn tên cột thời điểm cập nhật gần nhất (phục vụ optimistic lock theo thời gian)
 * @param deletedColumn tên cột soft-delete flag
 * @param deletedAtColumn tên cột thời điểm bị xoá mềm
 * @param createdAtColumn tên cột thời điểm tạo
 * @param createdUserColumn tên cột người tạo
 * @param modifiedUserColumn tên cột người cập nhật gần nhất
 */
public record TableMetadata(
        String tableName,
        String idColumn,
        String modifiedAtColumn,
        String deletedColumn,
        String deletedAtColumn,
        String createdAtColumn,
        String createdUserColumn,
        String modifiedUserColumn
) {
}
