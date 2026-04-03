#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

if ! command -v docker >/dev/null 2>&1; then
  echo "[ERRO] Docker não encontrado no PATH."
  exit 1
fi

mkdir -p linkedin_chrome_profile
chmod 777 linkedin_chrome_profile || true

echo "[INFO] Iniciando LinkedIn bot..."
docker compose --env-file .env -f docker-compose.yml up -d linkedin-bot

echo "[INFO] Bot iniciado. Logs em tempo real:"
docker compose --env-file .env -f docker-compose.yml logs -f --tail=120 linkedin-bot
