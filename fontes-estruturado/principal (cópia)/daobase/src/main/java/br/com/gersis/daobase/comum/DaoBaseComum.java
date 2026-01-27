package br.com.gersis.daobase.comum;

import java.util.HashMap;
import java.util.Map;

import br.com.gersis.daobase.DaoBase;
import br.com.gersis.daobase.IDatasetComum;

public abstract class DaoBaseComum extends DaoBase{

	
	//static protected RestAdapter adapter = null; 
	static protected Map<String,DaoBase> mapProximo = new HashMap<String,DaoBase>(); 
	static public String urlLoopback = null;

	@Override
	protected IDatasetComum criaDataSet() {
		throw new RuntimeException("Classe comum não pode criar dataset");
	}

	
	public static void setUrl(String url) {
		urlLoopback = url;
	}

	public static void setProximo(String atual, DaoBase proximo) {
		mapProximo.put(atual,proximo);
	}
	public static DaoBase getProximo(String nome) {
		return mapProximo.get(nome);
	}
	
	@Override
	protected DaoBase getProximo() {
		return mapProximo.get(getNome());
	}

	protected String getNome() {
		return this.getClass().getSimpleName();
	}
	
}
