package gerador.totalizapalavrachave.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import gerador.totalizapalavrachave.passo.*;
import gerador.totalizapalavrachave.passo.impl.*;
import br.com.gersis.daobase.comum.DaoBaseComum;

public class TotalizaPalavraChave {

	private static String UrlLoopback = "";

	public static void main(String[] args) {
		System.out.print("TotalizaPalavraChave");
		System.out.println("(30/07/2024 06:24:38)");
		try {
			carregaProp();
			TotalizaPalavraChaveObj obj = new TotalizaPalavraChaveObj();
			obj.executa();
			LocalDateTime dataHoraAtual = LocalDateTime.now();
			DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String dataHoraFormatada = dataHoraAtual.format(formatador);
			System.out.println("finalizou " + dataHoraFormatada);
			System.exit(0);
		} catch (Exception e) {
			gravarErro(e);
		}
	}


	private static void gravarErro(Exception e) {
		try {
			FileWriter fileWriter = new FileWriter("TotalizaPalavraChave.err", true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			e.printStackTrace(printWriter);
			printWriter.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void carregaProp() throws IOException {
		//System.out.println("Dir:" + System.getProperty("user.dir"));
		//InputStream input = new FileInputStream("CriaPythonTreinoRede.config");
		//Properties prop = new Properties();
		//prop.load(input);
		//UrlLoopback = prop.getProperty("loopback.url");
		UrlLoopback = "http://vps-40d69db1.vps.ovh.ca:25012/api";
		DaoBaseComum.setUrl(UrlLoopback);
	}

	private static void preparaComum() {
		DaoBaseComum.setUrl(UrlLoopback);
		DaoBaseComum.setProximo("TotalizaPalavraChaveObj", new PalavraChave_ListaPalavraImpl());
		DaoBaseComum.setProximo("PalavraChave_ListaPalavra", new PalavraRaiz_ListaParaVisitaImpl());
		DaoBaseComum.setProximo("PalavraRaiz_ListaParaVisita", new PalavraQuantidade_InicializaPorRaizImpl());
		DaoBaseComum.setProximo("PalavraQuantidade_InicializaPorRaiz", new LoopItemImpl());
		DaoBaseComum.setProximo("LoopItem", new PalavraQuantidade_TotalizaPalavraChaveImpl());
	}
}
