package br.com.gersis.daobase;

import java.util.Timer;
import java.util.TimerTask;

import br.com.gersis.daobase.comum.DaoBaseRestricaoTempo;

public abstract class DaoBaseRecorrente extends DaoBase {

	private Timer timer;
	private DaoBaseRestricaoTempo restricaoTempo;

	public DaoBaseRecorrente() {
		this.restricaoTempo = new DaoBaseRestricaoTempo();
		inicializaTempos(this.restricaoTempo);
	}

	protected abstract void inicializaTempos(DaoBaseRestricaoTempo restricaoTempo2);

	protected abstract int getIntervaloMinuto();

	@Override
	protected final void executaImpl() {
		timer = new Timer();
		timer.schedule(new RemindTask(), 0, getIntervaloMinuto() * 60 * 1000);
	}

	class RemindTask extends TimerTask {
		public void run() {
			if (restricaoTempo.podeExecutar()) {
				executaPrincipal();
				

			} else {
				if (restricaoTempo.podeFinalizar()) {
					finalizar();
				}
			}
		}
	}

	protected abstract void executaPrincipal();
	
	
}
