package gerador.obtemoportunidadefree.loopback;


import com.strongloop.android.loopback.RestAdapter;

import br.com.gersis.daobase.DaoBase;
import br.com.gersis.daobase.IDatasetComum;
import br.com.gersis.daobase.comum.DaoBaseComum;
import br.com.gersis.loopback.repositorio.*;

public abstract class DaoAplicacao extends DaoBase {

	private RestAdapter adapter = new RestAdapter(DaoBaseComum.urlLoopback);
	protected RepositorioOportunidadeLinkedin repOportunidadeLinkedin = adapter.createRepository(RepositorioOportunidadeLinkedin.class);
	protected RepositorioPalavraRaiz repPalavraRaiz = adapter.createRepository(RepositorioPalavraRaiz.class);
	protected RepositorioPalavraQuantidade repPalavraQuantidade = adapter.createRepository(RepositorioPalavraQuantidade.class);
	protected RepositorioPalavraChave repPalavraChave = adapter.createRepository(RepositorioPalavraChave.class);
	protected RepositorioOportunidadePalavra repOportunidadePalavra = adapter.createRepository(RepositorioOportunidadePalavra.class);
	protected RepositorioExperienciaProfissionalLivre repExperienciaProfissionalLivre = adapter.createRepository(RepositorioExperienciaProfissionalLivre.class);
	protected RepositorioOportunidadeFree repOportunidadeFree = adapter.createRepository(RepositorioOportunidadeFree.class);


	@Override
	protected long getTempo() {
		return 5000;
	}

	@Override
	protected IDatasetComum criaDataSet() {
		return new DatasetAplicacao();
	}

	@Override
	protected DaoBase getProximo() {
		return null;
	} 

}
