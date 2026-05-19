package com.yourdomain.demo.api.database;

/**
 * Request body cho cleanup recycle-bin.
 */
public record RecycleBinCleanupRequest(String cutoff) {
}
