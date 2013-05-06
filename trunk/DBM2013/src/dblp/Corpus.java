package dblp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NameNotFoundException;

public class Corpus {

	//private static Corpus instance = null; // riferimento all' istanza
	private ArrayList<Author> authors;
	private ArrayList<Paper> papers;
	private int cardinality;
	
	public Corpus(ArrayList<Author> authors, ArrayList<Paper> papers, int cardinality) {
		super();
		this.authors = authors;
		this.papers = papers;
		this.cardinality = cardinality;
	}
	
	/**
	 * Restituisce l'idf di una keyword.
	 * 
	 * @param keyword
	 * @return idf di una keyword
	 * @throws Exception
	 */
	//FIXME Sostituire con eccezione appropriata
	public double getIDF(String keyword) throws Exception {
		double idf = 0;
		int m = 0;
		int N = this.getCardinality();

		// conta il numero di occorrenze della keyword s nel corpus
		for(Paper p : papers) {
			HashMap<String, Integer> keywordSet = p.getKeywordSet();
			Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
				if (k.getKey().equals(keyword)) {
					m += k.getValue();
				}
			}				
		}
		if (N > 0 && m > 0)
		idf = Math.log((double)N/m);
		return idf;
	}
	
	/**
	 * Restituisce l'oggetto Author associato al nome dato.
	 * 
	 * @param name
	 * @return oggetto Author associato al nome dato
	 * @throws NameNotFoundException
	 */
	public Author getAuthorByName(String name) throws NameNotFoundException {
		for (Author a : authors) {
			if (name.equals(a.getName())) {
				return a;
			}
		}
		throw new NameNotFoundException("There is no author named '" + name + "' in the Corpus.");
	}
	
	/**
	 * Restituisce l'oggetto Author associato all'id dato.
	 * 
	 * @param id
	 * @return oggetto Author associato all`id dato
	 * @throws Exception
	 */
	public Author getAuthorByID(int id) throws Exception {
		for (Author a : authors) {
			if (id==a.getAuthorID()) {
				return a;
			}
		}
		//FIXME: sistemare con eccezione appropriata
		throw new Exception();
	}
		
	/**
	 * Estrae i coautori di un autore dato.
	 * 
	 * @param a
	 * @return lista di Author: coautori di un autore dato
	 * @throws Exception
	 */
	//FIXME: sostituire con exception appropriata
	public List<Author> getCoAuthors(Author a) throws Exception {
		List<Author> coAuthors = new ArrayList<Author>();
		List<Integer> coAuthorsIDs = a.getCoAuthorsIDs();
		
		for (int coA : coAuthorsIDs) {
			coAuthors.add(this.getAuthorByID(coA));
		}
			
		return coAuthors;
	}
	
	/**
	 * Estrae i coautori di un autore dato, insieme all'autore stesso.
	 * 
	 * @param a
	 * @return lista di Author: coautori di un autore dato + autore stesso
	 * @throws Exception
	 */
	//FIXME: sostituire con exception appropriata
	public List<Author> getCoAuthorsAndSelf(Author a) throws Exception {
		List<Author> coAuthorsAndSelf = this.getCoAuthors(a);
		coAuthorsAndSelf.add(a);

		return coAuthorsAndSelf;
	}
	
	/**
	 * Estrae la lista dei paper comuni ad una lista di autori (corpus ristretto).
	 * 
	 * @param authors: i coautori dell'autore in esame
	 * @return lista di Paper: paper comuni ad una lista di autori
	 */
	public List<Paper> getRestrictedCorpus(List<Author> authors) {
		List<Paper> papers = new ArrayList<Paper>();
		
		for (Author a : authors) {
			for (Paper p : a.getPapers()) {
				if (!papers.contains(p)) {
					papers.add(p);
				}
			}
		}
		
//		System.out.println("xxxxxxxxxxxx");
//		
//		for (Paper pp : papers) {
//			System.out.println(pp);
//		}
		
		return papers;
	} 
	
	/**
	 * Calcola il numero di occorrenze di una keyword nel corpus ristretto,
	 * ovvero il corpus formato dai paper di un autore e dei suoi coautori.
	 * 
	 * @param keyword
	 * @param author
	 * @param authors
	 * @return IDF calcolato in base al corpus ristretto
	 */
//	public double getRestrictedIDF(String keyword, Author author, List<Author> authors) {
//		double idf = 0;
//		int m = 0;
//		List<Paper> rc = getRestrictedCorpus(author, authors);
//		int N = rc.size();		
//		// conta il numero di occorrenze della keyword s nel corpus ristretto		
//		for(Paper p : rc) {
//			HashMap<String, Integer> keywordSet = p.getKeywordSet();
//			Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
//				if (k.getKey().equals(keyword)) {
//					m += k.getValue();
//				}
//			}				
//		}		
//		if (N > 0 && m > 0) {
//			idf = Math.log((double)N/m);
//		}	
//		return idf;
//	}
	
	
	/**
	 * Restituisce il keyword vector sotto forma di sequenza di coppie <keyword,weight>
	 * rispetto al modello di pesi TFIDF2, che per il calcolo del tf considera
	 * l'insieme di tutti gli articoli scritti dall'autore dato e per il calcolo dell'idf
	 * considera l'insieme degli articoli scritti dall'autore e dai suoi coautori.
	 * 
	 * @param c
	 * @return keywordVector pesato in base al modello TFIDF2
	 * @throws Exception
	 */
	
