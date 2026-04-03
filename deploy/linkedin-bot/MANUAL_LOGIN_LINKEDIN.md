# Manual do LinkedInBot no VPS com interface gráfica

Este fluxo foi simplificado para o cenário atual: **você já abre o LinkedIn no VPS com interface gráfica e deixa a sessão logada**.

O bot agora:

1. tenta entrar direto em `https://www.linkedin.com/jobs`;
2. se encontrar sessão ativa no profile, segue automaticamente;
3. só tenta login por usuário/senha se `LINKEDIN_USER` e `LINKEDIN_PASSWORD` estiverem definidos.

---

## 1) Pré-requisitos

Na pasta `deploy/linkedin-bot`:

```bash
cd deploy/linkedin-bot
mkdir -p linkedin_chrome_profile
chmod 777 linkedin_chrome_profile
```

> O volume `./linkedin_chrome_profile` precisa existir para persistir cookies/sessão.

## 2) Configurar `.env`

Use como base:

```env
LINKEDIN_HEADLESS=true
LINKEDIN_CHROME_USER_DATA_DIR=/home/seluser/chrome-profile
LINKEDIN_CHROME_PROFILE=Default
LINKEDIN_CHECKPOINT_TIMEOUT_SECONDS=300
```

Se quiser fallback por credenciais quando a sessão expirar:

```env
LINKEDIN_USER=seu_login
LINKEDIN_PASSWORD=sua_senha
```

## 3) Iniciar o robô pelo terminal da GUI

```bash
/opt/curriculosis/linkedin-bot/start-linkedin-bot.sh
```

## 4) Validar execução

```bash
docker compose --env-file /opt/curriculosis/linkedin-bot/.env -f /opt/curriculosis/linkedin-bot/docker-compose.yml logs -f linkedin-bot
```

---

## Solução de problemas

### Erro `permission denied` no Docker socket

Se aparecer erro ao conectar em `/var/run/docker.sock`, use:

```bash
sudo usermod -aG docker "$USER"
```

Depois abra um novo terminal da sessão RDP e rode novamente:

```bash
/opt/curriculosis/linkedin-bot/start-linkedin-bot.sh
```

> O `start-linkedin-bot.sh` já tenta fallback automático para `sudo docker` quando não há acesso direto ao Docker.

### Bot abriu login mesmo com VPS já autenticado

Checklist:

1. Verifique se o volume está montado:

```bash
docker compose exec linkedin-bot env | grep -E 'LINKEDIN_CHROME_USER_DATA_DIR|LINKEDIN_CHROME_PROFILE'
```

2. Confirme se você realmente logou no mesmo profile (`Default`) e no mesmo diretório persistido.
3. Se a sessão tiver expirado, relogue no LinkedIn e execute o bot novamente.

### Erro informando ausência de credenciais

Esse erro aparece quando:

- não havia sessão no profile; e
- `LINKEDIN_USER` / `LINKEDIN_PASSWORD` não foram definidos.

Nesse caso, ou restaure a sessão no profile ou configure as credenciais como fallback.
