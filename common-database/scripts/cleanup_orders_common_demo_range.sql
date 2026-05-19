-- Xóa dữ liệu benchmark theo khoảng id.
-- Cách dùng:
-- psql -h localhost -p 5432 -U postgres -d inventory_db -v start_id=1 -v end_id=1000000 -f cleanup_orders_common_demo_range.sql

\set ON_ERROR_STOP on

DELETE FROM orders_common_demo
WHERE id BETWEEN :start_id AND :end_id;

VACUUM (ANALYZE) orders_common_demo;
