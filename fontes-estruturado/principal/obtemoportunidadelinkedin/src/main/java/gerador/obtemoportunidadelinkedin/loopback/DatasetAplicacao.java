package gerador.obtemoportunidadelinkedin.loopback;


import br.com.gersis.daobase.IDatasetComum;
import br.com.gersis.loopback.modelo.*;
import java.util.List;

public class DatasetAplicacao  implements IDatasetComum {
	private List<OportunidadeLinkedin> listaOportunidade;
	private PalavraRaiz palavraPesquisaCorrente;
	private OportunidadeLinkedin itemOportunidade;


	public void setListaOportunidade(List<OportunidadeLinkedin> valor) { 
		this.listaOportunidade = valor;
	}
	public List<OportunidadeLinkedin> getListaOportunidade() { 
		return this.listaOportunidade;
	}
	public void setPalavraPesquisaCorrente(PalavraRaiz valor) { 
		this.palavraPesquisaCorrente = valor;
	}
	public PalavraRaiz getPalavraPesquisaCorrente() { 
		return this.palavraPesquisaCorrente;
	}
	public void setItemOportunidade(OportunidadeLinkedin valor) { 
		this.itemOportunidade = valor;
	}
	public OportunidadeLinkedin getItemOportunidade() { 
		return this.itemOportunidade;
	}
}
