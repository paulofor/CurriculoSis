package gerador.totalizapalavrachave.passo;


import gerador.totalizapalavrachave.loopback.DaoAplicacao;
import gerador.totalizapalavrachave.loopback.DatasetAplicacao;
import gerador.totalizapalavrachave.passo.impl.*;
import br.com.gersis.daobase.DaoBase;
import br.com.gersis.daobase.IDatasetComum;

public class TotalizaPalavraChaveObj extends DaoAplicacao { 

	@Override
	protected void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		PalavraChave_ListaPalavra exec = new PalavraChave_ListaPalavraImpl();
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

