package gerador.obtemoportunidadefree.passo;


import gerador.obtemoportunidadefree.loopback.DaoAplicacao;
import gerador.obtemoportunidadefree.loopback.DatasetAplicacao;
import gerador.obtemoportunidadefree.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class OportunidadeFree_RecebeItem extends DaoAplicacao { 

	private int NUM_PASSO = 4;


	protected OportunidadeFree oportunidade;

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom(ds.getOportunidade())) {
			if (oportunidade==null) {
				throw new RuntimeException("oportunidade precisa ser atribuido em OportunidadeFree_RecebeItemImpl ");
			}
			repOportunidadeFree.recebeItem( oportunidade, new VoidCallback() { 
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


	protected boolean executaCustom( OportunidadeFree oportunidade ) { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

