package com.yourdomain.demo.api.database;

import com.yourdomain.common.database.context.TableMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Cấu hình metadata cho bảng orders_common_demo trong demo service.
 */
@Configuration
public class OrderDatabaseConfiguration {

    @Bean
    @Primary
    public TableMetadata orderTableMetadata() {
        return new TableMetadata(
                "orders_common_demo",
                "id",
                "modified_at",
                "is_deleted",
                "deleted_at",
                "created_at",
                "created_user",
                "modified_user"
        );
    }
}
