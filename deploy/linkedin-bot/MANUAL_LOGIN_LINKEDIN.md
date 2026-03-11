# Manual rápido: login manual no LinkedIn e continuação automática do robô

Este guia é para o cenário em que o LinkedIn pede intervenção humana (senha, 2FA, captcha ou checkpoint) e, após isso, o robô deve seguir sozinho usando a sessão já salva.

## 1) Preparar ambiente

Na pasta `deploy/linkedin-bot`, crie/edite o `.env` com estes valores:

```env
LINKEDIN_HEADLESS=false
LINKEDIN_CHROME_USER_DATA_DIR=/home/seluser/chrome-profile
LINKEDIN_CHROME_PROFILE=Default
LINKEDIN_CHECKPOINT_TIMEOUT_SECONDS=300
SELENIUM_REMOTE_URL=http://selenium:4444/wd/hub
```

Crie a pasta que guarda a sessão do navegador:

```bash
mkdir -p linkedin_chrome_profile
chmod 777 linkedin_chrome_profile
```

> Essa pasta é essencial: nela ficam cookies e sessão do LinkedIn para as próximas execuções.

## 2) Subir modo de login manual

```bash
docker compose --profile manual-login up -d
```

Abra no navegador:

- `http://SEU_HOST:7900`
- Senha padrão do noVNC (Selenium): `secret`

## 3) Fazer login humano no LinkedIn

Dentro da tela noVNC:

1. Faça login com usuário e senha.
2. Resolva 2FA, captcha e checkpoint (se aparecer).
3. Aguarde até chegar na Home/Jobs do LinkedIn.

Quando chegar nessa tela, a sessão já deve ter sido persistida em `linkedin_chrome_profile`.

## 4) Voltar para execução automática do robô

Depois do login manual concluído, rode o bot normalmente (de preferência em headless):

```bash
LINKEDIN_HEADLESS=true docker compose up -d linkedin-bot
```

Pronto: o robô volta a rodar sozinho usando a sessão autenticada.

## 5) Conferência rápida (opcional)

Ver logs para confirmar inicialização sem bloqueio:

```bash
docker compose logs -f linkedin-bot
```

Se precisar validar que o Selenium está disponível:

```bash
curl -s http://localhost:4444/status
```

Procure por `"ready": true`.

## Solução de problemas

### Tela preta no noVNC

Normalmente significa falta de sessão de navegador ativa.

```bash
docker compose --profile manual-login down
docker compose --profile manual-login up -d selenium linkedin-bot
```

Depois, reabra `http://SEU_HOST:7900`.

### Robô pediu login de novo

- Verifique se `LINKEDIN_CHROME_USER_DATA_DIR` continua como `/home/seluser/chrome-profile`.
- Verifique se o volume `./linkedin_chrome_profile:/home/seluser/chrome-profile` está ativo no compose.
- Repita o fluxo manual quando o LinkedIn invalidar a sessão.

---

**Importante:** este processo não contorna segurança do LinkedIn; ele apenas permite completar manualmente as etapas exigidas e reaproveitar a sessão autenticada.
