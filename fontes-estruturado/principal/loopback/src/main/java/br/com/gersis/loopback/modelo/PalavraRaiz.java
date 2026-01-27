package br.com.gersis.loopback.modelo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.remoting.BeanUtil;
import org.json.JSONObject;


public class PalavraRaiz extends Model {


	private String palavra;
	private int ativo;
	private int quantidade;
	// Relacionamentos 1
	// Relacionamentos N
	private List<PalavraQuantidade> PalavraQuantidades;
	private List<OportunidadeLinkedin> OportunidadeLinkedins;

	public void setId(Long id) {
		this.setIdObjeto(id);
	}
	public void setId(Integer id) {
		this.setIdObjeto(id);
	}

	public int getIdInteger() {
		return new Integer(getId().toString());
	}
	public long getIdLong() {
		return new Long(getId().toString());
	}

	public JSONObject getJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("id",getId());
			obj.put("palavra", palavra);
			obj.put("ativo", ativo);
			obj.put("quantidade", quantidade);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}


	public void setPalavra(String valor) { 
		this.palavra = valor;
	}
	public String getPalavra() { 
		return this.palavra;
	}
	public void setAtivo(int valor) { 
		this.ativo = valor;
	}
	public int getAtivo() { 
		return this.ativo;
	}
	public void setQuantidade(int valor) { 
		this.quantidade = valor;
	}
	public int getQuantidade() { 
		return this.quantidade;
	}

	public List<PalavraQuantidade> getPalavraQuantidades() {
		return  PalavraQuantidades;
	}
	public void setPalavraQuantidades(List<PalavraQuantidade> valores) {
		this.PalavraQuantidades = new ArrayList<PalavraQuantidade>();
		for (int i = 0; i < valores.size(); i++) {
			Object objeto = new PalavraQuantidade();
			System.out.println(" --> ObjetoMap ");
			BeanUtil.setProperties(objeto, (Map<String, ? extends Object>) valores.get(i), true);
			this.PalavraQuantidades.add((PalavraQuantidade) objeto);
		}
	}
	public List<OportunidadeLinkedin> getOportunidadeLinkedins() {
		return  OportunidadeLinkedins;
	}
	public void setOportunidadeLinkedins(List<OportunidadeLinkedin> valores) {
		this.OportunidadeLinkedins = new ArrayList<OportunidadeLinkedin>();
		for (int i = 0; i < valores.size(); i++) {
			Object objeto = new OportunidadeLinkedin();
			System.out.println(" --> ObjetoMap ");
			BeanUtil.setProperties(objeto, (Map<String, ? extends Object>) valores.get(i), true);
			this.OportunidadeLinkedins.add((OportunidadeLinkedin) objeto);
		}
	}
}
