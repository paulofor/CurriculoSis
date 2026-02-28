package gerador.obtemoportunidadelinkedin.passo.impl;


import java.util.ArrayList;
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

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import br.com.gersis.loopback.modelo.OportunidadeLinkedin;
import br.com.gersis.loopback.modelo.PalavraRaiz;
import gerador.obtemoportunidadelinkedin.passo.AcessaLinkedIn;



public class AcessaLinkedInImpl extends AcessaLinkedIn {

	WebDriver driver = null;
	
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
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		String chromeBinaryPath = obtemChromeBinaryPath();
		if (chromeBinaryPath != null) {
			options.setBinary(chromeBinaryPath);
		}

		// Inicializar o navegador
		driver = criaWebDriver(options);

        try {
            // Acessar a página de login do LinkedIn
            driver.get("https://www.linkedin.com/login");

            // Fazer login
            WebElement emailField = driver.findElement(By.id("username"));
            String linkedinUser = System.getenv("LINKEDIN_USER");
            if (linkedinUser == null || linkedinUser.trim().isEmpty()) {
            	linkedinUser = "paulofore@gmail.com";
            }
            emailField.sendKeys(linkedinUser);

            WebElement passwordField = driver.findElement(By.id("password"));
            String linkedinPassword = System.getenv("LINKEDIN_PASSWORD");
            if (linkedinPassword == null || linkedinPassword.trim().isEmpty()) {
            	linkedinPassword = "xi5*4NDGrb^+Z6T";
            }
            passwordField.sendKeys(linkedinPassword);
            passwordField.sendKeys(Keys.RETURN);

            // Esperar até que a página principal seja carregada
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Navegar para a página de busca de vagas
            driver.get("https://www.linkedin.com/jobs");

            // Inserir termo de pesquisa e buscar
            WebElement searchBox = driver.findElement(By.className("jobs-search-box__text-input"));
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

	private WebDriver criaWebDriver(ChromeOptions options) {
		String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");
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
			configuraWebDriverManagerComVersaoDoNavegador(options);
		}

		try {
			return new ChromeDriver(options);
		} catch (SessionNotCreatedException e) {
			String majorVersaoBrowser = extraiMajorVersaoBrowser(e.getMessage());
			if (majorVersaoBrowser != null && !majorVersaoBrowser.isEmpty()) {
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

	private void configuraWebDriverManagerComVersaoDoNavegador(ChromeOptions options) {
		String chromeBinaryPath = options.getBinary();
		String major = obtemMajorVersaoChrome(chromeBinaryPath);
		if (major != null && !major.isEmpty()) {
			System.out.println("[INFO] Browser detectado na major version " + major + ". Baixando ChromeDriver compativel.");
			WebDriverManager.chromedriver().driverVersion(major).setup();
			return;
		}
		WebDriverManager.chromedriver().setup();
	}

	private String obtemMajorVersaoChrome(String chromeBinaryPath) {
		if (chromeBinaryPath == null || chromeBinaryPath.trim().isEmpty()) {
			return null;
		}
		try {
			Process processo = new ProcessBuilder(chromeBinaryPath, "--version").start();
			String saida = leTexto(processo.getInputStream());
			String erro = leTexto(processo.getErrorStream());
			processo.waitFor(3, TimeUnit.SECONDS);
			String texto = (saida + "\n" + erro);
			Matcher matcher = Pattern.compile("(\\d+)\\.").matcher(texto);
			if (matcher.find()) {
				return matcher.group(1);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private String extraiMajorVersaoBrowser(String mensagemErro) {
		if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
			return null;
		}
		Matcher matcher = Pattern.compile("Current browser version is (\\d+)\\.").matcher(mensagemErro);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
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
