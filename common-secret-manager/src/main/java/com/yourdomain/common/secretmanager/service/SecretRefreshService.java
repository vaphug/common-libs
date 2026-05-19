package com.yourdomain.common.secretmanager.service;

import com.yourdomain.common.secretmanager.model.SecretSnapshot;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class SecretRefreshService {

    private final SecretProvider provider;
    private final AtomicReference<SecretSnapshot> current = new AtomicReference<>();

    public SecretRefreshService(SecretProvider provider) {
        this.provider = provider;
    }

    public SecretSnapshot current() {
        SecretSnapshot snapshot = current.get();
        if (snapshot == null) {
            snapshot = forceRefresh();
        }
        return snapshot;
    }

    public SecretSnapshot forceRefresh() {
        SecretSnapshot latest = provider.fetchCurrent();
        current.set(latest);
        return latest;
    }

    public boolean refreshIfVersionChanged() {
        SecretSnapshot latest = provider.fetchCurrent();
        SecretSnapshot existing = current.get();
        if (existing == null || !Objects.equals(existing.version(), latest.version())) {
            current.set(latest);
            return true;
        }
        return false;
    }
}
