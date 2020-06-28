package it.polito.tdp.crimes.model;

public class Adiacenza {
	private int d1;
	private int d2;
	private double peso;
	
	
	
	
	
	public Adiacenza(int d1, int d2, double peso) {
		this.d1 = d1;
		this.d2 = d2;
		this.peso = peso;
	}
	public int getD1() {
		return d1;
	}
	public void setD1(int d1) {
		this.d1 = d1;
	}
	public int getD2() {
		return d2;
	}
	public void setD2(int d2) {
		this.d2 = d2;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	

}
