package gerador.obtemoportunidadelinkedin.passo.impl;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import br.com.gersis.loopback.modelo.OportunidadeLinkedin;
import br.com.gersis.loopback.modelo.PalavraRaiz;
import gerador.obtemoportunidadelinkedin.passo.CriaArquivoTexto;



public class CriaArquivoTextoImpl extends CriaArquivoTexto {

	
	final String PATH = "arquivos";
	
	@Override
	protected boolean executaCustom(List<OportunidadeLinkedin> listaOportunidade, PalavraRaiz palavraPesquisaCorrente) {
		

		final int LIMITE = 5;
		
		int contaArquivo = 1;
		int indice = 0;
		int limiteIndice = LIMITE;
		
		BufferedWriter writer = null;
		try {
		
			while (indice < limiteIndice) {
				String arquivo = PATH + "/" + palavraPesquisaCorrente.getPalavra().replaceAll(" " ,  "-") + "-" + (contaArquivo++) + ".txt";
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
				limiteIndice = limiteIndice + LIMITE;
				indice = indice + LIMITE;
				if (limiteIndice > listaOportunidade.size()) limiteIndice = listaOportunidade.size();
			}
			
			String arquivo = PATH + "/" + palavraPesquisaCorrente.getPalavra().replaceAll(" " ,  "-") + "-geral.txt";
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
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
	} 


}

