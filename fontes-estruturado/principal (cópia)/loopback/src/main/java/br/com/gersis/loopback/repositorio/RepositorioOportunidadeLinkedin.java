package br.com.gersis.loopback.repositorio;


import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.json.JSONArray;

import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.JsonArrayParser;
import com.strongloop.android.loopback.callbacks.JsonObjectParser;
import com.strongloop.android.loopback.callbacks.EmptyResponseParser;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.adapters.RestContractItem;

import br.com.gersis.loopback.modelo.OportunidadeLinkedin;

public class RepositorioOportunidadeLinkedin extends ModelRepository<OportunidadeLinkedin> {

	public RepositorioOportunidadeLinkedin() {
		super("OportunidadeLinkedin", OportunidadeLinkedin.class);
	}
	@Override
	protected String verificaNomeUrl(String nome) {
		return "OportunidadeLinkedin";
	}


	// ***  Operações  ***

	public synchronized void recebeItem(OportunidadeLinkedin oportunidade ,final VoidCallback callback ) {
		RestContractItem contrato = new RestContractItem("OportunidadeLinkedins/recebeItem","POST");
		this.getRestAdapter().getContract().addItem(contrato, "OportunidadeLinkedin.recebeItem");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oportunidade", oportunidade.getJSON());
		invokeStaticMethod("recebeItem", params,   new EmptyResponseParser(callback));
	}


	private JSONArray obtemLista(List<OportunidadeLinkedin> listaEntrada) {
		JSONArray lista = new JSONArray();
		for (OportunidadeLinkedin item : listaEntrada) {
			lista.put(item.getJSON());
		}
		return lista;
	}
}
