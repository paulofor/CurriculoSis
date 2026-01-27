package gerador.obtemoportunidadefree.loopback;


import br.com.gersis.daobase.IDatasetComum;
import br.com.gersis.loopback.modelo.*;
import java.util.List;

public class DatasetAplicacao  implements IDatasetComum {
	private List<OportunidadeFree> listaOportunidade;
	private OportunidadeFree oportunidade;


	public void setListaOportunidade(List<OportunidadeFree> valor) { 
		this.listaOportunidade = valor;
	}
	public List<OportunidadeFree> getListaOportunidade() { 
		return this.listaOportunidade;
	}
	public void setOportunidade(OportunidadeFree valor) { 
		this.oportunidade = valor;
	}
	public OportunidadeFree getOportunidade() { 
		return this.oportunidade;
	}
}
