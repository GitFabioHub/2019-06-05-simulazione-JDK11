package it.polito.tdp.crimes.model;

import java.time.LocalTime;

public class Crimine {
	
	private String nome;
	private int distretto;
	private LocalTime data;
	private Stato stato;
	public enum Stato{ 
		ZERO,
		GESTIRE,
	    MALGESTITO,
		GESTITO;
	}
	public Crimine(String nome, int distretto, LocalTime data) {
		this.stato=Stato.ZERO;
		this.nome = nome;
		this.distretto = distretto;
		this.data = data;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getDistretto() {
		return distretto;
	}
	public void setDistretto(int distretto) {
		this.distretto = distretto;
	}
	public LocalTime getData() {
		return data;
	}
	public void setData(LocalTime data) {
		this.data = data;
	}
	
	public  Stato getStato(){
		return this.stato;
	
	}
	public void setStato(Stato stato) {
		this.stato = stato;
	}
	
	

}
