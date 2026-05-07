package com.yourdomain.demo.api;

import com.yourdomain.common.validation.validator.DomainValidator;
import com.yourdomain.common.validation.validator.RegexMatcher;
import org.springframework.stereotype.Service;

@Service
public class ValidationUtilityService {

    public boolean checkRegex(String value, String pattern) {
        return RegexMatcher.isValid(value, pattern);
    }

    public boolean checkFullWidth(String value) {
        return DomainValidator.isFullWidth(value);
    }
}
