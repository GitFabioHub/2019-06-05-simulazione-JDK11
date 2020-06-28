package it.polito.tdp.crimes.model;

import java.time.Duration;

public class AgenteVelocita {
	
	private Agente a;
	private Duration durata;
	public AgenteVelocita(Agente a, Duration durata) {
		super();
		this.a = a;
		this.durata = durata;
	}
	public Agente getA() {
		return a;
	}
	public void setA(Agente a) {
		this.a = a;
	}
	public Duration getDurata() {
		return durata;
	}
	public void setDurata(Duration durata) {
		this.durata = durata;
	}
	

}
