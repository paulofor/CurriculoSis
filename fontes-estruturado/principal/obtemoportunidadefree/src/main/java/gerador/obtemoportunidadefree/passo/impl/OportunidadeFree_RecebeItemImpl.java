package gerador.obtemoportunidadefree.passo.impl;


import br.com.gersis.loopback.modelo.OportunidadeFree;
import gerador.obtemoportunidadefree.passo.*;



public class OportunidadeFree_RecebeItemImpl extends OportunidadeFree_RecebeItem {

	@Override
	protected boolean executaCustom(OportunidadeFree oportunidade) {
		this.oportunidade = oportunidade;
		return true;
	} 


}

