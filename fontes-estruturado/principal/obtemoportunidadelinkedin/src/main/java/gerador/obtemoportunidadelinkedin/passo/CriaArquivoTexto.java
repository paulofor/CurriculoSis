package gerador.obtemoportunidadelinkedin.passo;


import gerador.obtemoportunidadelinkedin.loopback.DaoAplicacao;
import gerador.obtemoportunidadelinkedin.loopback.DatasetAplicacao;
import gerador.obtemoportunidadelinkedin.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class CriaArquivoTexto extends DaoAplicacao { 

	private int NUM_PASSO = 3;


	// campos saida

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom(ds.getListaOportunidade(), ds.getPalavraPesquisaCorrente())) {
			executaProximo();
		} else {
			finalizar();
		}
	}


	@Override
	protected final DaoBase getProximo() {
		return new LoopItemImpl();
	}


	protected boolean executaCustom( List<OportunidadeLinkedin> listaOportunidade , PalavraRaiz palavraPesquisaCorrente ) { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

