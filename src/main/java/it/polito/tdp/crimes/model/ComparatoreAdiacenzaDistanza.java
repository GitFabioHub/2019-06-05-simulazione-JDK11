package it.polito.tdp.crimes.model;

import java.util.Comparator;

public class ComparatoreAdiacenzaDistanza implements Comparator<Adiacenza>{

	@Override
	public int compare(Adiacenza o1, Adiacenza o2) {
		return (int)( o1.getPeso()-o2.getPeso());
	}

}
