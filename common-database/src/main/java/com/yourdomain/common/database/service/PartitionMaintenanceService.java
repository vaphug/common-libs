package com.yourdomain.common.database.service;

import com.yourdomain.common.database.context.TableMetadata;
import com.yourdomain.common.database.util.SqlIdentifierValidator;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Service quản trị partition phục vụ bảng dữ liệu lớn.
 *
 * <p>Thiết kế theo chiến lược phân vùng theo khoảng (range) trên id,
 * mỗi partition tối đa 4 tỷ dòng.
 */
public class PartitionMaintenanceService {

    private static final Logger log = LoggerFactory.getLogger(PartitionMaintenanceService.class);

    private static final long PARTITION_SPAN = 4_000_000_000L;

    private final JdbcTemplate jdbcTemplate;

    public PartitionMaintenanceService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Tạo partition mới nếu chưa tồn tại.
     *
     * @param metadata metadata bảng
     * @param partitionIndex chỉ số partition (0-based)
     */
    public void ensureRangePartition(TableMetadata metadata, long partitionIndex) {
        String baseTable = SqlIdentifierValidator.safeIdentifier(metadata.tableName());
        String partitionTable = baseTable + "_p" + partitionIndex;
        long fromInclusive = partitionIndex * PARTITION_SPAN;
        long toExclusive = (partitionIndex + 1) * PARTITION_SPAN;

        String ddl = "CREATE TABLE IF NOT EXISTS " + partitionTable
                + " PARTITION OF " + baseTable
                + " FOR VALUES FROM (" + fromInclusive + ") TO (" + toExclusive + ")";
        jdbcTemplate.execute(ddl);
        log.info("Ensured partition table={} range=[{}, {})", partitionTable, fromInclusive, toExclusive);
    }
}
