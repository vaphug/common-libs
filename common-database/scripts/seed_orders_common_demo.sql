-- Seed nhanh dữ liệu lớn cho orders_common_demo bằng INSERT..SELECT (dùng generate_series trong DB).
-- Cách dùng:
-- psql -h localhost -p 5432 -U postgres -d inventory_db -v start_id=1 -v end_id=1000000 -f seed_orders_common_demo.sql

\set ON_ERROR_STOP on

INSERT INTO orders_common_demo (
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
SELECT
    gs AS id,
    'ORD-' || gs::text AS order_code,
    (ARRAY['NEW','PAID','CANCELLED','SHIPPED'])[1 + (random() * 3)::int] AS status,
    NOW() - ((random() * 730)::int || ' days')::interval AS created_at,
    'seed-user' AS created_user,
    NOW() - ((random() * 365)::int || ' days')::interval AS modified_at,
    'seed-user' AS modified_user,
    (random() < 0.10) AS is_deleted,
    CASE
        WHEN random() < 0.10 THEN NOW() - ((random() * 180)::int || ' days')::interval
        ELSE NULL
    END AS deleted_at
FROM generate_series(:start_id, :end_id) gs
ON CONFLICT (id) DO NOTHING;

ANALYZE orders_common_demo;
