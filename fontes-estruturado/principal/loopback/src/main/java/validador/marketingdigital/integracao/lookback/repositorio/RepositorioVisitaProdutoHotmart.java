package validador.marketingdigital.integracao.lookback.repositorio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.EmptyResponseParser;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.adapters.RestContractItem;

import validador.marketingdigital.integracao.lookback.modelo.VisitaProdutoHotmart;

public class RepositorioVisitaProdutoHotmart extends ModelRepository<VisitaProdutoHotmart>{

	public RepositorioVisitaProdutoHotmart() {
		super("ProdutoHotmart", VisitaProdutoHotmart.class);
	}
	@Override
	protected String verificaNomeUrl(String nome) {
		return "ProdutoHotmarts";
	}
	
	
	
	
	public void insereLista(List<VisitaProdutoHotmart> listaItem, final VoidCallback voidCallback ) {
		RestContractItem contrato = new RestContractItem("ProdutoHotmarts/insereListaProdutoHotmart","POST");
		this.getRestAdapter().getContract().addItem(contrato, "ProdutoHotmart.insereListaProdutoHotmart");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lista", obtemLista(listaItem));
        invokeStaticMethod("insereListaProdutoHotmart", params,   new EmptyResponseParser(voidCallback));
	}
	
	private JSONArray obtemLista(List<VisitaProdutoHotmart> listaEntrada) {
		JSONArray lista = new JSONArray();
		for (VisitaProdutoHotmart item : listaEntrada) {
			lista.put(item.getJSON());
		}
		return lista;
	}

}
