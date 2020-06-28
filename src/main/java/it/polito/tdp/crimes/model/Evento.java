package it.polito.tdp.crimes.model;

import java.time.LocalTime;

public class Evento implements Comparable<Evento>{
	
	public enum Type{
		
		NUOVO_CRIMINE,
		AGENTE_DISPONIBILE,
		ARRIVO_AGENTE,
		TIMEOUT_CRIMINE,
		PROCESSATO,
		TICK;
		
		
	}
	
	private Type type;
	private LocalTime time;
	private Agente agente;
	private Crimine crimine;
	public Evento(Type type, LocalTime time, Agente agente, Crimine crimine) {
		this.type = type;
		this.time = time;
		this.agente = agente;
		this.crimine=crimine;
	}
	public Type getType() {
		return type;
	}
	public LocalTime getTime() {
		return time;
	}
	public Agente getAgente() {
		return agente;
	}
	@Override
	public int compareTo(Evento o) {
		
		return this.time.compareTo(o.getTime());
	}
	public Crimine getCrimine() {
		return crimine;
	}
	
	
}
