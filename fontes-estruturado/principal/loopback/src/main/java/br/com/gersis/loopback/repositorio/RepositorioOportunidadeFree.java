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

import br.com.gersis.loopback.modelo.OportunidadeFree;

public class RepositorioOportunidadeFree extends ModelRepository<OportunidadeFree> {

	public RepositorioOportunidadeFree() {
		super("OportunidadeFree", OportunidadeFree.class);
	}
	@Override
	protected String verificaNomeUrl(String nome) {
		return "OportunidadeFree";
	}


	// ***  Operações  ***

	public synchronized void recebeItem(OportunidadeFree oportunidade ,final VoidCallback callback ) {
		RestContractItem contrato = new RestContractItem("OportunidadeFrees/recebeItem","POST");
		this.getRestAdapter().getContract().addItem(contrato, "OportunidadeFree.recebeItem");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oportunidade", oportunidade.getJSON());
		invokeStaticMethod("recebeItem", params,   new EmptyResponseParser(callback));
	}


	private JSONArray obtemLista(List<OportunidadeFree> listaEntrada) {
		JSONArray lista = new JSONArray();
		for (OportunidadeFree item : listaEntrada) {
			lista.put(item.getJSON());
		}
		return lista;
	}
}
