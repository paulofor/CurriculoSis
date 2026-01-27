package gerador.obtemoportunidadefree.passo.impl;


import java.util.List;

import br.com.gersis.loopback.modelo.OportunidadeFree;
import br.com.gersis.loopback.modelo.OportunidadeLinkedin;
import gerador.obtemoportunidadefree.loopback.DatasetAplicacao;
import gerador.obtemoportunidadefree.passo.LoopItem;



public class LoopItemImpl extends LoopItem {

	@Override
	protected boolean executaCustom(List<OportunidadeFree> listaOportunidade) {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		for (OportunidadeFree item : listaOportunidade) {
			ds.setOportunidade(item);
			executaProximoSemFinalizar();
		}
		preFinalizar();
		finalizar();
		return false;
	} 


}

