-- DEPRECATED / DEMO-ONLY
-- Không dùng cho production.
-- Nâng cấp bảng orders hiện hữu về chuẩn common-database mà không mất dữ liệu.
-- Dùng cho trường hợp bảng orders đã tồn tại từ trước với schema cũ.

ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS order_code VARCHAR(64),
    ADD COLUMN IF NOT EXISTS status VARCHAR(32),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS created_user VARCHAR(100),
    ADD COLUMN IF NOT EXISTS modified_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS modified_user VARCHAR(100),
    ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN,
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

UPDATE orders
SET
    created_at = COALESCE(created_at, CURRENT_TIMESTAMP),
    created_user = COALESCE(created_user, 'system-migration'),
    modified_at = COALESCE(modified_at, CURRENT_TIMESTAMP),
    modified_user = COALESCE(modified_user, 'system-migration'),
    is_deleted = COALESCE(is_deleted, FALSE),
    status = COALESCE(status, 'UNKNOWN')
WHERE created_at IS NULL
   OR created_user IS NULL
   OR modified_at IS NULL
   OR modified_user IS NULL
   OR is_deleted IS NULL
   OR status IS NULL;

ALTER TABLE orders
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN created_user SET DEFAULT 'system',
    ALTER COLUMN modified_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN modified_user SET DEFAULT 'system',
    ALTER COLUMN is_deleted SET DEFAULT FALSE;

ALTER TABLE orders
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN created_user SET NOT NULL,
    ALTER COLUMN modified_at SET NOT NULL,
    ALTER COLUMN modified_user SET NOT NULL,
    ALTER COLUMN is_deleted SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_orders_status_active
    ON orders (status)
    WHERE is_deleted = FALSE AND status IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_orders_deleted_at
    ON orders (deleted_at)
    WHERE is_deleted = TRUE;

CREATE INDEX IF NOT EXISTS idx_orders_modified_at
    ON orders (modified_at DESC);

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

-- Tăng độ an toàn cho dữ liệu demo: nếu order_code đang null thì tự điền theo id.
UPDATE orders
SET order_code = COALESCE(order_code, 'ORD-' || id::text)
WHERE order_code IS NULL;

-- Hỗ trợ schema legacy: nếu có cột table_number và đang NOT NULL nhưng chưa có default,
-- đặt default để tránh lỗi insert khi request chưa truyền field này.
DO
$$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'orders'
          AND column_name = 'table_number'
          AND is_nullable = 'NO'
          AND column_default IS NULL
    ) THEN
        ALTER TABLE orders ALTER COLUMN table_number SET DEFAULT 0;
    END IF;
END
$$;
