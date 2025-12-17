#!/usr/bin/env bash
set -euo pipefail

# Run backend + frontend + quick scrapy test (developer convenience script)
# This script does NOT store or transmit secrets. Export DB creds locally before running.

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
LOG_DIR="$ROOT_DIR/logs"
mkdir -p "$LOG_DIR"

echo "=== run-all: starting quick local run (logs -> $LOG_DIR) ==="

# Backend
if [ -n "${SPRING_DATASOURCE_URL-}" ] && [ -n "${SPRING_DATASOURCE_USERNAME-}" ] && [ -n "${SPRING_DATASOURCE_PASSWORD-}" ]; then
  echo "Starting backend..."
  (cd "$ROOT_DIR" && ./scripts/run-backend.sh) > "$LOG_DIR/backend.log" 2>&1 &
  echo "backend PID: $! (logs: $LOG_DIR/backend.log)"
else
  echo "Skipping backend: SPRING_DATASOURCE_URL / USER / PASSWORD not set. Export them to run backend."
fi

# Frontend (Vite)
if [ -d "$ROOT_DIR/frontend" ]; then
  if command -v npm >/dev/null 2>&1; then
    echo "Starting frontend (vite)..."
    (cd "$ROOT_DIR/frontend" && npm run dev) > "$LOG_DIR/frontend.log" 2>&1 &
    echo "frontend PID: $! (logs: $LOG_DIR/frontend.log)"
  else
    echo "npm not found: install Node.js to run frontend locally"
  fi
else
  echo "No frontend directory found, skipping frontend"
fi

# Scrapy quick test (uses run_quick_test.py) - will create venv if missing
SCRAPY_DIR="$ROOT_DIR/scrapy/scrapy_cotizador"
if [ -d "$SCRAPY_DIR" ]; then
  echo "Preparing scrapy quick test..."
  if [ ! -d "$SCRAPY_DIR/.venv" ]; then
    echo "Creating venv for scrapy at $SCRAPY_DIR/.venv"
    python3 -m venv "$SCRAPY_DIR/.venv"
    "$SCRAPY_DIR/.venv/bin/pip" install --upgrade pip
    echo "Installing scrapy requirements (this may take a while)..."
    "$SCRAPY_DIR/.venv/bin/pip" install -r "$SCRAPY_DIR/requirements.txt"
    "$SCRAPY_DIR/.venv/bin/python" -m playwright install chromium
  fi

  echo "Running scrapy quick test (no DB)"
  "$SCRAPY_DIR/.venv/bin/python" "$SCRAPY_DIR/run_quick_test.py" > "$LOG_DIR/scrapy_quick_test.log" 2>&1 || true
  echo "scrapy quick test finished (logs: $LOG_DIR/scrapy_quick_test.log)"
else
  echo "No scrapy project found at $SCRAPY_DIR, skipping scrapy test"
fi

echo "=== run-all complete; check logs in $LOG_DIR ==="
