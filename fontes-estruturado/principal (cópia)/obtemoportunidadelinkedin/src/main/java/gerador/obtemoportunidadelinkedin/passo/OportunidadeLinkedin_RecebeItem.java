package gerador.obtemoportunidadelinkedin.passo;


import gerador.obtemoportunidadelinkedin.loopback.DaoAplicacao;
import gerador.obtemoportunidadelinkedin.loopback.DatasetAplicacao;
import gerador.obtemoportunidadelinkedin.passo.impl.*;
import br.com.gersis.daobase.*;
import br.com.gersis.loopback.modelo.*;

import java.util.List;
import com.strongloop.android.loopback.callbacks.*;


public abstract class OportunidadeLinkedin_RecebeItem extends DaoAplicacao { 

	private int NUM_PASSO = 4;


	protected OportunidadeLinkedin oportunidade;

	@Override
	protected final void executaImpl() {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		if (executaCustom(ds.getItemOportunidade())) {
			if (oportunidade==null) {
				throw new RuntimeException("oportunidade precisa ser atribuido em OportunidadeLinkedin_RecebeItemImpl ");
			}
			repOportunidadeLinkedin.recebeItem( oportunidade, new VoidCallback() { 
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


	protected boolean executaCustom( OportunidadeLinkedin itemOportunidade ) { return true; }

	protected void preFinalizar() { return; }

	public int getNumPasso() {
		return NUM_PASSO;
	}


}

