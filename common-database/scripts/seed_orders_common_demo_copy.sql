-- Seed cực nhanh bằng COPY từ CSV đã chuẩn bị trước.
-- Cách dùng:
-- psql -h localhost -p 5432 -U postgres -d inventory_db -v csv_path="/absolute/path/orders_common_demo.csv" -f seed_orders_common_demo_copy.sql

\set ON_ERROR_STOP on

COPY orders_common_demo (
    id,
    order_code,
    status,
    created_at,
    created_user,
    modified_at,
    modified_user,
    is_deleted,
    deleted_at
)
FROM :'csv_path'
WITH (
    FORMAT csv,
    HEADER true,
    DELIMITER ',',
    QUOTE '"'
);

ANALYZE orders_common_demo;
