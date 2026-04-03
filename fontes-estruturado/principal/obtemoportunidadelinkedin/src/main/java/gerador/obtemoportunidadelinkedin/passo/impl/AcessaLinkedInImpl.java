package gerador.obtemoportunidadelinkedin.passo.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.time.Instant;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import br.com.gersis.loopback.modelo.OportunidadeLinkedin;
import br.com.gersis.loopback.modelo.PalavraRaiz;
import gerador.obtemoportunidadelinkedin.passo.AcessaLinkedIn;



public class AcessaLinkedInImpl extends AcessaLinkedIn {

	WebDriver driver = null;
	String chromeBinaryUtilizado = null;
	
	/*
	 * Trocar o Driver do Chrome:
	 * 
	 * /usr/local/bin/
	 * https://googlechromelabs.github.io/chrome-for-testing/
	 * 	https://storage.googleapis.com/chrome-for-testing-public/137.0.7151.119/win64/chromedriver-win64.zip
	 */
	
	
	@Override
	protected boolean executaCustom(PalavraRaiz palavraPesquisaCorrente) {
		ChromeOptions options = new ChromeOptions();
		String remoteUrl = obtemTextoEnv("SELENIUM_REMOTE_URL");
		boolean usandoSeleniumRemoto = remoteUrl != null && !remoteUrl.trim().isEmpty();
		boolean headless = obtemBooleanEnv("LINKEDIN_HEADLESS", true);
		if (headless) {
			options.addArguments("--headless");
		}
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		String userDataDir = obtemTextoEnv("LINKEDIN_CHROME_USER_DATA_DIR");
		if (userDataDir != null) {
			options.addArguments("--user-data-dir=" + userDataDir);
		}
		String profileDirectory = obtemTextoEnv("LINKEDIN_CHROME_PROFILE");
		if (profileDirectory != null) {
			options.addArguments("--profile-directory=" + profileDirectory);
		}
		String chromeBinaryPath = null;
		if (!usandoSeleniumRemoto) {
			chromeBinaryPath = obtemChromeBinaryPath();
			this.chromeBinaryUtilizado = chromeBinaryPath;
			if (chromeBinaryPath != null) {
				options.setBinary(chromeBinaryPath);
			}
		} else {
			this.chromeBinaryUtilizado = null;
		}
		System.out.println("[INFO] LinkedIn login config: headless=" + headless
				+ ", userDataDir=" + (userDataDir != null ? "definido" : "nao definido")
				+ ", profile=" + (profileDirectory != null ? profileDirectory : "default")
				+ ", seleniumRemoto=" + (usandoSeleniumRemoto ? remoteUrl : "nao") + ".");

		// Inicializar o navegador
		driver = criaWebDriver(options, remoteUrl);

        try {
            // Primeiro tenta usar sessão já autenticada no profile do Chrome.
            driver.get("https://www.linkedin.com/jobs");
            logEstadoPagina("apos abrir jobs");

			if (!loginConcluido()) {
				driver.get("https://www.linkedin.com/login");
				logEstadoPagina("apos abrir login");
				realizaLoginSeNecessario();
				garanteLoginSemCheckpoint();
			} else {
				System.out.println("[INFO] Sessao LinkedIn ativa detectada. Prosseguindo sem preencher credenciais.");
			}

            // Esperar até que a página principal seja carregada
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Garantir navegação para a página de busca de vagas
            driver.get("https://www.linkedin.com/jobs");
            logEstadoPagina("apos abrir pagina de vagas");

            // Inserir termo de pesquisa e buscar
            WebElement searchBox = aguardaCampoBuscaPalavraChave();
            try {
            	searchBox.clear();
            } catch (WebDriverException e) {
            	// Alguns campos do LinkedIn nao permitem clear, entao seguimos com o sendKeys direto
            }
            searchBox.sendKeys(palavraPesquisaCorrente.getPalavra());
            searchBox.sendKeys(Keys.RETURN);

            // Esperar resultados de pesquisa
            TimeUnit.SECONDS.sleep(5);

            // Coletar descrições de vagas
            

            this.saidaListaOportunidade = new ArrayList<OportunidadeLinkedin>();
            adicionaItens(palavraPesquisaCorrente);

			for (int pagina = 2; pagina <= 15; pagina++) {
				// Localiza o botão pelo atributo aria-label usando XPath
				try {
					WebElement button = driver.findElement(By.xpath("//button[@aria-label='Página " + pagina + "']"));
					if (button != null) {
						button.click();
						TimeUnit.SECONDS.sleep(5);
						adicionaItens(palavraPesquisaCorrente);
					}
				} catch (NoSuchElementException e) {

				}

			}
            
           
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Fechar o navegador
            driver.quit();
        }
        
	}

