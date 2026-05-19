package com.yourdomain.common.secretmanager.service;

import com.yourdomain.common.secretmanager.model.SecretSnapshot;

public interface SecretProvider {
    SecretSnapshot fetchCurrent();
}
