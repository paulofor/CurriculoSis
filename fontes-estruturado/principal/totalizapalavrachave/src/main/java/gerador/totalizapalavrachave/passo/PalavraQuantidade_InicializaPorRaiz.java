package gerador.totalizapalavrachave.passo;


import gerador.totalizapalavrachave.loopback.DaoAplicacao;
import gerador.totalizapalavrachave.loopback.DatasetAplicacao;
import gerador.totalizapalavrachave.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class PalavraQuantidade_InicializaPorRaiz extends DaoAplicacao { 

	private int NUM_PASSO = 3;


	protected int idPalavraRaiz;

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom(ds.getPalavraRaizCorrente())) {
			repPalavraQuantidade.inicializaPorRaiz( idPalavraRaiz, new VoidCallback() { 
				public void onSuccess() {
					executaProximo();
				}
				public void onError(Throwable t) {
					onErrorBase(t);
				}
			});
		} else {
			executaProximo();
		}
	}


	@Override
	protected final DaoBase getProximo() {
		return new LoopItemImpl();
	}


	protected boolean executaCustom( PalavraRaiz palavraRaizCorrente ) { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

