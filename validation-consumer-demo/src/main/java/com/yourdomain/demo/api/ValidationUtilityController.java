package com.yourdomain.demo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/utils")
public class ValidationUtilityController {
    private final ValidationUtilityService service;

    public ValidationUtilityController(ValidationUtilityService service) {
        this.service = service;
    }

    @GetMapping("/validate")
    public Map<String, Boolean> validate(
            @RequestParam String value,
            @RequestParam String pattern
    ) {
        boolean checkRegex = service.checkRegex(value, pattern);
        boolean checkFullWidth = service.checkFullWidth(value);
        return Map.of(
                "checkRegex", checkRegex,
                "checkFullWidth", checkFullWidth
        );
    }
}
