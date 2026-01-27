package br.com.gersis.loopback.modelo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.remoting.BeanUtil;
import org.json.JSONObject;


public class ExperienciaProfissionalLivre extends Model {


	private String cliente;
	private String dataInicio;
	private String dataTermino;
	private String consultoria;
	private String descricaoLivre;
	private String principaisTecnologias;
	private String tituloFuncao;
	private String descricaoGupy;
	private String dataGupy;
	// Relacionamentos 1
	// Relacionamentos N

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
			obj.put("cliente", cliente);
			obj.put("dataInicio", dataInicio);
			obj.put("dataTermino", dataTermino);
			obj.put("consultoria", consultoria);
			obj.put("descricaoLivre", descricaoLivre);
			obj.put("principaisTecnologias", principaisTecnologias);
			obj.put("tituloFuncao", tituloFuncao);
			obj.put("descricaoGupy", descricaoGupy);
			obj.put("dataGupy", dataGupy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}


	public void setCliente(String valor) { 
		this.cliente = valor;
	}
	public String getCliente() { 
		return this.cliente;
	}
	public void setDataInicio(String valor) { 
		this.dataInicio = valor;
	}
	public String getDataInicio() { 
		return this.dataInicio;
	}
	public void setDataTermino(String valor) { 
		this.dataTermino = valor;
	}
	public String getDataTermino() { 
		return this.dataTermino;
	}
	public void setConsultoria(String valor) { 
		this.consultoria = valor;
	}
	public String getConsultoria() { 
		return this.consultoria;
	}
	public void setDescricaoLivre(String valor) { 
		this.descricaoLivre = valor;
	}
	public String getDescricaoLivre() { 
		return this.descricaoLivre;
	}
	public void setPrincipaisTecnologias(String valor) { 
		this.principaisTecnologias = valor;
	}
	public String getPrincipaisTecnologias() { 
		return this.principaisTecnologias;
	}
	public void setTituloFuncao(String valor) { 
		this.tituloFuncao = valor;
	}
	public String getTituloFuncao() { 
		return this.tituloFuncao;
	}
	public void setDescricaoGupy(String valor) { 
		this.descricaoGupy = valor;
	}
	public String getDescricaoGupy() { 
		return this.descricaoGupy;
	}
	public void setDataGupy(String valor) { 
		this.dataGupy = valor;
	}
	public String getDataGupy() { 
		return this.dataGupy;
	}

}
