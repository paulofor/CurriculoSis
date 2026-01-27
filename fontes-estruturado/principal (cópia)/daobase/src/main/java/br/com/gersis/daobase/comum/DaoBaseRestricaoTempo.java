package br.com.gersis.daobase.comum;

import java.util.Calendar;
import java.util.Date;

public class DaoBaseRestricaoTempo {
	
	Date horaInicio;
	Date horaFinal;
	Date horaDesliga;
	Date horaAtual;

	public void setHorarioInicial(int hora, int minuto) {
		Calendar horaInicio = Calendar.getInstance();
		horaInicio.set(11, hora);
		horaInicio.set(12, minuto);
		horaInicio.set(13, 0);
		this.horaInicio = horaInicio.getTime();
		
	}
	public void setHorarioFinal(int hora, int minuto) {
		Calendar horaFinal = Calendar.getInstance();
		horaFinal.set(11, hora);
		horaFinal.set(12, minuto);
		horaFinal.set(13, 0);
		this.horaFinal = horaFinal.getTime();
	}
	public void setHorarioDesliga(int hora, int minuto) {
		Calendar horaDesliga = Calendar.getInstance();
		horaDesliga.set(11, hora);
		horaDesliga.set(12, minuto);
		horaDesliga.set(13, 0);
		this.horaDesliga = horaDesliga.getTime();
	}
	
	public boolean podeExecutar() {
		this.horaAtual = Calendar.getInstance().getTime();
		boolean saida = horaAtual.after(horaInicio) && horaAtual.before(horaFinal);
		System.out.println("Testou tempo - horaAtual:" + horaAtual);
		return saida;
	}
	public boolean podeFinalizar() {
		this.horaAtual = Calendar.getInstance().getTime();
		boolean saida =  horaAtual.after(horaDesliga);
		return saida;
	}

	
}
