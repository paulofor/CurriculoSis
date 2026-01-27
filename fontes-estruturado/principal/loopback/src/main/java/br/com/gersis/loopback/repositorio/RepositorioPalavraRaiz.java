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

import br.com.gersis.loopback.modelo.PalavraRaiz;

public class RepositorioPalavraRaiz extends ModelRepository<PalavraRaiz> {

	public RepositorioPalavraRaiz() {
		super("PalavraRaiz", PalavraRaiz.class);
	}
	@Override
	protected String verificaNomeUrl(String nome) {
		return "PalavraRaiz";
	}


	// ***  Operações  ***

	public synchronized void listaParaVisita(final ListCallback<PalavraRaiz> callback ) {
		RestContractItem contrato = new RestContractItem("PalavraRaizs/listaParaVisita","GET");
		this.getRestAdapter().getContract().addItem(contrato, "PalavraRaiz.listaParaVisita");
		Map<String, Object> params = new HashMap<String, Object>();
		invokeStaticMethod("listaParaVisita", params,   new JsonArrayParser<PalavraRaiz>(this, callback));
	}

	public synchronized void atualizaQuantidadeGeral(final VoidCallback callback ) {
		RestContractItem contrato = new RestContractItem("PalavraRaizs/atualizaQuantidadeGeral","POST");
		this.getRestAdapter().getContract().addItem(contrato, "PalavraRaiz.atualizaQuantidadeGeral");
		Map<String, Object> params = new HashMap<String, Object>();
		invokeStaticMethod("atualizaQuantidadeGeral", params,   new EmptyResponseParser(callback));
	}


	private JSONArray obtemLista(List<PalavraRaiz> listaEntrada) {
		JSONArray lista = new JSONArray();
		for (PalavraRaiz item : listaEntrada) {
			lista.put(item.getJSON());
		}
		return lista;
	}
}
