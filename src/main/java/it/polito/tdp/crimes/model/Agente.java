package it.polito.tdp.crimes.model;

public class Agente {
	
	private int distretto;
	private Stato stato;
	public enum Stato{
		DISPONIBILE,
		IN_VIAGGIO,
		OCCUPATO;
	}
	public Agente(int distretto) {
		this.distretto = distretto;
		this.stato=Stato.DISPONIBILE;
	}
	public int getDistretto() {
		return distretto;
	}
	public void setDistretto(int distretto) {
		this.distretto = distretto;
	}
	public Stato getStato() {
		return stato;
	}
	public void setStato(Stato stato) {
		this.stato = stato;
	}
	
	
	public boolean isDisponibile() {
		if (this.stato==Stato.DISPONIBILE)
			return true;
			else return false;
	}

}
