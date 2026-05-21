package com.yourdomain.common.ngword.service;

import com.yourdomain.common.ngword.model.NormalizationProfile;
import java.util.Map;

/**
 * Cung cấp profile chuẩn hóa theo từng scope/domain nghiệp vụ.
 */
public interface NormalizationProfileProvider {

    /**
     * Lấy profile chuẩn hóa áp dụng cho scope hiện tại.
     *
     * @param domainOrScreen mã domain hoặc màn hình nghiệp vụ. Ví dụ: {@code "member_register"} hoặc {@code "default"}.
     * @return profile chuẩn hóa đầy đủ bật/tắt các rule
     */
    NormalizationProfile getProfile(String domainOrScreen);

    /**
     * Lấy bảng mapping biểu thị tương đương (表示ゆれ) theo scope.
     *
     * @param domainOrScreen mã domain hoặc màn hình nghiệp vụ. Ví dụ: {@code "member_register"} hoặc {@code "default"}.
     * @return map nguồn-đích cho bước normalize notation variants
     */
    Map<String, String> getNotationVariantMap(String domainOrScreen);
}
