package gerador.obtemoportunidadefree.passo.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import br.com.gersis.loopback.modelo.OportunidadeFree;
import gerador.obtemoportunidadefree.passo.CriaArquivoTexto;

public class CriaArquivoTextoImpl extends CriaArquivoTexto {

	final String PATH = "arquivos";

	@Override
	protected boolean executaCustom(List<OportunidadeFree> listaOportunidade) {

		String arquivo = PATH + "/inteligencia-artificial.txt";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
			// Iterar sobre cada linha do ArrayList e escrever no arquivo
			for (OportunidadeFree line : listaOportunidade) {
				writer.newLine();
				writer.write("**" + line.getTitulo() + "***");
				writer.newLine(); // Adicionar uma nova linha após cada linha escrita
				writer.newLine();
				writer.write(line.getDescricao());
				writer.newLine(); // Adicionar uma nova linha após cada linha escrita
				writer.newLine();
			}
			System.out.println("Arquivo escrito com sucesso em " + arquivo);
			return true;
		} catch (IOException e) {
			// Tratar possíveis exceções de I/O
			e.printStackTrace();
			return false;
		}
	}

}
