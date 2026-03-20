# Manual do LinkedInBot: login manual via Selenium e execução automática

Este manual explica como usar o **LinkedInBot** quando o LinkedIn exige interação humana (login, 2FA, captcha ou checkpoint).

A ideia é simples:

1. abrir o navegador remoto pelo Selenium (com tela),
2. fazer o login manualmente,
3. salvar a sessão no perfil do Chrome,
4. voltar o bot para modo automático.

## 1) Pré-requisitos

Na pasta `deploy/linkedin-bot`:

- tenha Docker + Docker Compose funcionando;
- garanta que o arquivo `.env` exista;
- garanta uma pasta para persistir a sessão do navegador.

```bash
cd deploy/linkedin-bot
mkdir -p linkedin_chrome_profile
chmod 777 linkedin_chrome_profile
```

> Sem essa pasta, a sessão/cookies não persistem e o bot pode pedir login novamente toda vez.

## 2) Configurar o `.env` para login manual

Use os valores abaixo:

```env
LINKEDIN_HEADLESS=false
LINKEDIN_CHROME_USER_DATA_DIR=/home/seluser/chrome-profile
LINKEDIN_CHROME_PROFILE=Default
LINKEDIN_CHECKPOINT_TIMEOUT_SECONDS=300
SELENIUM_REMOTE_URL=http://selenium:4444/wd/hub
```

## 3) Subir os containers em modo manual

```bash
docker compose --profile manual-login up -d
```

Abra a tela do Selenium no navegador:

- `http://SEU_HOST:7900`
- senha padrão do noVNC: `secret`

## 4) Fazer o login manual no LinkedIn

Na tela do noVNC:

1. Faça login com usuário/senha;
2. Conclua 2FA, captcha e checkpoint (se aparecer);
3. Aguarde chegar na Home/Jobs do LinkedIn.

Quando chegar nessa tela, a sessão deve estar salva no diretório `linkedin_chrome_profile`.

## 5) Voltar para execução automática do LinkedInBot

Depois do login manual, rode o bot em modo automático (headless):

```bash
LINKEDIN_HEADLESS=true docker compose up -d linkedin-bot
```

Pronto: o bot continua sozinho usando a sessão já autenticada.

## 6) Validação rápida (opcional)

Ver logs do bot:

```bash
docker compose logs -f linkedin-bot
```

Ver status do Selenium:

```bash
curl -s http://localhost:4444/status
```

No retorno JSON, valide `"ready": true`.

## Solução de problemas

### Tela preta no noVNC

Geralmente significa que ainda não existe sessão de navegador ativa.

```bash
docker compose --profile manual-login down
docker compose --profile manual-login up -d selenium linkedin-bot
```

Depois reabra `http://SEU_HOST:7900`.

### Bot pediu login de novo

Confira:

- `LINKEDIN_CHROME_USER_DATA_DIR=/home/seluser/chrome-profile`;
- volume ativo no compose: `./linkedin_chrome_profile:/home/seluser/chrome-profile`;
- se o LinkedIn invalidou a sessão, repita o fluxo manual.

---

**Importante:** este fluxo não contorna segurança do LinkedIn. Ele apenas permite concluir manualmente as etapas obrigatórias e depois reaproveitar a sessão autenticada.
