package gerador.totalizapalavrachave.loopback;


import br.com.gersis.daobase.IDatasetComum;
import br.com.gersis.loopback.modelo.*;
import java.util.List;

public class DatasetAplicacao  implements IDatasetComum {
	private List<PalavraChave> listaPalavraChave;
	private PalavraRaiz palavraRaizCorrente;
	private PalavraChave palavraChave;


	public void setListaPalavraChave(List<PalavraChave> valor) { 
		this.listaPalavraChave = valor;
	}
	public List<PalavraChave> getListaPalavraChave() { 
		return this.listaPalavraChave;
	}
	public void setPalavraRaizCorrente(PalavraRaiz valor) { 
		this.palavraRaizCorrente = valor;
	}
	public PalavraRaiz getPalavraRaizCorrente() { 
		return this.palavraRaizCorrente;
	}
	public void setPalavraChave(PalavraChave valor) { 
		this.palavraChave = valor;
	}
	public PalavraChave getPalavraChave() { 
		return this.palavraChave;
	}
}
