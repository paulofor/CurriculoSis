package gerador.totalizapalavrachave.passo;


import gerador.totalizapalavrachave.loopback.DaoAplicacao;
import gerador.totalizapalavrachave.loopback.DatasetAplicacao;
import gerador.totalizapalavrachave.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class PalavraQuantidade_TotalizaPalavraChave extends DaoAplicacao { 

	private int NUM_PASSO = 5;


	protected PalavraQuantidade palavraRaiz;
	protected PalavraQuantidade palavraChave;

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom(ds.getPalavraRaizCorrente(), ds.getPalavraChave())) {
			if (palavraRaiz==null) {
				throw new RuntimeException("palavraRaiz precisa ser atribuido em PalavraQuantidade_TotalizaPalavraChaveImpl ");
			}
			if (palavraChave==null) {
				throw new RuntimeException("palavraChave precisa ser atribuido em PalavraQuantidade_TotalizaPalavraChaveImpl ");
			}
			repPalavraQuantidade.totalizaPalavraChave( palavraRaiz,palavraChave, new VoidCallback() { 
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
		return new DummyDaoBase();
	}


	protected boolean executaCustom( PalavraRaiz palavraRaizCorrente , PalavraChave palavraChave ) { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

