package br.com.gersis.loopback.modelo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.remoting.BeanUtil;
import org.json.JSONObject;


public class OportunidadePalavra extends Model {


	private int palavraChaveId;
	private int oportunidadeLinkedinId;
	// Relacionamentos 1
	private OportunidadeLinkedin OportunidadeLinkedin;
	private PalavraChave PalavraChave;
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
			obj.put("palavraChaveId", palavraChaveId);
			obj.put("oportunidadeLinkedinId", oportunidadeLinkedinId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}


	public void setPalavraChaveId(int valor) { 
		this.palavraChaveId = valor;
	}
	public int getPalavraChaveId() { 
		return this.palavraChaveId;
	}
	public void setOportunidadeLinkedinId(int valor) { 
		this.oportunidadeLinkedinId = valor;
	}
	public int getOportunidadeLinkedinId() { 
		return this.oportunidadeLinkedinId;
	}

	public OportunidadeLinkedin getOportunidadeLinkedin() {
		return OportunidadeLinkedin;
	}
	public void setOportunidadeLinkedin(HashMap valor) {
		this.OportunidadeLinkedin = new OportunidadeLinkedin();
		BeanUtil.setProperties(this.OportunidadeLinkedin, (Map<String, ? extends Object>) valor, true);
	}
	public PalavraChave getPalavraChave() {
		return PalavraChave;
	}
	public void setPalavraChave(HashMap valor) {
		this.PalavraChave = new PalavraChave();
		BeanUtil.setProperties(this.PalavraChave, (Map<String, ? extends Object>) valor, true);
	}
}
