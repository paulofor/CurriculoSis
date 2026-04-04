package gerador.obtemoportunidadelinkedin.passo.impl;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import br.com.gersis.loopback.modelo.OportunidadeLinkedin;
import br.com.gersis.loopback.modelo.PalavraRaiz;
import gerador.obtemoportunidadelinkedin.passo.CriaArquivoTexto;



public class CriaArquivoTextoImpl extends CriaArquivoTexto {

	
	final String PATH = "arquivos";
	
	@Override
	protected boolean executaCustom(List<OportunidadeLinkedin> listaOportunidade, PalavraRaiz palavraPesquisaCorrente) {
		
		String palavra = palavraPesquisaCorrente != null ? palavraPesquisaCorrente.getPalavra() : "sem-palavra";
		System.out.println("[INFO] Iniciando criacao de arquivos para palavra='" + palavra + "' com total de oportunidades=" + (listaOportunidade == null ? 0 : listaOportunidade.size()) + ".");
		System.out.println("[DEBUG] Diretorio de trabalho atual: " + new File(".").getAbsolutePath());
		File pastaArquivos = new File(PATH);
		System.out.println("[DEBUG] Pasta de saida configurada: " + pastaArquivos.getAbsolutePath() + " (existe=" + pastaArquivos.exists() + ").");
		if (!pastaArquivos.exists()) {
			boolean criada = pastaArquivos.mkdirs();
			System.out.println("[INFO] Pasta '" + PATH + "' nao existia. Tentativa de criacao => " + (criada ? "sucesso" : "falha") + ".");
		}
		if (!pastaArquivos.exists()) {
			System.out.println("[ERROR] Pasta de saida '" + PATH + "' indisponivel. Nao sera possivel gerar os arquivos de debug das vagas.");
			return false;
		}

		final int LIMITE = 5;
		
		int contaArquivo = 1;
		int indice = 0;
		int limiteIndice = LIMITE;
		
		BufferedWriter writer = null;
		try {
		
			while (indice < limiteIndice) {
				String arquivo = PATH + "/" + palavraPesquisaCorrente.getPalavra().replaceAll(" " ,  "-") + "-" + (contaArquivo++) + ".txt";
				System.out.println("[DEBUG] Gravando arquivo parcial: " + arquivo + " (itens " + indice + " ate " + (limiteIndice - 1) + ").");
				writer = new BufferedWriter(new FileWriter(arquivo));
				int oportunidade = 1;
				for (int pos = indice; pos < limiteIndice ; pos++) {
					OportunidadeLinkedin atual = listaOportunidade.get(pos);
					writer.write("[Oportunidade " + (oportunidade++) + "]");
					writer.newLine();
					writer.write("Oportunidade: " + atual.getTitulo());
					writer.newLine(); // Adicionar uma nova linha após cada linha escrita
					writer.newLine();
					writer.write(atual.getDescricao());
					writer.newLine(); // Adicionar uma nova linha após cada linha escrita
					writer.write("-----------------------------------------------------------------------------------");
					writer.newLine();
				}
				writer.close();
				System.out.println("[DEBUG] Arquivo parcial finalizado: " + arquivo + ".");
				limiteIndice = limiteIndice + LIMITE;
				indice = indice + LIMITE;
				if (limiteIndice > listaOportunidade.size()) limiteIndice = listaOportunidade.size();
			}
			
			String arquivo = PATH + "/" + palavraPesquisaCorrente.getPalavra().replaceAll(" " ,  "-") + "-geral.txt";
			System.out.println("[DEBUG] Gravando arquivo consolidado: " + arquivo + ".");
			writer = new BufferedWriter(new FileWriter(arquivo));
			for (OportunidadeLinkedin atual : listaOportunidade) {
				writer.newLine();
				writer.newLine();
				writer.write("Oportunidade: " + atual.getTitulo());
				writer.newLine(); // Adicionar uma nova linha após cada linha escrita
				writer.newLine();
				writer.write(atual.getDescricao());
				writer.newLine(); // Adicionar uma nova linha após cada linha escrita
				writer.write("-----------------------------------------------------------------------------------");
				writer.newLine();
			}
			writer.close();
			System.out.println("[INFO] Arquivo consolidado finalizado: " + arquivo + ".");
			
			return true;
		} catch (Exception e) {
			System.out.println("[ERROR] Falha ao criar arquivos de texto para palavra='" + palavra + "'.");
			e.printStackTrace();
			return false;
		}
		
		
		
	} 


}
