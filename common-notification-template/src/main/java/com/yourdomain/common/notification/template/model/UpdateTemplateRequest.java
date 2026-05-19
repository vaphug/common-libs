package com.yourdomain.common.notification.template.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Request cập nhật template notification hiện có.
 */
@Getter
@Setter
public class UpdateTemplateRequest {

    /** Loại template mới nếu caller muốn thay đổi. */
    private String type;
    /** Subject mới nếu caller muốn thay đổi. */
    private String subject;
    /** Tiêu đề mới nếu caller muốn thay đổi. */
    private String title;
    /** Nội dung text mới nếu caller muốn thay đổi. */
    private String text;
    /** Nội dung HTML mới nếu caller muốn thay đổi. */
    private String html;
    /** Người thực hiện cập nhật template. */
    private String updatedBy;
}
