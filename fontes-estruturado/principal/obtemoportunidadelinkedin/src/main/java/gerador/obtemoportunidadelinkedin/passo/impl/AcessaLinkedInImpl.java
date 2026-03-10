package gerador.obtemoportunidadelinkedin.passo.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;

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
            // Acessar a página de login do LinkedIn
            driver.get("https://www.linkedin.com/login");
            logEstadoPagina("apos abrir login");

            // Fazer login
            WebElement emailField = driver.findElement(By.id("username"));
            String linkedinUser = obtemTextoEnvObrigatorio("LINKEDIN_USER");
            emailField.sendKeys(linkedinUser);

            WebElement passwordField = driver.findElement(By.id("password"));
            String linkedinPassword = obtemTextoEnvObrigatorio("LINKEDIN_PASSWORD");
            passwordField.sendKeys(linkedinPassword);
            passwordField.sendKeys(Keys.RETURN);
            logEstadoPagina("apos enviar credenciais");
			garanteLoginSemCheckpoint();

            // Esperar até que a página principal seja carregada
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Navegar para a página de busca de vagas
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
			try {
				System.out.println("[INFO] Usando Selenium remoto em: " + remoteUrl);
				return new RemoteWebDriver(new URL(remoteUrl), options);
			} catch (Exception e) {
				throw new RuntimeException("Nao foi possivel conectar ao Selenium remoto em '" + remoteUrl + "'.", e);
			}
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
			if (versaoCompletaBrowser != null && !versaoCompletaBrowser.isEmpty()) {
				String versaoLog = majorVersaoBrowser != null ? majorVersaoBrowser + ".x" : versaoCompletaBrowser;
				System.out.println("[WARN] Incompatibilidade de versao entre Chrome e ChromeDriver detectada. Tentando ChromeDriver " + versaoLog + " (browser " + versaoCompletaBrowser + ")");
				System.clearProperty("webdriver.chrome.driver");
				WebDriverManager.chromedriver().browserVersion(versaoCompletaBrowser).setup();
				return new ChromeDriver(options);
			} else if (majorVersaoBrowser != null && !majorVersaoBrowser.isEmpty()) {
				System.out.println("[WARN] Incompatibilidade de versao entre Chrome e ChromeDriver detectada. Tentando ChromeDriver " + majorVersaoBrowser + ".x");
				System.clearProperty("webdriver.chrome.driver");
				WebDriverManager.chromedriver().driverVersion(majorVersaoBrowser).setup();
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

	private void configuraWebDriverManagerComVersaoDoNavegador() {
		String versaoCompleta = obtemVersaoCompletaChrome(this.chromeBinaryUtilizado);
		if (versaoCompleta != null && !versaoCompleta.isEmpty()) {
			String major = versaoCompleta.split("\\.")[0];
			System.out.println("[INFO] Browser detectado na versao " + versaoCompleta + " (major " + major + "). Baixando ChromeDriver compativel.");
			WebDriverManager.chromedriver().browserVersion(versaoCompleta).setup();
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
    	List<WebElement> jobs = driver.findElements(By.className("job-card-container__link"));
    	 for (WebElement job : jobs) {
         	OportunidadeLinkedin novo = new OportunidadeLinkedin();
             job.click();
             TimeUnit.SECONDS.sleep(2);

             try {
                 WebElement description = driver.findElement(By.id("job-details"));
                 //System.out.println("Descrição da Vaga:");
                 System.out.println(description.getText());
                 novo.setDescricao(description.getText());
                 //System.out.println("----");
                 System.out.println();
                 
                 // Localiza o elemento <a> pela posição no DOM
                 WebElement jobLinkElement = driver.findElement(By.xpath("//div[contains(@class, 'job-details-jobs-unified-top-card__job-title')]//h1[@class='t-24 t-bold inline']/a"));
                 // Extrai o valor do atributo href
                 String jobLinkUrl = jobLinkElement.getAttribute("href");
                 String baseUrl = jobLinkUrl.split("\\?")[0];
                 System.out.println("Job Link URL: " + baseUrl);
                 
                 WebElement jobTitleElement = driver.findElement(By.xpath("//div[contains(@class, 'job-details-jobs-unified-top-card__job-title')]//h1[@class='t-24 t-bold inline']/a"));
                 String jobTitleText = jobTitleElement.getText();
                 System.out.println("Job Title: " + jobTitleText);
                 
                 // Localiza o elemento que contém "Capco" pela posição no DOM
                 WebElement companyNameElement = driver.findElement(By.xpath("//div[contains(@class, 'job-details-jobs-unified-top-card__company-name')]//a"));
                 String companyNameText = companyNameElement.getText();
                 System.out.println("Company Name: " + companyNameText);
                 
                 // Localiza o elemento que contém "há 3 dias" pela posição no DOM
                 WebElement diasElement = driver.findElement(By.xpath("(//div[contains(@class, 'job-details-jobs-unified-top-card__primary-description-container')]//span[@class='tvm__text tvm__text--low-emphasis'])[3]"));
                 String diasText = diasElement.getText();
                 System.out.println("Tempo: " + diasText);

                 // Localiza o elemento que contém "68 candidaturas" pela posição no DOM
                 String candidaturasText = "0";
                 try {
                	 WebElement candidaturasElement = driver.findElement(By.xpath("(//div[contains(@class, 'job-details-jobs-unified-top-card__primary-description-container')]//span[@class='tvm__text tvm__text--low-emphasis'])[5]"));
                	 candidaturasText = candidaturasElement.getText();
                 } catch (NoSuchElementException err) {
                	 System.out.println("Sem candidatoras");
                 }
                 
                 String modelo = "";
                 try {
                     WebElement remotoElement = driver.findElement(By.xpath("//li[contains(@class, 'job-details-jobs-unified-top-card__job-insight--highlight')]//span[@aria-hidden='true']"));
                     modelo = remotoElement.getText();
                 } catch (NoSuchElementException err) {
                	 System.out.println("Sem remoto");
                 }
                 
                 System.out.println("Candidaturas: " + candidaturasText);
                 System.out.println("Modelo: " + modelo);
                 
                 novo.setDescricao(description.getText());
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


}
