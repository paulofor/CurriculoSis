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

import br.com.gersis.loopback.modelo.PalavraQuantidade;

public class RepositorioPalavraQuantidade extends ModelRepository<PalavraQuantidade> {

	public RepositorioPalavraQuantidade() {
		super("PalavraQuantidade", PalavraQuantidade.class);
	}
	@Override
	protected String verificaNomeUrl(String nome) {
		return "PalavraQuantidade";
	}


	// ***  Operações  ***

	public synchronized void totalizaPalavraChave(PalavraQuantidade palavraRaiz ,PalavraQuantidade palavraChave ,final VoidCallback callback ) {
		RestContractItem contrato = new RestContractItem("PalavraQuantidades/totalizaPalavraChave","POST");
		this.getRestAdapter().getContract().addItem(contrato, "PalavraQuantidade.totalizaPalavraChave");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("palavraRaiz", palavraRaiz.getJSON());
		params.put("palavraChave", palavraChave.getJSON());
		invokeStaticMethod("totalizaPalavraChave", params,   new EmptyResponseParser(callback));
	}

	public synchronized void inicializaPorRaiz(int idPalavraRaiz ,final VoidCallback callback ) {
		RestContractItem contrato = new RestContractItem("PalavraQuantidades/inicializaPorRaiz","POST");
		this.getRestAdapter().getContract().addItem(contrato, "PalavraQuantidade.inicializaPorRaiz");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idPalavraRaiz", idPalavraRaiz);
		invokeStaticMethod("inicializaPorRaiz", params,   new EmptyResponseParser(callback));
	}

	public synchronized void atualizaQuantidades(final VoidCallback callback ) {
		RestContractItem contrato = new RestContractItem("PalavraQuantidades/atualizaQuantidades","POST");
		this.getRestAdapter().getContract().addItem(contrato, "PalavraQuantidade.atualizaQuantidades");
		Map<String, Object> params = new HashMap<String, Object>();
		invokeStaticMethod("atualizaQuantidades", params,   new EmptyResponseParser(callback));
	}


	private JSONArray obtemLista(List<PalavraQuantidade> listaEntrada) {
		JSONArray lista = new JSONArray();
		for (PalavraQuantidade item : listaEntrada) {
			lista.put(item.getJSON());
		}
		return lista;
	}
}
