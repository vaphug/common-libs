package com.yourdomain.common.database.service;

import com.yourdomain.common.database.config.DatabaseProperties;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service dọn dữ liệu recycle-bin theo retention policy (mặc định 2 năm).
 */
public class DataRetentionService {

    private static final Logger log = LoggerFactory.getLogger(DataRetentionService.class);

    private final CommonDatabaseService databaseService;
    private final DatabaseProperties properties;

    public DataRetentionService(CommonDatabaseService databaseService, DatabaseProperties properties) {
        this.databaseService = databaseService;
        this.properties = properties;
    }

    /**
     * Thực thi cleanup dữ liệu cũ theo cấu hình retention năm.
     */
    public int purgeExpiredRecycleBinData() {
        LocalDateTime cutoff = LocalDateTime.now().minusYears(properties.getCleanupRetentionYears());
        int deletedRows = databaseService.cleanupBefore(cutoff);
        log.info("Purged recycle-bin data older than {} years. deletedRows={}",
                properties.getCleanupRetentionYears(), deletedRows);
        return deletedRows;
    }
}
