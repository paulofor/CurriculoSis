# Squad driver-sync

- Owner: ChatGPT Codex
- Foco: Alinhar versões do ChromeDriver/Chrome para destravar container do currículo LinkedIn.
- Risco: Médio (dependência do ambiente Playwright com versões não padronizadas).
- Custo estimado: Baixo (ajustes de código + testes locais sem infra extra).

## Checkpoint 1
- Data: 2026-03-02
- Ações: adicionada detecção da versão completa do navegador e fallback usando `WebDriverManager.browserVersion(...)` conforme documentação oficial.
- Progresso: Código ajustado, build local falhou apenas por dependências privadas ausentes; aguardando validação em ambiente do cliente.
