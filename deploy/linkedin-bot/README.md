# Deploy do robô do LinkedIn

Esta pasta contém os arquivos de container e deploy contínuo do módulo `obtemoportunidadelinkedin`.

## O que foi configurado

- `Dockerfile`: build do módulo Maven e imagem de runtime baseada em Playwright (browser já incluído).
- `docker-compose.yml`: sobe o container `curriculosis-linkedin-bot` usando variáveis de ambiente.
- Workflow `.github/workflows/deploy-linkedin-bot.yml`: builda imagem no GHCR e faz deploy via SSH no host `191.252.102.54`.

## Secrets necessários no GitHub

Configure no repositório (`Settings` → `Secrets and variables` → `Actions`):

- `DEPLOY_SSH_USER`: usuário SSH do host.
- `DEPLOY_SSH_KEY`: chave privada SSH para deploy.
- `DEPLOY_SSH_PORT`: porta SSH (opcional, padrão 22).
- `LOOPBACK_URL`: URL da API Loopback usada pelo robô.
- `LINKEDIN_USER`: login do LinkedIn.
- `LINKEDIN_PASSWORD`: senha do LinkedIn.
- `LINKEDIN_HEADLESS`: executa navegador em headless (`true` padrão). Para primeiro login/manual, use `false`.
- `LINKEDIN_CHROME_USER_DATA_DIR`: diretório de perfil do Chrome para persistir sessão/cookies (padrão no compose: `/data/chrome-profile`).
- `LINKEDIN_CHROME_PROFILE`: nome do profile dentro do `user-data-dir` (padrão: `Default`).
- `LINKEDIN_CHECKPOINT_TIMEOUT_SECONDS`: tempo máximo aguardando liberação manual de checkpoint/captcha (padrão recomendado: `300`).
- `SELENIUM_REMOTE_URL`: URL do Selenium remoto (ex.: `http://selenium:4444/wd/hub`) quando quiser login manual com VNC.

## Fluxo de deploy

1. Push na `main` (ou execução manual do workflow).
2. Action gera e publica imagem `ghcr.io/<owner>/curriculosis-linkedin-bot:<sha>`.
3. Action copia `docker-compose.yml` para `/opt/curriculosis/linkedin-bot` no host.
4. Action cria/atualiza `.env` no host e executa `docker compose pull && docker compose up -d`.

## Como o usuário faz o login manual na prática

Para ficar operacional de verdade, use o serviço `selenium` com noVNC (tela no navegador):

1. Configure o `.env` com:

```env
LINKEDIN_HEADLESS=false
LINKEDIN_CHROME_USER_DATA_DIR=/data/chrome-profile
LINKEDIN_CHROME_PROFILE=Default
LINKEDIN_CHECKPOINT_TIMEOUT_SECONDS=300
SELENIUM_REMOTE_URL=http://selenium:4444/wd/hub
```

2. Suba os containers com perfil de login manual:

```bash
docker compose --profile manual-login up -d
```

3. Abra a tela do navegador remoto em `http://SEU_HOST:7900` (senha padrão do container Selenium: `secret`).
4. Na tela noVNC, conclua login + 2FA + captcha/checkpoint do LinkedIn até cair em Home/Jobs.
5. Pare o bot e ligue novamente em modo automático:

```bash
# opcional: voltar ao headless depois de validar sessão
export LINKEDIN_HEADLESS=true

docker compose up -d linkedin-bot
```

Como o `user-data-dir` fica em volume (`linkedin_chrome_profile`), os cookies/sessão validados continuam para as próximas execuções.

### Se a tela do noVNC ficar preta (apenas logo do Selenium)

Isso normalmente significa que **não existe sessão de navegador ativa** no Selenium (o container está de pé, mas nenhum teste abriu o Chrome ainda).

Checklist rápido:

1. Garanta que o bot está apontando para o Selenium e sem headless durante o login manual:

```bash
LINKEDIN_HEADLESS=false \
SELENIUM_REMOTE_URL=http://selenium:4444/wd/hub \
docker compose --profile manual-login up -d selenium linkedin-bot
```

2. Confira se as variáveis entraram no container do bot:

```bash
docker compose exec linkedin-bot env | grep -E 'LINKEDIN_HEADLESS|SELENIUM_REMOTE_URL'
```

Saída esperada:

```text
LINKEDIN_HEADLESS=false
SELENIUM_REMOTE_URL=http://selenium:4444/wd/hub
```

3. Verifique se existe sessão ativa no Selenium:

```bash
curl -s http://localhost:4444/status
```

No JSON, confirme `"ready": true`. Se não houver sessão após iniciar o bot, veja logs:

```bash
docker compose logs -f linkedin-bot selenium
```

4. Se ainda ficar preto, reinicie apenas os serviços de login manual:

```bash
docker compose --profile manual-login down
docker compose --profile manual-login up -d selenium linkedin-bot
```

> Dica: abra o noVNC **depois** que o `linkedin-bot` iniciar. Sem sessão ativa, a tela permanece preta mesmo com o Selenium saudável.

> Importante: isso **não faz bypass** de segurança do LinkedIn. Apenas habilita intervenção manual quando exigida e reaproveita a sessão autenticada.

## Onde executar os comandos de Git e PR

Os comandos `git remote add`, `git push` e criação de PR precisam ser executados **na pasta local do repositório** (onde existe a pasta `.git`).

Se você ainda não tem o projeto localmente, faça assim:

```bash
# 1) clonar o repositório no seu computador
git clone https://github.com/paulofor/CurriculoSis.git

# 2) entrar na pasta clonada
cd CurriculoSis

# 3) conferir branch e remoto
git branch --show-current
git remote -v

# 4) enviar sua branch de trabalho
git push -u origin work
```

Depois disso, abra o PR no GitHub (`Compare & pull request`) ou com GitHub CLI:

```bash
gh pr create --base main --head work --title "Add GitHub Actions deployment structure for LinkedIn bot module"
```

## Se não consegue fazer `git push` para o GitHub

Se aparecer erro de autenticação/permissão, normalmente é um destes casos:

### 1) Remote não configurado

```bash
git remote -v
git remote add origin https://github.com/paulofor/CurriculoSis.git
```

### 2) Branch sem upstream

```bash
git push -u origin work
```

### 3) Erro `Permission denied` / `Authentication failed`

Use uma destas opções:

- **HTTPS + Token (PAT)**
  1. GitHub → `Settings` → `Developer settings` → `Personal access tokens`.
  2. Gere token com escopo `repo`.
  3. Faça push via HTTPS e informe usuário + token quando solicitado.

```bash
git remote set-url origin https://github.com/paulofor/CurriculoSis.git
git push -u origin work
```

- **SSH (recomendado para uso contínuo)**

```bash
ssh-keygen -t ed25519 -C "seu-email@dominio.com"
cat ~/.ssh/id_ed25519.pub
# adicionar essa chave em GitHub -> Settings -> SSH and GPG keys
ssh -T git@github.com
git remote set-url origin git@github.com:paulofor/CurriculoSis.git
git push -u origin work
```

### 4) Sem permissão de escrita no repositório

Se o teste `ssh -T git@github.com` funcionar mas o push ainda falhar com `403`, sua conta não tem permissão no repo.
Nesse caso, peça acesso de escrita ao repositório `paulofor/CurriculoSis` ou trabalhe via fork.
