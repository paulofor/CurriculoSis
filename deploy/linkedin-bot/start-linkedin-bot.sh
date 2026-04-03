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

DOCKER_CMD=(docker)
if ! docker info >/dev/null 2>&1; then
  if command -v sudo >/dev/null 2>&1; then
    echo "[INFO] Sem acesso direto ao Docker. Tentando executar com sudo..."
    DOCKER_CMD=(sudo docker)
    if ! sudo -n docker info >/dev/null 2>&1; then
      echo "[ERRO] O usuário atual não consegue acessar o Docker nem com sudo sem senha."
      echo "[DICA] Execute um dos comandos abaixo e abra um novo terminal:"
      echo "       sudo usermod -aG docker \"$USER\""
      echo "       # ou rode este script com sudo"
      exit 1
    fi
  else
    echo "[ERRO] Usuário sem permissão para acessar /var/run/docker.sock."
    echo "[DICA] Adicione o usuário ao grupo docker e abra um novo terminal:"
    echo "       sudo usermod -aG docker \"$USER\""
    exit 1
  fi
fi

echo "[INFO] Iniciando LinkedIn bot..."
"${DOCKER_CMD[@]}" compose --env-file .env -f docker-compose.yml up -d linkedin-bot

echo "[INFO] Bot iniciado. Logs em tempo real:"
"${DOCKER_CMD[@]}" compose --env-file .env -f docker-compose.yml logs -f --tail=120 linkedin-bot
