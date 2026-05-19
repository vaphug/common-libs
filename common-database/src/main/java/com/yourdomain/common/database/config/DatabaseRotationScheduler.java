package com.yourdomain.common.database.config;

public class DatabaseRotationScheduler {

    private final RotatingDataSource rotatingDataSource;

    public DatabaseRotationScheduler(RotatingDataSource rotatingDataSource) {
        this.rotatingDataSource = rotatingDataSource;
    }

    public void refresh() {
        rotatingDataSource.refreshIfRotated();
    }
}
