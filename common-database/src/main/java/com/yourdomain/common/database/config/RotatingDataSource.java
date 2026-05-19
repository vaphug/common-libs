package com.yourdomain.common.database.config;

import com.yourdomain.common.secretmanager.model.SecretSnapshot;
import com.yourdomain.common.secretmanager.service.SecretRefreshService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class RotatingDataSource implements DataSource {

    private final SecretRefreshService secretRefreshService;
    private final DatabaseSecretMapper secretMapper;
    private final AtomicReference<State> state = new AtomicReference<>();

    public RotatingDataSource(SecretRefreshService secretRefreshService, DatabaseSecretMapper secretMapper) {
        this.secretRefreshService = secretRefreshService;
        this.secretMapper = secretMapper;
        refreshFromLatestSecret();
    }

    public synchronized boolean refreshIfRotated() {
        boolean rotated = secretRefreshService.refreshIfVersionChanged();
        if (rotated) {
            return refreshFromLatestSecret();
        }
        return false;
    }

    public synchronized boolean refreshFromLatestSecret() {
        SecretSnapshot snapshot = secretRefreshService.current();
        State current = state.get();
        if (current != null && Objects.equals(current.version(), snapshot.version())) {
            return false;
        }
        DatabaseSecret secret = secretMapper.map(snapshot);
        DriverManagerDataSource delegate = new DriverManagerDataSource();
        delegate.setDriverClassName("org.postgresql.Driver");
        delegate.setUrl(secret.jdbcUrl());
        delegate.setUsername(secret.username());
        delegate.setPassword(secret.password());
        state.set(new State(snapshot.version(), delegate));
        return true;
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            return currentDelegate().getConnection();
        } catch (SQLException ex) {
            secretRefreshService.forceRefresh();
            refreshFromLatestSecret();
            return currentDelegate().getConnection();
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        try {
            return currentDelegate().getConnection(username, password);
        } catch (SQLException ex) {
            secretRefreshService.forceRefresh();
            refreshFromLatestSecret();
            return currentDelegate().getConnection(username, password);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return currentDelegate().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return currentDelegate().isWrapperFor(iface);
    }

    @Override
    public java.io.PrintWriter getLogWriter() throws SQLException {
        return currentDelegate().getLogWriter();
    }

    @Override
    public void setLogWriter(java.io.PrintWriter out) throws SQLException {
        currentDelegate().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        currentDelegate().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return currentDelegate().getLoginTimeout();
    }

    @Override
    public java.util.logging.Logger getParentLogger() {
        return java.util.logging.Logger.getGlobal();
    }

    private DataSource currentDelegate() {
        State current = state.get();
        if (current == null) {
            throw new IllegalStateException("Database datasource not initialized");
        }
        return current.dataSource();
    }

    private record State(String version, DataSource dataSource) {
    }
}
