#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

JAR_PATH="${JAR_PATH:-$REPO_ROOT/dist/ObtemOportunidadeLinkedin.jar}"
CHROME_BIN="${CHROME_BIN:-/usr/bin/google-chrome}"
DRIVER_PATH="${DRIVER_PATH:-/usr/local/bin/chromedriver}"

if [ ! -x "$CHROME_BIN" ]; then
  echo "[ERRO] Chrome não encontrado em: $CHROME_BIN"
  echo "[DICA] Defina CHROME_BIN, exemplo: CHROME_BIN=/usr/bin/google-chrome ./run-linkedin-bot-local.sh"
  exit 1
fi

if [ ! -f "$JAR_PATH" ]; then
  echo "[ERRO] Jar não encontrado em: $JAR_PATH"
  echo "[DICA] Defina JAR_PATH, exemplo: JAR_PATH=/opt/curriculosis/linkedin-bot/ObtemOportunidadeLinkedin.jar ./run-linkedin-bot-local.sh"
  exit 1
fi

if ! command -v wget >/dev/null 2>&1; then
  echo "[ERRO] wget não encontrado. Instale com: sudo apt-get update && sudo apt-get install -y wget"
  exit 1
fi

if ! command -v unzip >/dev/null 2>&1; then
  echo "[ERRO] unzip não encontrado. Instale com: sudo apt-get update && sudo apt-get install -y unzip"
  exit 1
fi

CHROME_VERSION="$($CHROME_BIN --version | awk '{print $3}')"
URL="https://storage.googleapis.com/chrome-for-testing-public/${CHROME_VERSION}/linux64/chromedriver-linux64.zip"

TMP_ZIP="/tmp/chromedriver-${CHROME_VERSION}.zip"
TMP_DIR="/tmp/chromedriver-linux64"

echo "[INFO] Chrome detectado: ${CHROME_VERSION}"
echo "[INFO] Baixando ChromeDriver compatível: ${URL}"
wget -q -O "$TMP_ZIP" "$URL"

rm -rf "$TMP_DIR"
unzip -oq "$TMP_ZIP" -d /tmp
sudo mv /tmp/chromedriver-linux64/chromedriver "$DRIVER_PATH"
sudo chmod +x "$DRIVER_PATH"

rm -rf "${HOME}/.cache/selenium/chromedriver"

echo "[INFO] ChromeDriver instalado em: $DRIVER_PATH"
"$DRIVER_PATH" --version || true

echo "[INFO] Iniciando bot com Java..."
exec java \
  -Dwdm.chromeDriverVersion="$CHROME_VERSION" \
  -Dwebdriver.chrome.driver="$DRIVER_PATH" \
  -jar "$JAR_PATH"
