# Manual de uso do LinkedInBot (login manual via Selenium)

Este manual explica como executar o módulo `obtemoportunidadelinkedin` com **login manual** no LinkedIn usando Selenium.

## 1) Pré-requisitos

- Java 8+ instalado.
- Maven instalado.
- Google Chrome/Chromium instalado.
- A API Loopback acessível (para persistir oportunidades).

## 2) Build do projeto

No diretório raiz do repositório:

```bash
mvn -f fontes-estruturado/principal/obtemoportunidadelinkedin/pom.xml -DskipTests compile
```

## 3) Variáveis de ambiente obrigatórias

### Obrigatória

- `LOOPBACK_URL`: URL da API Loopback (ex.: `http://localhost:3000/api`).

### Para login manual

- `LINKEDIN_MANUAL_LOGIN=true` para habilitar login manual.
- `LINKEDIN_HEADLESS=false` para abrir a janela do navegador (necessário para digitar usuário/senha e resolver 2FA/captcha).
- `LINKEDIN_MANUAL_LOGIN_TIMEOUT_SECONDS` (opcional, padrão `240`) para aumentar o tempo de espera do login manual.
- `LINKEDIN_CHECKPOINT_TIMEOUT_SECONDS` (opcional, padrão `120`) para esperar liberação de checkpoint/captcha após login.

> Quando `LINKEDIN_MANUAL_LOGIN=true`, as variáveis `LINKEDIN_USER` e `LINKEDIN_PASSWORD` não são utilizadas.

## 4) Execução

No Linux/macOS:

```bash
export LOOPBACK_URL="http://localhost:3000/api"
export LINKEDIN_MANUAL_LOGIN="true"
export LINKEDIN_HEADLESS="false"
export LINKEDIN_MANUAL_LOGIN_TIMEOUT_SECONDS="300"

mvn -f fontes-estruturado/principal/obtemoportunidadelinkedin/pom.xml exec:java \
  -Dexec.mainClass=gerador.obtemoportunidadelinkedin.app.ObtemOportunidadeLinkedin
```

## 5) Fluxo esperado de login manual

1. O Selenium abre `https://www.linkedin.com/login`.
2. Você faz login manualmente (usuário/senha, 2FA e captcha se necessário).
3. O bot detecta sessão autenticada e segue para a aba de vagas.
4. O processo coleta as oportunidades e envia para o Loopback.

## 6) Dicas de operação

- Se a janela não abrir, confirme `LINKEDIN_HEADLESS=false`.
- Se o tempo de login expirar, aumente `LINKEDIN_MANUAL_LOGIN_TIMEOUT_SECONDS`.
- Se houver bloqueio/checkpoint, conclua manualmente e aguarde o bot continuar.
- Em servidor sem interface gráfica, prefira Selenium remoto (`SELENIUM_REMOTE_URL`) com VNC para fazer o login manual.

## 7) Modo automático (opcional)

Caso deseje login por credenciais (sem intervenção manual), não defina `LINKEDIN_MANUAL_LOGIN` e configure:

- `LINKEDIN_USER`
- `LINKEDIN_PASSWORD`

Ainda assim, pode ser necessário resolver checkpoint/captcha manualmente.
