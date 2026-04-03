#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

if ! command -v docker >/dev/null 2>&1; then
  echo "[ERRO] Docker não encontrado no PATH."
  exit 1
fi

ensure_env_file() {
  if [ -f .env ]; then
    return
  fi

  cat > .env <<'ENVEOF'
# Gerado automaticamente por start-linkedin-bot.sh
# Ajuste conforme necessário.
LINKEDIN_HEADLESS=false
LINKEDIN_CHROME_USER_DATA_DIR=/home/seluser/chrome-profile
LINKEDIN_CHROME_PROFILE=Default
LINKEDIN_CHECKPOINT_TIMEOUT_SECONDS=300
ENVEOF

  echo "[INFO] Arquivo .env não encontrado. Foi criado um .env padrão em $SCRIPT_DIR/.env"
}

resolve_docker_cmd() {
  if docker info >/dev/null 2>&1; then
    echo "docker"
    return
  fi

  if command -v sudo >/dev/null 2>&1 && sudo -n docker info >/dev/null 2>&1; then
    echo "sudo docker"
    return
  fi

  echo "[ERRO] Sem permissão para acessar o Docker daemon (/var/run/docker.sock)."
  echo "[DICA] Execute um destes comandos e tente novamente:"
  echo "       sudo usermod -aG docker \$USER"
  echo "       newgrp docker"
  echo "       # ou rode diretamente com sudo"
  exit 1
}

ensure_env_file

mkdir -p linkedin_chrome_profile
chmod 777 linkedin_chrome_profile || true

DOCKER_CMD="$(resolve_docker_cmd)"
echo "[INFO] Iniciando LinkedIn bot..."
$DOCKER_CMD compose --env-file .env -f docker-compose.yml up -d linkedin-bot

echo "[INFO] Bot iniciado. Logs em tempo real:"
$DOCKER_CMD compose --env-file .env -f docker-compose.yml logs -f --tail=120 linkedin-bot
