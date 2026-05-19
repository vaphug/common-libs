\set ON_ERROR_STOP on

SELECT COUNT(*) AS total_records FROM orders_common_demo;
SELECT status, COUNT(*) AS cnt FROM orders_common_demo GROUP BY status ORDER BY status;
SELECT is_deleted, COUNT(*) AS cnt FROM orders_common_demo GROUP BY is_deleted ORDER BY is_deleted;
