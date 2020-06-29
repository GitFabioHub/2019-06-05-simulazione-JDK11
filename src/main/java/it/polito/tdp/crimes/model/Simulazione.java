package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.crimes.model.Crimine.Stato;
import it.polito.tdp.crimes.model.Evento.Type;


public class Simulazione {
	
	private int NA=5;
	private final Duration TEMPO_STANDARD=Duration.ofHours(2);
	private final Duration TIMEOUT=Duration.ofMinutes(15);
	private final LocalTime oraInizio=LocalTime.of(00, 00);
	private final Duration TICK=Duration.ofMinutes(5);
	
	
	private int casiMalGestiti;
	
	private List<Agente>agenti;
	private List<Crimine>crimini;
	
	private PriorityQueue<Crimine> attesa ;
	private int agentiDisponibili;
	private Model model;
	
	
	private PriorityQueue<Evento> queue;
	
	
	
	public void init(Model model) {
		
		casiMalGestiti=0;
		agenti=new ArrayList<>();
		crimini=new ArrayList<>(model.getCrimini());
		queue=new PriorityQueue<>();
		attesa=new PriorityQueue<>();
		agentiDisponibili=NA;
		int distretto=model.distrettoBuono();
		this.model=model;
		
		for(int j=0;j<NA;j++) {
			agenti.add(new Agente(distretto));	
			
		}
		for(Crimine c :crimini) {
			Evento e =new Evento(Type.NUOVO_CRIMINE,c.getData(),null,c);
			queue.add(e);	
		}
		Evento e=new Evento(Type.TICK,oraInizio,null,null);
		queue.add(e);
	
	}
	public void run() {
		while (!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			
			processEvent(e);
		}
	}
	private void processEvent(Evento e) {
		
		
		Crimine c=e.getCrimine();
		switch (e.getType()) {
		
		case NUOVO_CRIMINE:
			
			c.setStato(Stato.GESTIRE);
			attesa.add(c);
			Evento e2=new Evento(Type.TIMEOUT_CRIMINE,e.getTime().plus(TIMEOUT),null,c);
			queue.add(e2);
			break;
		case AGENTE_DISPONIBILE:
			
			if(this.agentiDisponibili==0)
				break;
			
			Crimine prossimo=attesa.poll();
			if(prossimo!=null)
				this.agentiDisponibili--;
			Agente a=this.bestAgente(prossimo.getDistretto());
			double distanza=this.model.getGrafo().getEdgeWeight(this.model.getGrafo().getEdge(prossimo.getDistretto(), a.getDistretto()));
			Duration durata=Duration.ofSeconds((long) ((distanza/60)*3600));
			
			
			this.bestAgente(prossimo.getDistretto()).setStato(Agente.Stato.IN_VIAGGIO);
			Evento e3=new Evento(Type.ARRIVO_AGENTE,e.getTime().plus(durata),a,prossimo);
			queue.add(e3);	
			break;
		case ARRIVO_AGENTE:
			
			Crimine cr=e.getCrimine();
			e.getAgente().setDistretto(cr.getDistretto());
			if(cr.getStato()==Stato.MALGESTITO) {
				e.getAgente().setStato(Agente.Stato.DISPONIBILE);
				Evento e4=new Evento(Type.AGENTE_DISPONIBILE,e.getTime(),null,null);
				queue.add(e4);
				this.agentiDisponibili++;
			}
			else {
				if(cr.getStato()==Stato.GESTIRE) {
					if(cr.getNome().compareTo("all_other_crimes")==0) {
						Random r=new Random();
						if(r.nextFloat()<0.5) {
							e.getAgente().setStato(Agente.Stato.OCCUPATO);
							Evento e4=new Evento(Type.PROCESSATO,e.getTime().plus(Duration.ofHours(1)),e.getAgente(),e.getCrimine());
							queue.add(e4);
							
						}else {
							e.getAgente().setStato(Agente.Stato.OCCUPATO);
							Evento e4=new Evento(Type.PROCESSATO,e.getTime().plus(TEMPO_STANDARD),e.getAgente(),e.getCrimine());
							queue.add(e4);
						}
						
					}
					else {
					e.getAgente().setStato(Agente.Stato.OCCUPATO);
					Evento e4=new Evento(Type.PROCESSATO,e.getTime().plus(TEMPO_STANDARD),e.getAgente(),e.getCrimine());
					queue.add(e4);
					}
				}
			}
			break;
		case TIMEOUT_CRIMINE:
			e.getCrimine().setStato(Stato.MALGESTITO);
			this.casiMalGestiti++;
			attesa.remove(e.getCrimine());
			
			break;
		
		case PROCESSATO:
			
			e.getCrimine().setStato(Stato.GESTITO);
			e.getAgente().setStato(Agente.Stato.DISPONIBILE);
			this.agentiDisponibili++;
			attesa.remove(e.getCrimine());
			Evento e6=new Evento(Type.AGENTE_DISPONIBILE,e.getTime(),e.getAgente(),null);
			queue.add(e6);
			break;
			
		case TICK:
			
			if(this.agentiDisponibili > 0) {
				this.queue.add(new Evento(Type.AGENTE_DISPONIBILE,e.getTime(),null, null));
			}
			
			if(e.getTime().isBefore(LocalTime.of(23, 55)))
				this.queue.add(new Evento(Type.TICK,e.getTime().plus(TICK),null,null));
			break;
			
		
		}	
			
	}
	
	
	
	public Agente bestAgente(int distretto) {
		
		List<Agente>list=new ArrayList<>();
		for(Agente a:this.agenti) 
			if(a.isDisponibile())
				list.add(a);
		
		Agente best = null;
		double shortestPath=Double.MAX_VALUE;
		for(Agente a:list) {
			if(a.getDistretto()==distretto) {
			    best=a;
			    shortestPath=0;
			}else {
				
			double distanza=this.model.getGrafo().getEdgeWeight(this.model.getGrafo().getEdge(distretto, a.getDistretto()));
			if (distanza<shortestPath) {
				best=a;
				shortestPath=distanza;
				
			}		
			}
		}
		return  best;
		
	}
	public int getCasiMalGestiti() {
		return casiMalGestiti;
	}
	public void setCasiMalGestiti(int casiMalGestiti) {
		this.casiMalGestiti = casiMalGestiti;
	}
	public void setNA(int nA) {
		NA = nA;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
