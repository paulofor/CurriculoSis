package gerador.obtemoportunidadefree.passo.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.gersis.loopback.modelo.OportunidadeFree;
import gerador.obtemoportunidadefree.passo.Acessa99Freela;



public class Acessa99FreelaImpl extends Acessa99Freela { 

	
	WebDriver driver = null;

	@Override
	protected boolean executaCustom() {
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        // Inicializar o navegador
        driver = new ChromeDriver();

        try {
        	
            this.saidaListaOportunidade = new ArrayList<OportunidadeFree>();
            // Acessar a página de login do LinkedIn
            driver.get("https://www.99freelas.com.br/projects");

            WebElement inputField = driver.findElement(By.id("palavras-chave"));
            inputField.sendKeys("inteligencia artificial");

            // Encontra o botão de submit e clica nele
            WebElement submitButton = driver.findElement(By.xpath("//form[@id='frmProjectSearchBox']//button[@type='submit']"));
            submitButton.click();
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            

            // Defina um tempo de espera
            WebDriverWait wait = new WebDriverWait(driver, 10); // 10 segundos
            adicionaItens();
            
            for (int pagina = 2; pagina <= 4; pagina++) {
				// Localiza o botão pelo atributo aria-label usando XPath
				try {
					WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".pagination-component .page-item[data-page='" + pagina + "']")));

					if (button != null) {
						button.click();
						TimeUnit.SECONDS.sleep(5);
						adicionaItens();
					}
				} catch (Exception e) {
					System.out.println("Deu erro mas continua");
				}

			}
            
            driver.quit();
            /*
            // Fazer login
            WebElement emailField = driver.findElement(By.id("username"));
            emailField.sendKeys("paulofore@gmail.com");

            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.sendKeys("Digicom2004");
            passwordField.sendKeys(Keys.RETURN);

            // Esperar até que a página principal seja carregada


     
            
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
            */
           
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Fechar o navegador
            driver.quit();
        }
	}
	
	
	public void adicionaItens() {
        // Defina um tempo de espera
        WebDriverWait wait = new WebDriverWait(driver, 10); // 10 segundos
		  // Localize a lista de itens (result-list)
        List<WebElement> items = driver.findElements(By.cssSelector(".result-list .result-item"));

        // Itere através da lista de itens
        for (WebElement item : items) {
                // Aguarde um curto período para que o conteúdo se expanda
             try {
            	  List<WebElement> expandLinks = item.findElements(By.cssSelector(".read-more .more-link"));
                  if (!expandLinks.isEmpty()) {
                      WebElement expandLink = expandLinks.get(0);

                      // Rolar para o elemento
                      JavascriptExecutor js = (JavascriptExecutor) driver;
                      js.executeScript("arguments[0].scrollIntoView(true);", expandLink);

                      // Aguarde até que o elemento esteja clicável
                      try {
                          wait.until(ExpectedConditions.elementToBeClickable(expandLink)).click();
                      } catch (Exception e) {
                          // Se o clique falhar, tente usar JavaScript
                          js.executeScript("arguments[0].click();", expandLink);
                      }
                  } 
             } catch (Exception e) {
                 e.printStackTrace();
             }
            

        	
            // Extrair informações de cada item
            String title = item.findElement(By.cssSelector("h1.title a")).getText();
            String description = item.findElement(By.cssSelector(".description")).getText();
            //String proposals = item.findElement(By.xpath(".//p[contains(text(), 'Propostas:')]//b")).getText();
            //String interested = item.findElement(By.xpath(".//p[contains(text(), 'Interessados:')]//b")).getText();
            //String timeRemaining = item.findElement(By.cssSelector(".datetime-restante")).getText();

            // Imprimir as informações
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);
            //System.out.println("Proposals: " + proposals);
            //System.out.println("Interested: " + interested);
            //System.out.println("Time Remaining: " + timeRemaining);
            System.out.println("--------------------------------------------------");
            
            OportunidadeFree oportunidade = new OportunidadeFree();
            oportunidade.setTitulo(title);
            oportunidade.setDescricao(description);
            this.saidaListaOportunidade.add(oportunidade);
        }
	}
	
}

