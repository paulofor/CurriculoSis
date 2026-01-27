package gerador.obtemoportunidadelinkedin.passo;


import gerador.obtemoportunidadelinkedin.loopback.DaoAplicacao;
import gerador.obtemoportunidadelinkedin.loopback.DatasetAplicacao;
import gerador.obtemoportunidadelinkedin.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class LoopItem extends DaoAplicacao { 

	private int NUM_PASSO = 3;


	// campos saida
	protected OportunidadeLinkedin  saidaItemOportunidade;

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom(ds.getListaOportunidade())) {
			ds.setItemOportunidade(saidaItemOportunidade);
			executaProximo();
		} else {
			finalizar();
		}
	}


	@Override
	protected final DaoBase getProximo() {
		return new OportunidadeLinkedin_RecebeItemImpl();
	}


	protected boolean executaCustom( List<OportunidadeLinkedin> listaOportunidade ) { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

