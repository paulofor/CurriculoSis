# Deploy do robô do LinkedIn

Esta pasta contém os arquivos de container e deploy contínuo do módulo `obtemoportunidadelinkedin`.

## O que foi configurado

- `Dockerfile`: build do módulo Maven e imagem de runtime com Chromium/ChromeDriver.
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

## Fluxo de deploy

1. Push na `main` (ou execução manual do workflow).
2. Action gera e publica imagem `ghcr.io/<owner>/curriculosis-linkedin-bot:<sha>`.
3. Action copia `docker-compose.yml` para `/opt/curriculosis/linkedin-bot` no host.
4. Action cria/atualiza `.env` no host e executa `docker compose pull && docker compose up -d`.

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
