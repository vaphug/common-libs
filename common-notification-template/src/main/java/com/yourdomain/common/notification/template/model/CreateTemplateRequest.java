package com.yourdomain.common.notification.template.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Request tạo mới template notification.
 */
@Getter
@Setter
public class CreateTemplateRequest {

    /** Định danh duy nhất của template cần tạo. */
    private String templateId;
    /** Loại template, ví dụ MAIL hoặc SMS. */
    private String type;
    /** Subject mặc định của template. */
    private String subject;
    /** Tiêu đề mặc định của template. */
    private String title;
    /** Nội dung text mặc định của template. */
    private String text;
    /** Nội dung HTML mặc định của template. */
    private String html;
    /** Người tạo template. */
    private String createdBy;
}