//TODO
//	public Map<String, Double> getTFIDF2Vector(Corpus c, Author author ,List<Author> authors, List<Paper> papers) throws Exception {
//		Map<String, Double> keywordVectorTFIDF2 = new TreeMap<String, Double>();		
//		for (Paper p : papers){
//			HashMap<String, Integer> keywordSet = p.getKeywordSet();
//			double tfidf2;
//			String key;
//			Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
//				key = k.getKey();
//				tfidf2 = author.getRestrictedTF(k.getKey()) * c.getRestrictedIDF(k.getKey(), author, authors);
//				keywordVectorTFIDF2.put(key, tfidf2);
//			}
//		}
//		return keywordVectorTFIDF2;
//	}
		
	/**
	 * Estrae gli articoli dei coautori di un autore dato.
	 * 
	 * @param a
	 * @return lista di Paper: elenco dei paper dei coautori di un autore dato
	 * @throws Exception
	 */
	//FIXME: sostituire con exception appropriata
	public List<Paper> getCoAuthorsPapers(Author a) throws Exception {
		List<Author> coAuthors = this.getCoAuthors(a);
		List<Paper> coAuthorsPapers = new ArrayList<Paper>();
		
//		List<Paper> authorsPapers = new ArrayList<Paper>();
//		for (Author coA : coAuthors) {
//			authorsPapers = coA.getPapers();
//			for (Paper p : authorsPapers) {
//				if(!coAuthorsPapers.contains(p)) {
//					coAuthorsPapers.add(p);
//				}
//			}
//		}
		
		coAuthorsPapers = this.getRestrictedCorpus(coAuthors);
		
		return coAuthorsPapers;
	}
	
	/**
	 * Estrae gli articoli dei coautori di un autore dato, insieme a quelli dell'autore stesso.
	 * 
	 * @param a
	 * @return lista di Paper: elenco degli articoli di un autore dato insieme a quelli dell'autore stesso
	 * @throws Exception
	 */
	//FIXME: sostituire con exception appropriata
	public List<Paper> getCoAuthorsAndSelfPapers(Author a) throws Exception {
		List<Paper> coAuthorsAndSelfPapers = this.getCoAuthorsPapers(a);
		
		List<Paper> selfPapers = a.getPapers();
		for (Paper p : selfPapers) {
			if(!coAuthorsAndSelfPapers.contains(p)) {
				coAuthorsAndSelfPapers.add(p);
			}
		}
		
		//coAuthorsAndSelfPapers.addAll(a.getPapers());

		return coAuthorsAndSelfPapers;
	}
	
	/**
	 * Calcola il numero di articoli dei soli coautori dell'autore a_i che non contengono la chiave k_j.
	 * 
	 * @param a_i
	 * @param k_j
	 * @return numero di articoli dei soli coautori dell'autore a_i che non contengono la chiave k_j
	 * @throws Exception
	 */
	//FIXME: sostituire con exception appropriata
	public int r_withoutKey(Author a_i, String k_j) throws Exception {
		List<Paper> coAuthorsPapers = this.getCoAuthorsPapers(a_i);
		int r_ij = 0;
		
		for (Paper p : coAuthorsPapers) {
			if(!p.containsKeyword(k_j)) {
				r_ij++;
			}
		}		
		
		return r_ij;
	}
	
	/** Calcola il numero di articoli dell'autore a_i e dei suoi coautori che non contengono la chiave k_j.
	 * 
	 * @param a_i
	 * @param k_j
	 * @return numero intero: gli articoli dell'autore a_i e dei suoi coautori che non contengono la chiave k_j
	 * @throws Exception
	 */
	public int n_withoutKey(Author a_i, String k_j) throws Exception {
		List<Paper> coAuthorsPapers = this.getCoAuthorsAndSelfPapers(a_i);
		int n_ij = 0;
		
		for (Paper p : coAuthorsPapers) {
			if(!p.containsKeyword(k_j)) {
				n_ij++;
			}
		}		
		
		return n_ij;
	}
	
	/**
	 * Calcola il peso u_ij della keyword k_j per l'autore a_i.
	 * 
	 * @param a_i
	 * @param k_j
	 * @return peso u_ij della keyword k_j per l'autore a_i
	 * @throws Exception
	 */
	//FIXME: sostituire con eccezione appropriata...
	public double u_ij(Author a_i, String k_j) throws Exception{
		double u_ij = 0.0;
		double epsilon = 0.1; // costante che non fa andare a zero		
		double numLog = 0.0;
		double denLog = 0.0;
		double resLog = 0.0;
		double resAbs = 0.0;
		
		int r_ij = this.r_withoutKey(a_i,  k_j);
		int n_ij = this.n_withoutKey(a_i, k_j);
		int R_i = this.getCoAuthorsPapers(a_i).size();
		int N_i = this.getCoAuthorsAndSelfPapers(a_i).size();
		
		numLog = (double) (r_ij)/ (R_i - r_ij + epsilon);
		denLog = (double) (n_ij - r_ij + epsilon) / (N_i - n_ij - R_i + r_ij + epsilon);
		//FIXME: aggiunto 1 + ... all'argomento del logaritmo (come visto il 2 maggio a lezione)
		resLog = Math.log(1 + numLog / denLog);
		
		resAbs = Math.abs((double)(r_ij + epsilon / R_i + epsilon) - (double)((n_ij - r_ij + epsilon) / (N_i - R_i + epsilon)));
		
		u_ij = resLog * resAbs;
		
		return u_ij;
	}
		
	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}

	public int getCardinality() {
		return cardinality;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Corpus [authors=" + authors + ", papers=" + papers
				+ ", cardinality=" + cardinality + "]";
	}
}
