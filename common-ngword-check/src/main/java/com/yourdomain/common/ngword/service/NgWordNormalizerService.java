package com.yourdomain.common.ngword.service;

/**
 * Dịch vụ chuẩn hóa chuỗi phục vụ check NG word.
 */
public interface NgWordNormalizerService {

    /**
     * Chuẩn hóa chuỗi phục vụ so khớp NG word.
     *
     * <p>Hàm này được dùng chung cho:
     * 1) input người dùng nhập
     * 2) token NG word từ DB
     * 3) token/rule whitelist
     *
     * @param text chuỗi gốc trước chuẩn hóa; có thể null. Ví dụ: {@code "abc@1"} hoặc {@code "ｱﾍﾞ・ｶﾞ"}.
     * @return chuỗi đã chuẩn hóa theo profile nghiệp vụ. Ví dụ: {@code "ＡＢＣ１"} hoặc {@code "アベガ"}.
     */
    String normalize(String text);
}
