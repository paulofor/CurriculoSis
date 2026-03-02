# Squad linkedin-search-box

- Owner: ChatGPT Codex
- Foco: Garantir que o gerador do currículo consiga preencher o campo de palavra-chave no LinkedIn Jobs mesmo com alterações de layout.
- Risco: Médio (LinkedIn altera o DOM com frequência e impõe bloqueios quando detecta automação).
- Custo estimado: Baixo (ajustes de Selenium + testes locais no módulo obtemoportunidadelinkedin).

## Checkpoint 1
- Data: 2026-03-02
- Ações: Diagnóstico do erro `jobs-search-box__text-input` inexistente, captura do HTML público do LinkedIn Jobs para mapear atributos estáveis (`name="keywords"`).
- Progresso: Em andamento — aguardando validação após ajuste do seletor e das esperas explícitas.

## Checkpoint 2
- Data: 2026-03-02
- Ações: Implementado fallback com múltiplos seletores e espera explícita via `WebDriverWait`, além de tentativa de compilação (`mvn -f fontes-estruturado/principal/obtemoportunidadelinkedin/pom.xml -DskipTests compile`) para validar a alteração.
- Progresso: Ajuste concluído; compilação local bloqueada por dependências proprietárias (`br.com.gersis:loopback` e `br.com.gersis:daobase`) indisponíveis no Maven Central.
