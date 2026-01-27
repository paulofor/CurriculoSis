package gerador.obtemoportunidadefree.passo;


import gerador.obtemoportunidadefree.loopback.DaoAplicacao;
import gerador.obtemoportunidadefree.loopback.DatasetAplicacao;
import gerador.obtemoportunidadefree.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class CriaArquivoTexto extends DaoAplicacao { 

	private int NUM_PASSO = 2;


	// campos saida

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom(ds.getListaOportunidade())) {
			executaProximo();
		} else {
			finalizar();
		}
	}


	@Override
	protected final DaoBase getProximo() {
		return new LoopItemImpl();
	}


	protected boolean executaCustom( List<OportunidadeFree> listaOportunidade ) { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

