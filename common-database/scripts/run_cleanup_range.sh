#!/usr/bin/env bash
set -euo pipefail

: "${DB_HOST:=localhost}"
: "${DB_PORT:=5432}"
: "${DB_NAME:=inventory_db}"
: "${DB_USER:=postgres}"
: "${DB_PASSWORD:=postgres}"
: "${START_ID:=1}"
: "${END_ID:=1000000}"

export PGPASSWORD="${DB_PASSWORD}"

psql -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" \
  -v start_id="${START_ID}" -v end_id="${END_ID}" \
  -f "$(dirname "$0")/cleanup_orders_common_demo_range.sql"

echo "Cleanup done for id range [${START_ID}, ${END_ID}]"
