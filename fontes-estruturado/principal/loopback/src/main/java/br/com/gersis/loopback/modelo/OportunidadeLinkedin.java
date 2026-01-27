package br.com.gersis.loopback.modelo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.remoting.BeanUtil;
import org.json.JSONObject;


public class OportunidadeLinkedin extends Model {


	private String descricao;
	private String data;
	private String url;
	private String tempo;
	private String volume;
	private String titulo;
	private String palavraRaizId;
	private String empresa;
	private int maisRecente;
	private String modelo;
	private String dataEnvio;
	// Relacionamentos 1
	private PalavraRaiz PalavraRaiz;
	// Relacionamentos N
	private List<OportunidadePalavra> OportunidadePalavras;

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
			obj.put("descricao", descricao);
			obj.put("data", data);
			obj.put("url", url);
			obj.put("tempo", tempo);
			obj.put("volume", volume);
			obj.put("titulo", titulo);
			obj.put("palavraRaizId", palavraRaizId);
			obj.put("empresa", empresa);
			obj.put("maisRecente", maisRecente);
			obj.put("modelo", modelo);
			obj.put("dataEnvio", dataEnvio);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}


	public void setDescricao(String valor) { 
		this.descricao = valor;
	}
	public String getDescricao() { 
		return this.descricao;
	}
	public void setData(String valor) { 
		this.data = valor;
	}
	public String getData() { 
		return this.data;
	}
	public void setUrl(String valor) { 
		this.url = valor;
	}
	public String getUrl() { 
		return this.url;
	}
	public void setTempo(String valor) { 
		this.tempo = valor;
	}
	public String getTempo() { 
		return this.tempo;
	}
	public void setVolume(String valor) { 
		this.volume = valor;
	}
	public String getVolume() { 
		return this.volume;
	}
	public void setTitulo(String valor) { 
		this.titulo = valor;
	}
	public String getTitulo() { 
		return this.titulo;
	}
	public void setPalavraRaizId(String valor) { 
		this.palavraRaizId = valor;
	}
	public String getPalavraRaizId() { 
		return this.palavraRaizId;
	}
	public void setEmpresa(String valor) { 
		this.empresa = valor;
	}
	public String getEmpresa() { 
		return this.empresa;
	}
	public void setMaisRecente(int valor) { 
		this.maisRecente = valor;
	}
	public int getMaisRecente() { 
		return this.maisRecente;
	}
	public void setModelo(String valor) { 
		this.modelo = valor;
	}
	public String getModelo() { 
		return this.modelo;
	}
	public void setDataEnvio(String valor) { 
		this.dataEnvio = valor;
	}
	public String getDataEnvio() { 
		return this.dataEnvio;
	}

	public PalavraRaiz getPalavraRaiz() {
		return PalavraRaiz;
	}
	public void setPalavraRaiz(HashMap valor) {
		this.PalavraRaiz = new PalavraRaiz();
		BeanUtil.setProperties(this.PalavraRaiz, (Map<String, ? extends Object>) valor, true);
	}
	public List<OportunidadePalavra> getOportunidadePalavras() {
		return  OportunidadePalavras;
	}
	public void setOportunidadePalavras(List<OportunidadePalavra> valores) {
		this.OportunidadePalavras = new ArrayList<OportunidadePalavra>();
		for (int i = 0; i < valores.size(); i++) {
			Object objeto = new OportunidadePalavra();
			System.out.println(" --> ObjetoMap ");
			BeanUtil.setProperties(objeto, (Map<String, ? extends Object>) valores.get(i), true);
			this.OportunidadePalavras.add((OportunidadePalavra) objeto);
		}
	}
}
