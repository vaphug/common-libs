package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.InputValidationResult;

/**
 * Validate input theo rule cấu hình trước khi đi vào pipeline normalize và check NG.
 */
public interface InputValidationService {

    /**
     * Validate raw input theo scope nghiệp vụ.
     *
     * @param rawInput text gốc từ request; có thể null. Ví dụ: {@code "abc@1"} hoặc {@code ""}.
     * @param scope scope nghiệp vụ để chọn bộ rule validate. Ví dụ: {@code "member_register"} hoặc {@code "default"}.
     * @return kết quả validate gồm cờ hợp lệ và danh sách issue chi tiết
     */
    InputValidationResult validate(String rawInput, String scope);
}
