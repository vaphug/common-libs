-- Top query theo total_exec_time để so sánh trước/sau index.
SELECT
  query,
  calls,
  round(total_exec_time::numeric, 3) AS total_ms,
  round(mean_exec_time::numeric, 3) AS mean_ms,
  rows,
  shared_blks_hit,
  shared_blks_read
FROM pg_stat_statements
WHERE dbid = (SELECT oid FROM pg_database WHERE datname = current_database())
ORDER BY total_exec_time DESC
LIMIT 30;
