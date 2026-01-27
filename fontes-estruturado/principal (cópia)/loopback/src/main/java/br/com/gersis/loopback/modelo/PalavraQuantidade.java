package br.com.gersis.loopback.modelo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongloop.android.loopback.Model;
import com.strongloop.android.remoting.BeanUtil;
import org.json.JSONObject;


public class PalavraQuantidade extends Model {


	private int quantidade;
	private String data;
	private int palavraRaizId;
	private int palavraChaveId;
	// Relacionamentos 1
	private PalavraChave PalavraChave;
	private PalavraRaiz PalavraRaiz;
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
			obj.put("quantidade", quantidade);
			obj.put("data", data);
			obj.put("palavraRaizId", palavraRaizId);
			obj.put("palavraChaveId", palavraChaveId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}


	public void setQuantidade(int valor) { 
		this.quantidade = valor;
	}
	public int getQuantidade() { 
		return this.quantidade;
	}
	public void setData(String valor) { 
		this.data = valor;
	}
	public String getData() { 
		return this.data;
	}
	public void setPalavraRaizId(int valor) { 
		this.palavraRaizId = valor;
	}
	public int getPalavraRaizId() { 
		return this.palavraRaizId;
	}
	public void setPalavraChaveId(int valor) { 
		this.palavraChaveId = valor;
	}
	public int getPalavraChaveId() { 
		return this.palavraChaveId;
	}

	public PalavraChave getPalavraChave() {
		return PalavraChave;
	}
	public void setPalavraChave(HashMap valor) {
		this.PalavraChave = new PalavraChave();
		BeanUtil.setProperties(this.PalavraChave, (Map<String, ? extends Object>) valor, true);
	}
	public PalavraRaiz getPalavraRaiz() {
		return PalavraRaiz;
	}
	public void setPalavraRaiz(HashMap valor) {
		this.PalavraRaiz = new PalavraRaiz();
		BeanUtil.setProperties(this.PalavraRaiz, (Map<String, ? extends Object>) valor, true);
	}
}
