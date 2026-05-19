package com.yourdomain.common.database.config;

import com.yourdomain.common.database.context.TableMetadata;
import com.yourdomain.common.database.mapper.CommonEntityMapper;
import com.yourdomain.common.database.repository.CommonCrudRepository;
import com.yourdomain.common.database.repository.MyBatisCommonCrudRepository;
import com.yourdomain.common.database.service.CommonDatabaseService;
import com.yourdomain.common.database.service.DataRetentionService;
import com.yourdomain.common.database.service.PartitionMaintenanceService;
import java.time.Clock;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Bean configuration cho lớp service/repository của common-database.
 */
@AutoConfiguration
public class CommonDatabaseBeansConfiguration {

    /**
     * Metadata mặc định; service có thể override bean này cho từng bảng cụ thể.
     */
    @Bean
    public TableMetadata defaultTableMetadata() {
        return new TableMetadata(
                "entity_records",
                "id",
                "modified_at",
                "is_deleted",
                "deleted_at",
                "created_at",
                "created_user",
                "modified_user"
        );
    }

    @Bean
    public CommonCrudRepository commonCrudRepository(CommonEntityMapper mapper, TableMetadata metadata, Clock clock) {
        return new MyBatisCommonCrudRepository(mapper, metadata, clock);
    }

    @Bean
    public Clock systemUtcClock() {
        return Clock.systemUTC();
    }

    @Bean
    public CommonDatabaseService commonDatabaseService(CommonCrudRepository repository) {
        return new CommonDatabaseService(repository);
    }

    @Bean
    public PartitionMaintenanceService partitionMaintenanceService(DataSource dataSource) {
        return new PartitionMaintenanceService(dataSource);
    }

    @Bean
    public DataRetentionService dataRetentionService(CommonDatabaseService service, DatabaseProperties properties) {
        return new DataRetentionService(service, properties);
    }
}
