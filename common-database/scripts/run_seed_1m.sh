#!/usr/bin/env bash
set -euo pipefail

: "${DB_HOST:=localhost}"
: "${DB_PORT:=5432}"
: "${DB_NAME:=inventory_db}"
: "${DB_USER:=postgres}"
: "${DB_PASSWORD:=postgres}"

export PGPASSWORD="${DB_PASSWORD}"

psql -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" \
  -v start_id=1 -v end_id=1000000 \
  -f "$(dirname "$0")/seed_orders_common_demo.sql"

psql -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" \
  -f "$(dirname "$0")/verify_orders_common_demo.sql"
