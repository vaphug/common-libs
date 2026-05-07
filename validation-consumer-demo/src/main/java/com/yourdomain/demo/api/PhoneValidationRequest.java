package com.yourdomain.demo.api;

import com.yourdomain.common.validation.ValidPhoneNumber;

public record PhoneValidationRequest(@ValidPhoneNumber String phoneNumber) {
}
