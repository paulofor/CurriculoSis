package gerador.totalizapalavrachave.passo.impl;


import java.util.List;

import br.com.gersis.loopback.modelo.PalavraChave;
import gerador.totalizapalavrachave.loopback.DatasetAplicacao;
import gerador.totalizapalavrachave.passo.LoopItem;



public class LoopItemImpl extends LoopItem {

	@Override
	protected boolean executaCustom(List<PalavraChave> listaPalavraChave) {
		final DatasetAplicacao ds = (DatasetAplicacao) this.getComum();
		for (PalavraChave item : listaPalavraChave) {
			ds.setPalavraChave(item);
			executaProximoSemFinalizar();
		}
		preFinalizar();
		finalizar();
		return false;
	} 


}

