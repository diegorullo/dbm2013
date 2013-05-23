package dblp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	 * Restituisce l'oggetto paper associato all'id dato.
	 * 
	 * @param id
	 * @return oggetto Paper associato all'id dato
	 * @throws Exception
	 */
	public Paper getPaperByID(int id) throws Exception {
		for (Paper p : this.papers) {
			if (id==p.getPaperID()) {
				return p;
			}
		}
		//FIXME: sistemare con eccezione appropriata
		throw new Exception();
	}
	
	/**
	 * Restituisce l'oggetto Author associato all'id dato.
	 * 
	 * @param id
	 * @return oggetto Author associato all'id dato
	 * @throws Exception
	 */
	public Author getAuthorByID(int id) throws Exception {
		for (Author a : this.authors) {
			if (id==a.getAuthorID()) {
				return a;
			}
		}
		//FIXME: sistemare con eccezione appropriata
		throw new Exception();
	}
	
	/**
	 * Restituisce l'oggetto Author associato al nome dato.
	 * 
	 * @param name
	 * @return oggetto Author associato al nome dato
	 * @throws NameNotFoundException
	 */
	public Author getAuthorByName(String name) throws NameNotFoundException {
		for (Author a : this.authors) {
			if (name.equals(a.getName())) {
				return a;
			}
		}
		throw new NameNotFoundException("There is no author named '" + name + "' in the Corpus.");
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
			HashMap<String, Integer> keywordSet = p.getKeywordSetWithOccurrences();
			for(Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				if (k.getKey().equals(keyword)) {
					m++;
				}
			}				
		}
		if (N > 0 && m > 0) {
			idf = Math.log((double)N/m);
		}
		return idf;
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
