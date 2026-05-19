package com.yourdomain.common.database.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cấu hình kết nối và hành vi cho thư viện common-database.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "common.database")
public class DatabaseProperties {

    /** Host database mặc định dùng để tạo JDBC URL. */
    private String host = "localhost";
    /** Port database mặc định. */
    private int port = 5432;
    /** Tên database đích. */
    private String name = "inventory_db";
    /** Schema mặc định dùng cho query và migration logic của module. */
    private String schema = "public";
    /** Username mặc định khi khởi tạo datasource. */
    private String username = "postgres";
    /** Password mặc định khi khởi tạo datasource. */
    private String password = "postgres";
    /** Số năm retention cho dữ liệu recycle bin hoặc soft delete cleanup. */
    private int cleanupRetentionYears = 2;
    /** Bật hoặc tắt cơ chế rotate datasource credential/runtime secret. */
    private boolean rotateEnabled = true;
    /** Chu kỳ quét secret hoặc datasource metadata để phát hiện rotation. */
    private Duration rotateScanInterval = Duration.ofSeconds(20);

    /**
     * Tạo JDBC URL PostgreSQL từ bộ host/port/database name hiện tại.
     *
     * @return JDBC URL hoàn chỉnh để datasource có thể kết nối đến PostgreSQL
     */
    public String jdbcUrl() {
        return "jdbc:postgresql://" + host + ":" + port + "/" + name;
    }
}
