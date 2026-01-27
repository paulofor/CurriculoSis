package gerador.obtemoportunidadelinkedin.passo.impl;


import java.util.List;

import br.com.gersis.loopback.modelo.OportunidadeLinkedin;
import gerador.obtemoportunidadelinkedin.loopback.DatasetAplicacao;
import gerador.obtemoportunidadelinkedin.passo.LoopItem;



public class LoopItemImpl extends LoopItem { 

	
	
	
	@Override
	protected boolean executaCustom(List<OportunidadeLinkedin> listaOportunidade) {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		for (OportunidadeLinkedin item : listaOportunidade) {
			ds.setItemOportunidade(item);
			executaProximoSemFinalizar();
		}
		preFinalizar();
		finalizar();
		return false;
	}


}

