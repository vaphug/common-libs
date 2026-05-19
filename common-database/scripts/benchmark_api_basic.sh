#!/usr/bin/env bash
set -euo pipefail

: "${BASE_URL:=http://localhost:8093}"
: "${WARMUP:=20}"
: "${ITERATIONS:=200}"
: "${ID_FROM:=1}"
: "${ID_TO:=1000000}"

endpoint_findall="${BASE_URL}/api/v1/orders?offset=0&limit=100&includeDeleted=false"
endpoint_search="${BASE_URL}/api/v1/orders/search"

measure_ms() {
  local url="$1"
  local method="${2:-GET}"
  local body="${3:-}"
  if [[ "$method" == "GET" ]]; then
    curl -s -o /dev/null -w '%{time_total}' "$url"
  else
    curl -s -o /dev/null -w '%{time_total}' -X "$method" -H 'Content-Type: application/json' -d "$body" "$url"
  fi
}

run_case() {
  local name="$1"
  local mode="$2"
  local total=0
  local min=999999
  local max=0

  for ((i=1; i<=WARMUP; i++)); do
    if [[ "$mode" == "findById" ]]; then
      id=$((ID_FROM + RANDOM % (ID_TO - ID_FROM + 1)))
      measure_ms "${BASE_URL}/api/v1/orders/${id}?lockMode=NONE&includeDeleted=false" >/dev/null
    elif [[ "$mode" == "findAll" ]]; then
      measure_ms "$endpoint_findall" >/dev/null
    else
      measure_ms "$endpoint_search" "POST" '{"equalsFilters":{"status":"PAID"},"offset":0,"limit":100,"includeDeleted":false}' >/dev/null
    fi
  done

  for ((i=1; i<=ITERATIONS; i++)); do
    if [[ "$mode" == "findById" ]]; then
      id=$((ID_FROM + RANDOM % (ID_TO - ID_FROM + 1)))
      t=$(measure_ms "${BASE_URL}/api/v1/orders/${id}?lockMode=NONE&includeDeleted=false")
    elif [[ "$mode" == "findAll" ]]; then
      t=$(measure_ms "$endpoint_findall")
    else
      t=$(measure_ms "$endpoint_search" "POST" '{"equalsFilters":{"status":"PAID"},"offset":0,"limit":100,"includeDeleted":false}')
    fi

    ms=$(awk -v x="$t" 'BEGIN { printf "%.3f", x*1000 }')
    total=$(awk -v a="$total" -v b="$ms" 'BEGIN { printf "%.3f", a+b }')
    min=$(awk -v a="$min" -v b="$ms" 'BEGIN { if (b<a) print b; else print a }')
    max=$(awk -v a="$max" -v b="$ms" 'BEGIN { if (b>a) print b; else print a }')
  done

  avg=$(awk -v t="$total" -v n="$ITERATIONS" 'BEGIN { printf "%.3f", t/n }')
  echo "$name: avg=${avg}ms min=${min}ms max=${max}ms iterations=${ITERATIONS}"
}

echo "Benchmarking BASE_URL=${BASE_URL} ITERATIONS=${ITERATIONS}"
run_case "findById" "findById"
run_case "findAll" "findAll"
run_case "search(status=PAID)" "search"
