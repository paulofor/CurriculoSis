package gerador.obtemoportunidadefree.passo;


import gerador.obtemoportunidadefree.loopback.DaoAplicacao;
import gerador.obtemoportunidadefree.loopback.DatasetAplicacao;
import gerador.obtemoportunidadefree.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class LoopItem extends DaoAplicacao { 

	private int NUM_PASSO = 3;


	// campos saida
	protected OportunidadeFree  saidaOportunidade;

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom(ds.getListaOportunidade())) {
			ds.setOportunidade(saidaOportunidade);
			executaProximo();
		} else {
			finalizar();
		}
	}


	@Override
	protected final DaoBase getProximo() {
		return new OportunidadeFree_RecebeItemImpl();
	}


	protected boolean executaCustom( List<OportunidadeFree> listaOportunidade ) { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