	private void realizaLoginSeNecessario() throws InterruptedException {
		boolean loginManual = obtemBooleanEnv("LINKEDIN_MANUAL_LOGIN", false);
		if (loginManual) {
			aguardaLoginManual();
			return;
		}

		String linkedinUser = obtemTextoEnv("LINKEDIN_USER");
		String linkedinPassword = obtemTextoEnv("LINKEDIN_PASSWORD");
		boolean temCredenciais = linkedinUser != null && !linkedinUser.trim().isEmpty()
				&& linkedinPassword != null && !linkedinPassword.trim().isEmpty();

		if (!temCredenciais) {
			throw new IllegalStateException("Nao foi possivel autenticar no LinkedIn: sessao nao encontrada no profile e LINKEDIN_USER/LINKEDIN_PASSWORD nao foram informados.");
		}

		WebElement emailField = driver.findElement(By.id("username"));
		emailField.sendKeys(linkedinUser);

		WebElement passwordField = driver.findElement(By.id("password"));
		passwordField.sendKeys(linkedinPassword);
		passwordField.sendKeys(Keys.RETURN);
		logEstadoPagina("apos enviar credenciais");
	}

	private void aguardaLoginManual() throws InterruptedException {
		int timeoutSegundos = obtemInteiroEnv("LINKEDIN_MANUAL_LOGIN_TIMEOUT_SECONDS", 240);
		long fimEspera = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutSegundos);
		System.out.println("[INFO] LINKEDIN_MANUAL_LOGIN=true. Realize o login manualmente na janela do Selenium.");
		while (System.currentTimeMillis() <= fimEspera) {
			if (loginConcluido()) {
				logEstadoPagina("login manual concluido");
				return;
			}
			long faltam = Math.max(0, (fimEspera - System.currentTimeMillis()) / 1000);
			System.out.println("[INFO] Aguardando login manual. Tempo restante: " + faltam + "s.");
			TimeUnit.SECONDS.sleep(5);
		}
		throw new IllegalStateException("Tempo esgotado aguardando login manual no LinkedIn. Aumente LINKEDIN_MANUAL_LOGIN_TIMEOUT_SECONDS ou conclua o login mais rapido.");
	}

	private boolean loginConcluido() {
		try {
			String currentUrl = valorSeguro(driver.getCurrentUrl()).toLowerCase(Locale.ROOT);
			if (currentUrl.contains("/feed") || currentUrl.contains("/jobs") || currentUrl.contains("/mynetwork")) {
				return true;
			}
			return !driver.findElements(By.id("username")).isEmpty() ? false : true;
		} catch (Exception e) {
			return false;
		}
	}

	private WebElement aguardaCampoBuscaPalavraChave() {
		List<By> seletores = Arrays.asList(
			By.name("keywords"),
			By.id("job-search-bar-keywords"),
			By.cssSelector("input[id^='jobs-search-box-keyword-id']"),
			By.cssSelector("input[data-tracking-control-name*='keywords']"),
			By.cssSelector("input[aria-label*='palavra-chave']"),
			By.cssSelector("input[aria-label*='palavras-chave']"),
			By.cssSelector("input[aria-label*='palavra chave']"),
			By.cssSelector("input[aria-label*='job titles']"),
			By.cssSelector("input[aria-label*='keywords']"),
			By.cssSelector("input[placeholder*='palavra-chave']"),
			By.cssSelector("input[placeholder*='palavras-chave']"),
			By.cssSelector("input[placeholder*='palavra chave']"),
			By.cssSelector("input[placeholder*='job titles']"),
			By.cssSelector("input[placeholder*='keyword']"),
			By.className("jobs-search-box__text-input")
		);

		WebDriverWait wait = new WebDriverWait(driver, 20);
		List<String> seletoresTestados = new ArrayList<String>();
		for (By seletor : seletores) {
			seletoresTestados.add(seletor.toString());
			try {
				System.out.println("[DEBUG] Tentando localizar campo de busca com seletor: " + seletor);
				WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(seletor));
				if (elemento != null) {
					System.out.println("[DEBUG] Campo de busca localizado com seletor: " + seletor);
					System.out.println("[DEBUG] Estado do elemento: displayed=" + elemento.isDisplayed() + ", enabled=" + elemento.isEnabled());
					return elemento;
				}
			} catch (TimeoutException | NoSuchElementException e) {
				System.out.println("[DEBUG] Seletor nao encontrou elemento visivel: " + seletor + " -> " + e.getClass().getSimpleName());
				// tenta o proximo seletor
			}
		}

		logDiagnosticoCampoBusca();

		throw new NoSuchElementException("Nao foi possivel localizar o campo de busca de vagas do LinkedIn. Seletores testados: " + String.join(" | ", seletoresTestados));
	}

	private void logDiagnosticoCampoBusca() {
		try {
			logEstadoPagina("falha ao localizar campo de busca");
			List<WebElement> todosInputs = driver.findElements(By.cssSelector("input"));
			System.out.println("[DEBUG] Total de inputs encontrados na pagina: " + todosInputs.size());
			int limite = Math.min(10, todosInputs.size());
			for (int i = 0; i < limite; i++) {
				WebElement input = todosInputs.get(i);
				String id = valorSeguro(input.getAttribute("id"));
				String name = valorSeguro(input.getAttribute("name"));
				String ariaLabel = valorSeguro(input.getAttribute("aria-label"));
				String placeholder = valorSeguro(input.getAttribute("placeholder"));
				String clazz = valorSeguro(input.getAttribute("class"));
				System.out.println("[DEBUG] Input[" + i + "] id='" + id + "' name='" + name + "' aria-label='" + ariaLabel + "' placeholder='" + placeholder + "' class='" + clazz + "'");
			}

			String pageSource = driver.getPageSource();
			String pageSourceNormalizada = pageSource == null ? "" : pageSource.toLowerCase(Locale.ROOT);
			if (pageSourceNormalizada.contains("captcha") || pageSourceNormalizada.contains("challenge")) {
				System.out.println("[WARN] Possivel bloqueio/captcha detectado no HTML da pagina.");
			}
			if (pageSourceNormalizada.contains("checkpoint") || pageSourceNormalizada.contains("security verification")) {
				System.out.println("[WARN] Possivel checkpoint/verificacao de seguranca detectado no HTML da pagina.");
			}
		} catch (Exception e) {
			System.out.println("[WARN] Falha ao coletar diagnostico do campo de busca: " + e.getMessage());
		}
	}

	private void garanteLoginSemCheckpoint() throws InterruptedException {
		int timeoutSegundos = obtemInteiroEnv("LINKEDIN_CHECKPOINT_TIMEOUT_SECONDS", 120);
		long fimEspera = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutSegundos);
		while (System.currentTimeMillis() <= fimEspera) {
			if (!paginaEmCheckpointOuBloqueio()) {
				return;
			}

			long faltam = Math.max(0, (fimEspera - System.currentTimeMillis()) / 1000);
			System.out.println("[WARN] Login com desafio de seguranca/captcha detectado. "
					+ "Conclua manualmente no navegador. Aguardando liberacao da sessao por ate " + faltam + "s.");
			TimeUnit.SECONDS.sleep(5);
			logEstadoPagina("aguardando liberacao de checkpoint");
		}

		throw new IllegalStateException("LinkedIn bloqueou o login em checkpoint/captcha e a sessao nao foi liberada em "
				+ timeoutSegundos + "s. Resolva manualmente a verificacao de seguranca, reduza a frequencia de automacao "
				+ "ou reutilize cookies/sessao previamente validada.");
	}

	private boolean paginaEmCheckpointOuBloqueio() {
		String currentUrl = "";
		String title = "";
		String pageSource = "";
		try {
			currentUrl = valorSeguro(driver.getCurrentUrl()).toLowerCase(Locale.ROOT);
			title = valorSeguro(driver.getTitle()).toLowerCase(Locale.ROOT);
			pageSource = valorSeguro(driver.getPageSource()).toLowerCase(Locale.ROOT);
		} catch (Exception e) {
			return true;
		}

		return currentUrl.contains("checkpoint")
				|| currentUrl.contains("challenge")
				|| title.contains("security verification")
				|| pageSource.contains("captcha")
				|| pageSource.contains("checkpoint")
				|| pageSource.contains("challenge");
	}

	private int obtemInteiroEnv(String nomeVariavel, int valorPadrao) {
		String valor = System.getenv(nomeVariavel);
		if (valor == null || valor.trim().isEmpty()) {
			return valorPadrao;
		}
		try {
			return Integer.parseInt(valor.trim());
		} catch (NumberFormatException e) {
			System.out.println("[WARN] Valor invalido para " + nomeVariavel + "='" + valor + "'. Usando padrao " + valorPadrao + "s.");
			return valorPadrao;
		}
	}

	private boolean obtemBooleanEnv(String nomeVariavel, boolean valorPadrao) {
		String valor = System.getenv(nomeVariavel);
		if (valor == null || valor.trim().isEmpty()) {
			return valorPadrao;
		}
		if ("true".equalsIgnoreCase(valor.trim()) || "1".equals(valor.trim()) || "yes".equalsIgnoreCase(valor.trim())) {
			return true;
		}
		if ("false".equalsIgnoreCase(valor.trim()) || "0".equals(valor.trim()) || "no".equalsIgnoreCase(valor.trim())) {
			return false;
		}
		System.out.println("[WARN] Valor invalido para " + nomeVariavel + "='" + valor + "'. Usando padrao=" + valorPadrao + ".");
		return valorPadrao;
	}

	private String obtemTextoEnv(String nomeVariavel) {
		String valor = System.getenv(nomeVariavel);
		if (valor == null) {
			return null;
		}
		String trimmed = valor.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}

	private String obtemTextoEnvObrigatorio(String nomeVariavel) {
		String valor = obtemTextoEnv(nomeVariavel);
		if (valor == null) {
			throw new IllegalStateException("Variavel de ambiente obrigatoria ausente: " + nomeVariavel
					+ ". Configure os secrets/envs antes de executar o robô.");
		}
		return valor;
	}

	private void logEstadoPagina(String contexto) {
		try {
			String currentUrl = valorSeguro(driver.getCurrentUrl());
			String title = valorSeguro(driver.getTitle());
			System.out.println("[DEBUG] Estado da pagina (" + contexto + "): url='" + currentUrl + "' title='" + title + "'");
		} catch (Exception e) {
			System.out.println("[WARN] Nao foi possivel capturar URL/titulo da pagina em '" + contexto + "': " + e.getMessage());
		}
	}

	private String valorSeguro(String valor) {
		if (valor == null) {
			return "";
		}
		return valor.replace("\n", " ").replace("\r", " ").trim();
	}

	private WebDriver criaWebDriver(ChromeOptions options, String remoteUrl) {
		if (remoteUrl != null && !remoteUrl.trim().isEmpty()) {
			return criaWebDriverRemotoComFallback(options, remoteUrl);
		}

		String chromeDriverPath = obtemChromeDriverPath();
		if (chromeDriverPath != null && !chromeDriverPath.trim().isEmpty()) {
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			System.out.println("[INFO] Usando chromedriver local: " + chromeDriverPath);
		} else {
			System.clearProperty("webdriver.chrome.driver");
			System.out.println("[INFO] Chromedriver local nao encontrado. Tentando download automatico via WebDriverManager.");
			configuraWebDriverManagerComVersaoDoNavegador();
		}

		try {
			return new ChromeDriver(options);
		} catch (SessionNotCreatedException e) {
			String versaoCompletaBrowser = extraiVersaoCompletaBrowser(e.getMessage());
			String majorVersaoBrowser = extraiMajorVersaoBrowser(e.getMessage());
			if (majorVersaoBrowser != null && !majorVersaoBrowser.isEmpty()) {
				System.out.println("[WARN] Incompatibilidade de versao entre Chrome e ChromeDriver detectada. Tentando ChromeDriver " + majorVersaoBrowser + ".x");
				System.clearProperty("webdriver.chrome.driver");
				WebDriverManager.chromedriver().driverVersion(majorVersaoBrowser).setup();
				return new ChromeDriver(options);
			} else if (versaoCompletaBrowser != null && !versaoCompletaBrowser.isEmpty()) {
				System.out.println("[WARN] Incompatibilidade de versao entre Chrome e ChromeDriver detectada. Tentando ChromeDriver para browser " + versaoCompletaBrowser);
				System.clearProperty("webdriver.chrome.driver");
				WebDriverManager.chromedriver().browserVersion(versaoCompletaBrowser).setup();
				return new ChromeDriver(options);
			}
			throw e;
		} catch (WebDriverException e) {
			String mensagem = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
			if (mensagem.contains("timed out waiting for driver server to start") || mensagem.contains("localhost")) {
				throw new RuntimeException(
					"Falha ao subir ChromeDriver local. O endereco localhost exibido no stacktrace e interno do processo do driver "
					+ "(nao e a sua API Loopback). Em container, prefira instalar as libs do Chrome/Chromedriver "
					+ "(ex.: libglib2.0-0) ou configurar SELENIUM_REMOTE_URL para usar um Selenium remoto.",
					e);
			}
			throw e;
		}
	}

	private WebDriver criaWebDriverRemotoComFallback(ChromeOptions options, String remoteUrl) {
		try {
			System.out.println("[INFO] Usando Selenium remoto em: " + remoteUrl);
			return new RemoteWebDriver(new URL(remoteUrl), options);
		} catch (Exception primeiraFalha) {
			if (!deveTentarFallbackPerfilRemoto(options)) {
				throw new RuntimeException("Nao foi possivel conectar ao Selenium remoto em '" + remoteUrl + "'.", primeiraFalha);
			}
			String perfilTemporario = "/tmp/linkedin-profile-" + Instant.now().toEpochMilli();
			ChromeOptions fallbackOptions = new ChromeOptions();
			fallbackOptions.merge(options);
			fallbackOptions.addArguments("--user-data-dir=" + perfilTemporario);
			fallbackOptions.addArguments("--profile-directory=Default");
			System.out.println("[WARN] Falha ao iniciar sessao remota com perfil persistente. "
					+ "Tentando profile temporario para evitar lock de sessao: " + perfilTemporario);
			try {
				return new RemoteWebDriver(new URL(remoteUrl), fallbackOptions);
			} catch (Exception fallbackFalha) {
				throw new RuntimeException("Nao foi possivel conectar ao Selenium remoto em '" + remoteUrl
						+ "' nem com fallback de profile temporario.", fallbackFalha);
			}
		}
	}

	private boolean deveTentarFallbackPerfilRemoto(ChromeOptions options) {
		if (options == null) {
			return false;
		}
		Object googOptions = options.asMap().get("goog:chromeOptions");
		if (!(googOptions instanceof Map<?, ?>)) {
			return false;
		}
		Object args = ((Map<?, ?>) googOptions).get("args");
		if (!(args instanceof List<?>)) {
			return false;
		}
		for (Object item : (List<?>) args) {
			if (item instanceof String && ((String) item).startsWith("--user-data-dir=")) {
				return true;
			}
		}
		return false;
	}

	private void configuraWebDriverManagerComVersaoDoNavegador() {
		String versaoCompleta = obtemVersaoCompletaChrome(this.chromeBinaryUtilizado);
		if (versaoCompleta != null && !versaoCompleta.isEmpty()) {
			String major = versaoCompleta.split("\\.")[0];
			System.out.println("[INFO] Browser detectado na versao " + versaoCompleta + " (major " + major + "). Baixando ChromeDriver compativel.");
			WebDriverManager.chromedriver().driverVersion(major).setup();
			return;
		}
		WebDriverManager.chromedriver().setup();
	}

	private String obtemVersaoCompletaChrome(String chromeBinaryPath) {
		if (chromeBinaryPath == null || chromeBinaryPath.trim().isEmpty()) {
			return null;
		}
		try {
			Process processo = new ProcessBuilder(chromeBinaryPath, "--version").start();
			String saida = leTexto(processo.getInputStream());
			String erro = leTexto(processo.getErrorStream());
			processo.waitFor(3, TimeUnit.SECONDS);
			String texto = (saida + "\n" + erro);
			Matcher matcher = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)").matcher(texto);
			if (matcher.find()) {
				return matcher.group(1);
			}
			matcher = Pattern.compile("(\\d+\\.\\d+\\.\\d+)").matcher(texto);
			if (matcher.find()) {
				return matcher.group(1);
			}
			matcher = Pattern.compile("(\\d+)\\.").matcher(texto);
			if (matcher.find()) {
				return matcher.group(1);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private String obtemMajorVersaoChrome(String chromeBinaryPath) {
		String versaoCompleta = obtemVersaoCompletaChrome(chromeBinaryPath);
		if (versaoCompleta == null || versaoCompleta.trim().isEmpty()) {
			return null;
		}
		return versaoCompleta.split("\\.")[0];
	}

	private String extraiVersaoCompletaBrowser(String mensagemErro) {
		if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
			return null;
		}
		Matcher matcher = Pattern.compile("Current browser version is ([0-9.]+)").matcher(mensagemErro);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private String extraiMajorVersaoBrowser(String mensagemErro) {
		String versaoCompleta = extraiVersaoCompletaBrowser(mensagemErro);
		if (versaoCompleta == null || versaoCompleta.trim().isEmpty()) {
			return null;
		}
		return versaoCompleta.split("\\.")[0];
	}

	private String obtemChromeDriverPath() {
		String envSkip = System.getenv("SKIP_CHROMEDRIVER_DISCOVERY");
		if ("true".equalsIgnoreCase(envSkip)) {
			return null;
		}

		String envPath = System.getenv("CHROMEDRIVER_PATH");
		if (arquivoExecutavelValido(envPath)) {
			return envPath;
		}

		String pathExecutavel = obtemExecutavelNoPath("chromedriver");
		if (arquivoExecutavelValido(pathExecutavel)) {
			return pathExecutavel;
		}

		String[] candidatos = {
			"/usr/local/bin/chromedriver",
			"/usr/bin/chromedriver",
			"/usr/lib/chromium-browser/chromedriver",
			"/usr/lib/chromium/chromedriver",
			"/opt/chromedriver/chromedriver"
		};
		for (String caminho : candidatos) {
			if (arquivoExecutavelValido(caminho)) {
				return caminho;
			}
		}
		return null;
	}

	private String obtemExecutavelNoPath(String comando) {
		try {
			Process processo = new ProcessBuilder("/bin/sh", "-lc", "command -v " + comando).start();
			BufferedReader leitor = new BufferedReader(new InputStreamReader(processo.getInputStream(), StandardCharsets.UTF_8));
			String linha = leitor.readLine();
			processo.waitFor(2, TimeUnit.SECONDS);
			if (linha == null) {
				return null;
			}
			return linha.trim();
		} catch (Exception e) {
			return null;
		}
	}

	private String obtemChromeBinaryPath() {
		String envPath = System.getenv("CHROME_BINARY");
		if (arquivoExecutavelSimples(envPath)) {
			return envPath;
		}
		String[] candidatos = {
			"/usr/bin/google-chrome",
			"/usr/bin/chromium",
			"/usr/bin/chromium-browser"
		};
		for (String caminho : candidatos) {
			if (arquivoExecutavelSimples(caminho)) {
				return caminho;
			}
		}
		return null;
	}

	private boolean arquivoExecutavelValido(String caminho) {
		if (!arquivoExecutavelSimples(caminho)) {
			return false;
		}
		if (isSnapWrapper(caminho)) {
			return false;
		}
		if (!comandoDriverValido(caminho)) {
			return false;
		}
		return true;
	}


	private boolean comandoDriverValido(String caminho) {
		try {
			Process processo = new ProcessBuilder(caminho, "--version").start();
			String saida = leTexto(processo.getInputStream());
			String erro = leTexto(processo.getErrorStream());
			processo.waitFor(3, TimeUnit.SECONDS);
			if (processo.exitValue() != 0) {
				return false;
			}
			String texto = (saida + "\n" + erro).toLowerCase();
			return texto.contains("chromedriver") && !texto.contains("snap install chromium") && !texto.contains("requires the chromium snap");
		} catch (Exception e) {
			return false;
		}
	}

	private String leTexto(java.io.InputStream in) {
		try (BufferedReader leitor = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			StringBuilder sb = new StringBuilder();
			String linha;
			while ((linha = leitor.readLine()) != null) {
				sb.append(linha).append('\n');
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}
	private boolean arquivoExecutavelSimples(String caminho) {
		if (caminho == null || caminho.trim().isEmpty()) {
			return false;
		}
		return Files.exists(Paths.get(caminho)) && Files.isExecutable(Paths.get(caminho));
	}

	private boolean isSnapWrapper(String caminho) {
		try {
			byte[] conteudo = Files.readAllBytes(Paths.get(caminho));
			String texto = new String(conteudo, StandardCharsets.UTF_8);
			return texto.contains("snap install chromium") || texto.contains("requires the chromium snap");
		} catch (Exception e) {
			return false;
		}
	}

	
	
	private void adicionaItens(PalavraRaiz palavraRaiz) throws InterruptedException {
		List<WebElement> jobs = obtemListaCardsVagas();
		for (WebElement job : jobs) {
			OportunidadeLinkedin novo = new OportunidadeLinkedin();
			if (!clicaCardVaga(job)) {
				continue;
			}
			TimeUnit.SECONDS.sleep(2);

			try {
				String descricao = obtemDescricaoVaga();
				String jobTitleText = obtemTextoPrimeiroSeletor(Arrays.asList(
					By.cssSelector(".job-details-jobs-unified-top-card__job-title h1"),
					By.cssSelector(".jobs-unified-top-card__job-title h1"),
					By.cssSelector(".jobs-details-top-card__job-title h1"),
					By.cssSelector("h1.t-24.t-bold.inline"),
					By.cssSelector("h1[data-test-job-title]"),
					By.cssSelector(".job-card-list__title")
				));

				String companyNameText = obtemTextoPrimeiroSeletor(Arrays.asList(
					By.cssSelector(".job-details-jobs-unified-top-card__company-name a"),
					By.cssSelector(".jobs-unified-top-card__company-name a"),
					By.cssSelector(".jobs-details-top-card__company-url"),
					By.cssSelector(".job-details-jobs-unified-top-card__company-name"),
					By.cssSelector(".jobs-unified-top-card__company-name"),
					By.cssSelector(".job-card-container__primary-description")
				));

				String baseUrl = obtemUrlVagaAtual();
				List<String> insights = obtemInsightsVaga();
				String diasText = escolheInsight(insights, Arrays.asList("há ", " ha ", "ago", "dia", "dias", "week", "weeks", "month", "months"));
				String candidaturasText = escolheInsight(insights, Arrays.asList("candid", "applicant", "candidate"));
				String modelo = escolheInsight(insights, Arrays.asList("remoto", "remote", "híbrido", "hibrido", "hybrid", "presencial", "on-site", "onsite"));

				if (candidaturasText.isEmpty()) {
					candidaturasText = "0";
				}

				System.out.println("Job Link URL: " + baseUrl);
				System.out.println("Job Title: " + jobTitleText);
				System.out.println("Company Name: " + companyNameText);
				System.out.println("Tempo: " + diasText);
				System.out.println("Candidaturas: " + candidaturasText);
				System.out.println("Modelo: " + modelo);

				novo.setDescricao(descricao);
				novo.setVolume(candidaturasText);
				novo.setTempo(diasText);
				novo.setTitulo(jobTitleText);
				novo.setUrl(baseUrl);
				novo.setEmpresa(companyNameText);
				novo.setPalavraRaizId("" + palavraRaiz.getIdInteger());
				novo.setModelo(modelo);

				saidaListaOportunidade.add(novo);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Não foi possível extrair a descrição da vaga.");
			}
		}
	}

	private List<WebElement> obtemListaCardsVagas() {
		List<By> seletoresCards = Arrays.asList(
			By.cssSelector(".job-card-container"),
			By.cssSelector(".jobs-search-results-list__list-item"),
			By.cssSelector("li.scaffold-layout__list-item")
		);
		Set<WebElement> resultado = new LinkedHashSet<WebElement>();
		for (By seletor : seletoresCards) {
			try {
				resultado.addAll(driver.findElements(seletor));
			} catch (Exception e) {
				// ignora seletor inválido no layout atual
			}
		}
		return new ArrayList<WebElement>(resultado);
	}

	private boolean clicaCardVaga(WebElement job) {
		try {
			WebElement clicavel = null;
			List<By> seletoresInternos = Arrays.asList(
				By.cssSelector("a.job-card-container__link"),
				By.cssSelector("a.job-card-list__title"),
				By.cssSelector("a[href*='/jobs/view/']"),
				By.cssSelector("button")
			);
			for (By seletor : seletoresInternos) {
				try {
					clicavel = job.findElement(seletor);
					if (clicavel != null) {
						break;
					}
				} catch (NoSuchElementException e) {
					// tenta o próximo
				}
			}
			if (clicavel != null) {
				clicavel.click();
			} else {
				job.click();
			}
			return true;
		} catch (Exception e) {
			System.out.println("[WARN] Não foi possível clicar no card da vaga: " + e.getMessage());
			return false;
		}
	}

	private String obtemDescricaoVaga() {
		String descricao = obtemTextoPrimeiroSeletor(Arrays.asList(
			By.id("job-details"),
			By.cssSelector(".jobs-description__content"),
			By.cssSelector(".jobs-description-content__text"),
			By.cssSelector(".jobs-box__html-content"),
			By.cssSelector(".jobs-description")
		));
		System.out.println(descricao);
		System.out.println();
		return descricao;
	}

	private String obtemUrlVagaAtual() {
		List<By> seletoresLink = Arrays.asList(
			By.cssSelector(".job-details-jobs-unified-top-card__job-title a"),
			By.cssSelector(".jobs-unified-top-card__job-title a"),
			By.cssSelector(".jobs-details-top-card__job-title a"),
			By.cssSelector("a[data-test-job-title]"),
			By.cssSelector("a[href*='/jobs/view/']")
		);
		for (By seletor : seletoresLink) {
			try {
				WebElement jobLinkElement = driver.findElement(seletor);
				String jobLinkUrl = valorSeguro(jobLinkElement.getAttribute("href"));
				if (!jobLinkUrl.isEmpty()) {
					return jobLinkUrl.split("\\?")[0];
				}
			} catch (NoSuchElementException e) {
				// tenta próximo seletor
			}
		}
		String currentUrl = valorSeguro(driver.getCurrentUrl());
		Matcher matcher = Pattern.compile("/jobs/view/(\\d+)").matcher(currentUrl);
		if (matcher.find()) {
			return "https://www.linkedin.com/jobs/view/" + matcher.group(1);
		}
		return currentUrl;
	}

	private List<String> obtemInsightsVaga() {
		List<String> insights = new ArrayList<String>();
		List<By> seletoresInsights = Arrays.asList(
			By.cssSelector(".job-details-jobs-unified-top-card__primary-description-container .tvm__text"),
			By.cssSelector(".jobs-unified-top-card__primary-description-container .tvm__text"),
			By.cssSelector(".jobs-details-top-card__primary-description-container .tvm__text"),
			By.cssSelector(".job-details-jobs-unified-top-card__job-insight"),
			By.cssSelector(".jobs-unified-top-card__job-insight")
		);
		for (By seletor : seletoresInsights) {
			try {
				for (WebElement item : driver.findElements(seletor)) {
					String texto = valorSeguro(item.getText());
					if (!texto.isEmpty() && !insights.contains(texto)) {
						insights.add(texto);
					}
				}
			} catch (Exception e) {
				// ignora e segue com os demais seletores
			}
		}
		return insights;
	}

	private String escolheInsight(List<String> insights, List<String> termos) {
		for (String item : insights) {
			String normalized = item.toLowerCase(Locale.ROOT);
			for (String termo : termos) {
				if (normalized.contains(termo)) {
					return item;
				}
			}
		}
		return "";
	}

	private String obtemTextoPrimeiroSeletor(List<By> seletores) {
		for (By seletor : seletores) {
			try {
				WebElement elemento = driver.findElement(seletor);
				String texto = valorSeguro(elemento.getText());
				if (!texto.isEmpty()) {
					return texto;
				}
			} catch (NoSuchElementException e) {
				// tenta próximo seletor
			}
		}
		return "";
	}


}
