package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private ItunesDAO dao;
	private Graph<Album, DefaultWeightedEdge> grafo;
	//varibili per la ricorsione
	private List<Album> migliorPercorso;
	private double BestScore;
	private double soglia;
	private Album destinazione;
	private Album sorgente;
	private double bilancioMinimo;
	
	public Model() {
		this.dao=new ItunesDAO();
	}

	public List<Album> creaGrafo(double n) {
		this.grafo=new SimpleDirectedWeightedGraph<Album, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Album> vertici=this.dao.getAllAlbums(n);
		Graphs.addAllVertices(this.grafo, vertici);
		for (Album a1: this.grafo.vertexSet()) {
			for (Album a2: this.grafo.vertexSet()) {
				if(a1.getAlbumId()>a2.getAlbumId() && a1.getDurata()!=a2.getDurata() && a1.getDurata()+a2.getDurata()>4*n) {
					if(a1.getDurata()>a2.getDurata()) {
						DefaultWeightedEdge e=this.grafo.addEdge(a2, a1);
						this.grafo.setEdgeWeight(e, a1.getDurata()+a2.getDurata());
					}else {
						DefaultWeightedEdge e=this.grafo.addEdge(a1, a2);
						this.grafo.setEdgeWeight(e, a1.getDurata()+a2.getDurata());
					}
				}
			}
		}
		
		System.out.println("vertici: "+this.grafo.vertexSet().size());
		System.out.println("archi: "+this.grafo.edgeSet().size());
		return vertici;
	}

	public List<Album> getAdiacenze(Album a1) {
		
		if(this.grafo.vertexSet().isEmpty()) {
			return null;
		}
		List<Album> successori=Graphs.successorListOf(this.grafo, a1);
		
		for(Album a: successori) {
			double bilancio=0;
			Set<DefaultWeightedEdge> entranti=this.grafo.incomingEdgesOf(a);
			Set<DefaultWeightedEdge> uscenti=this.grafo.outgoingEdgesOf(a);
			
			for(DefaultWeightedEdge e: entranti) {
				bilancio+=this.grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e: uscenti) {
				bilancio-=this.grafo.getEdgeWeight(e);
			}
			a.setBilancio(bilancio);
		}
		Collections.sort(successori);
		return successori;
		
		
	}

	public List<Album> calcolaPercorso(Album a1, Album a2, double x) {
		System.out.println(this.grafo.vertexSet());
		this.sorgente=a1;
		this.destinazione=a2;
		this.soglia=x;
		this.BestScore=0;
		this.bilancioMinimo=getBilancio(a1);
		this.migliorPercorso=new ArrayList<Album>();
		List<Album> parziale =new ArrayList<>();
		parziale.add(sorgente);
		
		cerca(parziale,Graphs.successorListOf(this.grafo, sorgente));	
		return this.migliorPercorso;
	}
	
	private void cerca(List<Album> parziale, List<Album> successori) {
		
		Album last=parziale.get(parziale.size()-1);
	
		//condizione terminazione
		if(last.equals(this.destinazione)) {
			if(score(parziale)>this.BestScore) {
				System.out.println("trovato migliore");
				this.migliorPercorso=new ArrayList<>(parziale);
				this.BestScore=score(parziale);
			}
			return;
		}
		if(successori.size()==0) {
			return;
		}
		System.out.println(successori);
		
		for(Album a: successori) {
			DefaultWeightedEdge e=this.grafo.getEdge(last, a);
			if(this.grafo.getEdgeWeight(e)>=this.soglia && !parziale.contains(a)) {
				System.out.println("aggiungo "+a);
				parziale.add(a);
				cerca(parziale,Graphs.successorListOf(this.grafo, a));
				System.out.println("rimuovo "+a);
				parziale.remove(parziale.size()-1);
		}
		
		}
	
	}

	private double score(List<Album> parziale) {
		int punteggio=0;
		for(Album a:parziale) {
			if(getBilancio(a)>bilancioMinimo)
				punteggio++;
		}
		
		return punteggio;
	}
	
	private double getBilancio(Album a) {
		double bilancio=0;
		Set<DefaultWeightedEdge> entranti=this.grafo.incomingEdgesOf(a);
		Set<DefaultWeightedEdge> uscenti=this.grafo.outgoingEdgesOf(a);
			
		for(DefaultWeightedEdge e: entranti) {
			bilancio+=this.grafo.getEdgeWeight(e);
		}
		for(DefaultWeightedEdge e: uscenti) {
			bilancio-=this.grafo.getEdgeWeight(e);
		}
		a.setBilancio(bilancio);
		return bilancio;
		
	}
}
