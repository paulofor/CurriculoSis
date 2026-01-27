package gerador.obtemoportunidadelinkedin.passo.impl;


import br.com.gersis.loopback.modelo.OportunidadeLinkedin;
import gerador.obtemoportunidadelinkedin.passo.*;



public class OportunidadeLinkedin_RecebeItemImpl extends OportunidadeLinkedin_RecebeItem {

	@Override
	protected boolean executaCustom(OportunidadeLinkedin itemOportunidade) {
		this.oportunidade = itemOportunidade;
		return true;
	} 


}

