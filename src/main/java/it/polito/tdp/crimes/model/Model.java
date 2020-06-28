package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private Graph<Integer,DefaultWeightedEdge>grafo;
	private EventsDao dao;
	private int anno;
	private int mese;
	private int giorno;

	
	public Model() {
		
		dao=new EventsDao();
	}
	public Collection<Integer> getAnni() {
		return dao.getAnni();
		
	}
	public void creaGrafo(int anno) {
		
		this.anno=anno;
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Adiacenza>adiacenze=new ArrayList<Adiacenza>(dao.getAdiacenze(anno));
		for(Adiacenza a:adiacenze) {
			if(!grafo.containsVertex(a.getD1())) {
				grafo.addVertex(a.getD1());
			}
			else if(!grafo.containsVertex(a.getD2())) {
				grafo.addVertex(a.getD2());
			}
			
			if(this.grafo.getEdge(a.getD1(), a.getD2())==null && grafo.containsVertex(a.getD2()) && grafo.containsVertex(a.getD1())) {	
			Graphs.addEdgeWithVertices(grafo, a.getD1(), a.getD2(), a.getPeso());
			}	
		}
	}
	
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Graph<Integer,DefaultWeightedEdge>getGrafo(){
		return this.grafo;
	}
	
	public Collection<Adiacenza> getVicini(int distretto){
		
		
	List<Adiacenza>l=new ArrayList<>();
		for(int i:Graphs.neighborListOf(this.grafo, distretto)) {
			l.add(new Adiacenza(distretto,i,this.grafo.getEdgeWeight(this.grafo.getEdge(distretto, i))));	
		}
		Collections.sort(l,new ComparatoreAdiacenzaDistanza());
		return l;
		
	}
	
	public int distrettoBuono() {
		return this.dao.getLowRateDist(this.getAnno());
	}
	
	
	public Collection<Crimine>getCrimini(){
		
		return dao.getCrimini(mese, anno, giorno);
	}
	
	public int getAnno() {
		return anno;
	}
	public void setAnno(int anno) {
		this.anno = anno;
	}
	public int getMese() {
		return mese;
	}
	public void setMese(int mese) {
		this.mese = mese;
	}
	public int getGiorno() {
		return giorno;
	}
	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}
	
	
	
	
	
	
	
}
