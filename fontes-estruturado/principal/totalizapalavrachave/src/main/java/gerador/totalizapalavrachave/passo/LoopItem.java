package gerador.totalizapalavrachave.passo;


import gerador.totalizapalavrachave.loopback.DaoAplicacao;
import gerador.totalizapalavrachave.loopback.DatasetAplicacao;
import gerador.totalizapalavrachave.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class LoopItem extends DaoAplicacao { 

	private int NUM_PASSO = 4;


	// campos saida
	protected PalavraChave  saidaPalavraChave;

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom(ds.getListaPalavraChave())) {
			ds.setPalavraChave(saidaPalavraChave);
			executaProximo();
		} else {
			finalizar();
		}
	}


	@Override
	protected final DaoBase getProximo() {
		return new PalavraQuantidade_TotalizaPalavraChaveImpl();
	}


	protected boolean executaCustom( List<PalavraChave> listaPalavraChave ) { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

