package validador.marketingdigital.integracao.lookback.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import com.strongloop.android.loopback.Model;

public class VisitaProdutoHotmart extends Model{

	private int hotmartId;
	private String nome;
	private String imagem;
	private double temperatura;
	private String categoria;
	private String formato;
	private double reviewRating;
	private int totalAnswers;
	private int afiliacaoTipo;
	private double afiliacaoValor;
	private double afiliacaoPercentual;
	private String produtorNome;
	private String produtorCodigoHotmart;
	private int blueprint;
	
	public JSONObject getJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("hotmartId", hotmartId);
			obj.put("nome", nome);
			obj.put("imagem", imagem);
			obj.put("temperatura", temperatura);
			obj.put("categoria", categoria);
			obj.put("formato", formato);
			obj.put("reviewRating", reviewRating);
			obj.put("totalAnswers", totalAnswers);
			obj.put("afiliacaoTipo", afiliacaoTipo);
			obj.put("afiliacaoValor", afiliacaoValor);
			obj.put("afiliacaoPercentual", afiliacaoPercentual);
			obj.put("produtorNome", produtorNome);
			obj.put("produtorCodigoHotmart", produtorCodigoHotmart);
			obj.put("blueprint", blueprint);
			//obj.put("afiliacaoTipo", afiliacaoTipo);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public VisitaProdutoHotmart(JSONObject item) throws JSONException {
		JSONObject produto = item.getJSONObject("product");
		JSONObject produtor = item.getJSONObject("producer");
		JSONObject afiliacao = item.getJSONObject("affiliation");
		
		this.hotmartId = produto.getInt("id");
		this.nome = produto.getString("name");
		this.imagem = produto.getString("image");
		this.temperatura = produto.getDouble("temperature");
		this.categoria = produto.getString("category");
		this.formato = produto.getString("format");
		this.reviewRating = produto.getDouble("reviewRating");
		this.totalAnswers = produto.getInt("totalAnswers");
		this.blueprint = produto.getInt("blueprint");
		
		this.afiliacaoTipo = afiliacao.getInt("type");
		this.afiliacaoValor = afiliacao.getJSONObject("commission").getJSONObject("price").getDouble("value");
		this.afiliacaoPercentual = afiliacao.getJSONObject("commission").getDouble("percentage");
		
		this.produtorNome = produtor.getString("name");
		this.produtorCodigoHotmart = produtor.getString("ucode");
	}
	
	
}
