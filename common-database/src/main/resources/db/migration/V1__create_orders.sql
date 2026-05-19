-- DEPRECATED / DEMO-ONLY
-- Không dùng cho production.
-- Migration mẫu cho PostgreSQL: bảng orders tích hợp common-database
-- Bao gồm audit columns, soft-delete, optimistic lock bằng modified_at và index tối ưu truy vấn.

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT NOT NULL,
    order_code VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_user VARCHAR(100) NOT NULL,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_user VARCHAR(100) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT uk_orders_order_code UNIQUE (order_code)
);

-- Index tối ưu cho luồng query thường xuyên.
CREATE INDEX IF NOT EXISTS idx_orders_status_active
    ON orders (status)
    WHERE is_deleted = FALSE;

CREATE INDEX IF NOT EXISTS idx_orders_deleted_at
    ON orders (deleted_at)
    WHERE is_deleted = TRUE;

CREATE INDEX IF NOT EXISTS idx_orders_modified_at
    ON orders (modified_at DESC);

-- Trigger cập nhật modified_at tự động ở tầng DB để đảm bảo tính nhất quán nếu có cập nhật ngoài ứng dụng.
CREATE OR REPLACE FUNCTION fn_orders_touch_modified_at()
RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_orders_touch_modified_at ON orders;
CREATE TRIGGER trg_orders_touch_modified_at
BEFORE UPDATE ON orders
FOR EACH ROW
EXECUTE FUNCTION fn_orders_touch_modified_at();
