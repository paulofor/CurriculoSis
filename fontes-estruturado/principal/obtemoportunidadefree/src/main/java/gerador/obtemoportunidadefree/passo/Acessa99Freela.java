package gerador.obtemoportunidadefree.passo;


import gerador.obtemoportunidadefree.loopback.DaoAplicacao;
import gerador.obtemoportunidadefree.loopback.DatasetAplicacao;
import gerador.obtemoportunidadefree.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class Acessa99Freela extends DaoAplicacao { 

	private int NUM_PASSO = 1;


	// campos saida
	protected List<OportunidadeFree>  saidaListaOportunidade;

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom()) {
			ds.setListaOportunidade(saidaListaOportunidade);
			executaProximo();
		} else {
			finalizar();
		}
	}


	@Override
	protected final DaoBase getProximo() {
		return new CriaArquivoTextoImpl();
	}


	protected boolean executaCustom() { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

