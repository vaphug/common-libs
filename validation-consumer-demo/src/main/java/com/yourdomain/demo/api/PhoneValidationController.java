package com.yourdomain.demo.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/phones")
public class PhoneValidationController {

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@Valid @RequestBody PhoneValidationRequest request) {
        return ResponseEntity.ok("valid");
    }
}
