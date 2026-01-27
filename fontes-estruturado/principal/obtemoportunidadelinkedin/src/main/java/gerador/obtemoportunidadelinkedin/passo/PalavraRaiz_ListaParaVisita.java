package gerador.obtemoportunidadelinkedin.passo;


import gerador.obtemoportunidadelinkedin.loopback.DaoAplicacao;
import gerador.obtemoportunidadelinkedin.loopback.DatasetAplicacao;
import gerador.obtemoportunidadelinkedin.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class PalavraRaiz_ListaParaVisita extends DaoAplicacao { 

	private int NUM_PASSO = 1;



	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom()) {
			repPalavraRaiz.listaParaVisita(  new ListCallback<PalavraRaiz>() { 
				public void onSuccess(List<PalavraRaiz> lista) {
					for (PalavraRaiz item : lista) {
						ds.setPalavraPesquisaCorrente(item);
						executaProximoSemFinalizar();
					}
					preFinalizar();
					finalizar();
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
		return new AcessaLinkedInImpl();
	}


	protected boolean executaCustom() { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

