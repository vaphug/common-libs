package com.yourdomain.common.validation.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class DomainValidationRepository {
    private static final String CONFIG_PATH = "validation/DomainValidationData.json";

    private final Map<String, DomainValidationDefinition> definitions;

    private DomainValidationRepository(Map<String, DomainValidationDefinition> definitions) {
        this.definitions = definitions;
    }

    public static DomainValidationRepository load() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try (InputStream is = DomainValidationRepository.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            if (is == null) {
                return new DomainValidationRepository(Map.of());
            }
            Map<String, DomainValidationDefinition> data = mapper.readValue(
                    is,
                    new TypeReference<>() {
                    }
            );
            return new DomainValidationRepository(data);
        } catch (IOException ex) {
            throw new IllegalStateException("Khong the doc file cau hinh validation: " + CONFIG_PATH, ex);
        }
    }

    public DomainValidationDefinition find(String itemKey) {
        return definitions.get(itemKey);
    }
}
