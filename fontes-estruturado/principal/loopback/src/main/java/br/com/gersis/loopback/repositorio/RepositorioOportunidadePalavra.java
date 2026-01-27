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

import br.com.gersis.loopback.modelo.OportunidadePalavra;

public class RepositorioOportunidadePalavra extends ModelRepository<OportunidadePalavra> {

	public RepositorioOportunidadePalavra() {
		super("OportunidadePalavra", OportunidadePalavra.class);
	}
	@Override
	protected String verificaNomeUrl(String nome) {
		return "OportunidadePalavra";
	}


	// ***  Operações  ***

	public synchronized void atualiza(final VoidCallback callback ) {
		RestContractItem contrato = new RestContractItem("OportunidadePalavras/atualiza","POST");
		this.getRestAdapter().getContract().addItem(contrato, "OportunidadePalavra.atualiza");
		Map<String, Object> params = new HashMap<String, Object>();
		invokeStaticMethod("atualiza", params,   new EmptyResponseParser(callback));
	}


	private JSONArray obtemLista(List<OportunidadePalavra> listaEntrada) {
		JSONArray lista = new JSONArray();
		for (OportunidadePalavra item : listaEntrada) {
			lista.put(item.getJSON());
		}
		return lista;
	}
}
