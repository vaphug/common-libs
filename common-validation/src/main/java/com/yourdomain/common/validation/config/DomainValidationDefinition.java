package com.yourdomain.common.validation.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yourdomain.common.validation.validator.MaxLengthConstraint;
import com.yourdomain.common.validation.validator.ValidationKind;

import java.util.Map;

public class DomainValidationDefinition {
    private final String type;
    private final ValidationKind kind;
    private final String maxLength;
    private final String messageId;
    private final Map<String, String> params;
    private final MaxLengthConstraint maxLengthConstraint;

    @JsonCreator
    public DomainValidationDefinition(
            @JsonProperty("type") String type,
            @JsonProperty("kind") ValidationKind kind,
            @JsonProperty("maxLength") String maxLength,
            @JsonProperty("messageId") String messageId,
            @JsonProperty("params") Map<String, String> params
    ) {
        this.type = type;
        this.kind = kind;
        this.maxLength = maxLength;
        this.messageId = messageId;
        this.params = params == null ? Map.of() : params;
        this.maxLengthConstraint = MaxLengthConstraint.parse(maxLength);
    }

    public String getType() {
        return type;
    }

    public ValidationKind getKind() {
        return kind;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public String getMessageId() {
        return messageId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public MaxLengthConstraint getMaxLengthConstraint() {
        return maxLengthConstraint;
    }
}
