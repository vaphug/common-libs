-- Bảng demo riêng cho common-database, tách biệt hoàn toàn với bảng orders nghiệp vụ cũ.
CREATE TABLE IF NOT EXISTS orders_common_demo (
    id BIGINT PRIMARY KEY,
    order_code VARCHAR(64) NOT NULL UNIQUE,
    status VARCHAR(32) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_user VARCHAR(100) NOT NULL DEFAULT 'system',
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_user VARCHAR(100) NOT NULL DEFAULT 'system',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP NULL
);

CREATE INDEX IF NOT EXISTS idx_orders_common_demo_status_active
    ON orders_common_demo (status)
    WHERE is_deleted = FALSE;

CREATE INDEX IF NOT EXISTS idx_orders_common_demo_deleted_at
    ON orders_common_demo (deleted_at)
    WHERE is_deleted = TRUE;

CREATE INDEX IF NOT EXISTS idx_orders_common_demo_modified_at
    ON orders_common_demo (modified_at DESC);

CREATE OR REPLACE FUNCTION fn_orders_common_demo_touch_modified_at()
RETURNS TRIGGER AS
$$
BEGIN
    NEW.modified_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_orders_common_demo_touch_modified_at ON orders_common_demo;
CREATE TRIGGER trg_orders_common_demo_touch_modified_at
BEFORE UPDATE ON orders_common_demo
FOR EACH ROW
EXECUTE FUNCTION fn_orders_common_demo_touch_modified_at();
