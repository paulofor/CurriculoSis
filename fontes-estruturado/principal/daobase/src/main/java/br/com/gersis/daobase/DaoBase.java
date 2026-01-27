package br.com.gersis.daobase;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.List;

public abstract class DaoBase {


	protected DummyDaoBase dummy = null;
	
	private IDatasetComum comum = null;
	private int contadorObj = 0;
	private DaoBase proximo = null;
	private static int contador = 0;
	private Thread minhaThread;
	
	
	
	
	public void setThread(Thread thread) {
		this.minhaThread = thread;
	}
	public void finalizaThread() {
		System.out.println("Minha thread: " + this.minhaThread);
		this.minhaThread.stop();
		this.minhaThread = null;
	}
	
	public DaoBase() {
		this.contadorObj = ++contador;
		
	}
	protected abstract long getTempo();
	
	protected boolean concluido = false;
	public void executa() {
		System.out.println("Executando " + this.getClass().getName());
		this.concluido = false;
		executaImpl();
		while (!this.concluido) {
			try {
				Thread.sleep(getTempo());
				//System.out.println(this.getNumPasso() + "-" + this.toString() + " não terminou");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	protected abstract void executaImpl();
	protected abstract int getNumPasso();
	
	public boolean getConcluido() {
		return this.concluido;
	}
	
	public void setConcluido() {
		this.concluido = true;
	}
	
	
	protected void onErrorBase(Throwable t) {
		//System.out.println("Erro[" + this + "] : " + t.getMessage());
		t.printStackTrace();
		System.exit(-1);
	}

	public IDatasetComum getComum() {
		if(comum==null) comum = criaDataSet();
		return comum;
	}
	protected abstract IDatasetComum criaDataSet();
	
	private DaoBase proximoExec() {
		if (this.proximo != null) {
			return this.proximo;
		} else {
			this.proximo = this.getProximo();
			if (this.proximo!=null) {
				return this.proximo;
			} else {
				throw new RuntimeException(this.getClass().getSimpleName() + " precisa implementar getProximo()");
			}
		}
	}

	public void setComum(IDatasetComum comum) {
		this.comum = comum;
	}
	
	protected abstract DaoBase getProximo();
	
	// usado sem loops
	public void executaProximo() {
		this.executaProximoSemFinalizar();
		this.finalizar();
	}
	
	// Usado para loops
	public void executaProximoSemFinalizar() { // dentro do loop
		proximoExec().setComum(getComum());
		//System.out.println("*** I-" + this.contadorObj + "-" + this.getClass().getSimpleName());
		//System.out.println("Total Number of threads " + ManagementFactory.getThreadMXBean().getThreadCount());
		proximoExec().executa();
		//System.out.println("*** F-" + this.contadorObj + "-" + this.getClass().getSimpleName());
	}
	public void finalizar() { // ao final do loop
		//System.out.println("*** Concluido-" + this.contadorObj + "-" + this.getClass().getSimpleName());
		concluido = true;
		//System.out.println("Total Number of threads " + ManagementFactory.getThreadMXBean().getThreadCount());
	}

	public void executaEmThread(List<DaoBase> listaDaoBase) {
		for (DaoBase item : listaDaoBase) {
			DaoBaseThread thread = new DaoBaseThread(item);
			thread.start();
		}
		
	}

	
	
}
