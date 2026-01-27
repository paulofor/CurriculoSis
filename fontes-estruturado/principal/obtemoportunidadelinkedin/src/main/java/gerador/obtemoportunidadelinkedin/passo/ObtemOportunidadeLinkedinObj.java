package gerador.obtemoportunidadelinkedin.passo;


import gerador.obtemoportunidadelinkedin.loopback.DaoAplicacao;
import gerador.obtemoportunidadelinkedin.loopback.DatasetAplicacao;
import gerador.obtemoportunidadelinkedin.passo.impl.*;
import br.com.gersis.daobase.DaoBase;
import br.com.gersis.daobase.IDatasetComum;

public class ObtemOportunidadeLinkedinObj extends DaoAplicacao { 

	@Override
	protected void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		PalavraRaiz_ListaParaVisita exec = new PalavraRaiz_ListaParaVisitaImpl();
		exec.setComum(ds);
		exec.executa();
		executaFinalizacao(ds);
		finalizar();
	}
	private void executaFinalizacao(DatasetAplicacao ds) {
	}
	public int getNumPasso() {
		return 1;
	}
}

