package br.com.gersis.daobase;

import java.lang.management.ManagementFactory;

public class DaoBaseThread extends Thread{
	
	private DaoBase base;
	
	public DaoBaseThread(DaoBase base) {
		super();
		this.base = base;
		this.base.setThread(this);
	}

	@Override
	public void run() {
		//System.out.println(this + " iniciada");
		this.base.executaImpl();
		this.base = null;
		//System.out.println(this + " finalizada");
	}

}
