package com.yourdomain.common.ngword.model;

/**
 * Danh sách scope hợp lệ cho nghiệp vụ NG word check.
 *
 * <p>Scope dùng để chọn bộ rule validate/normalize/whitelist theo từng màn hình hoặc nghiệp vụ.
 * Nó không phải cơ chế phân quyền (authorization).
 */
public final class NgWordScopes {

    /** Scope mặc định khi request không truyền hoặc truyền rỗng. */
    public static final String DEFAULT = "default";

    /**
     * Ví dụ scope cho màn hình đăng ký hội viên.
     *
     * <p>Lưu ý: đây là scope gợi ý để minh hoạ cách phân tách rule theo màn hình/nghiệp vụ.
     * Cần confirm với spec/khách trước khi coi là scope chính thức.
     */
    public static final String MEMBER_REGISTER = "member_register";

    /**
     * Ví dụ scope cho màn hình cập nhật hồ sơ hội viên.
     *
     * <p>Lưu ý: đây là scope gợi ý để minh hoạ cách phân tách rule theo màn hình/nghiệp vụ.
     * Cần confirm với spec/khách trước khi coi là scope chính thức.
     */
    public static final String MEMBER_PROFILE_UPDATE = "member_profile_update";

    /**
     * Ví dụ scope cho các ô nhập free text (inquiry/notes) nơi rule validate có thể nới lỏng hoặc siết chặt riêng.
     *
     * <p>Lưu ý: đây là scope gợi ý để minh hoạ cách phân tách rule theo màn hình/nghiệp vụ.
     * Cần confirm với spec/khách trước khi coi là scope chính thức.
     */
    public static final String INQUIRY_FREE_TEXT = "inquiry_free_text";

    /**
     * Ví dụ scope cho luồng import/batch (admin) nơi dữ liệu có thể đến từ file và cần rule validate riêng.
     *
     * <p>Lưu ý: đây là scope gợi ý để minh hoạ cách phân tách rule theo màn hình/nghiệp vụ.
     * Cần confirm với spec/khách trước khi coi là scope chính thức.
     */
    public static final String ADMIN_BULK_IMPORT = "admin_bulk_import";

    private NgWordScopes() {
    }
}
