package dblp;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NameNotFoundException;

import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoAuthorsWithSuchNameException;
import exceptions.NoPaperWithSuchIDException;

public class Corpus {

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
	 * @throws NoPaperWithSuchIDException 
	 *
	 */
	public Paper getPaperByID(int id) throws NoPaperWithSuchIDException {
		ArrayList<Paper> papersList = this.getPapers();
		for (Paper p : papersList) {
			if (id==p.getPaperID()) {
				return p;
			}
		}
		throw new NoPaperWithSuchIDException("ID: " + id);
	}
	
	/**
	 * Restituisce l'oggetto Author associato all'id dato.
	 * 
	 * @param id
	 * @return oggetto Author associato all'id dato
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public Author getAuthorByID(int id) throws NoAuthorsWithSuchIDException {
		ArrayList<Author> authorsList = this.getAuthors();
		for (Author a : authorsList) {
			if (id == a.getAuthorID()) {
				return a;
			}
		}
		throw new NoAuthorsWithSuchIDException("ID: " + id);
	}
	
	/**
	 * Restituisce l'oggetto Author associato al nome dato.
	 * 
	 * @param name
	 * @return oggetto Author associato al nome dato
	 * @throws NameNotFoundException
	 * @throws NoAuthorsWithSuchNameException 
	 */
	public Author getAuthorByName(String name) throws NoAuthorsWithSuchNameException {
		ArrayList<Author> authorsList = this.getAuthors();
		for (Author a : authorsList) {
			if (name.equals(a.getName())) {
				return a;
			}
		}
		throw new NoAuthorsWithSuchNameException("There is no author named '" + name + "' in the Corpus.");
	}
	
	/**
	 * Restituisce l'idf di una keyword.
	 * 
	 * @param keyword
	 * @return idf di una keyword
	 *
	 */
	public double getIDF(String keyword) {
		double idf = 0;
		int m = 0;
		int N = this.getCardinality();
		ArrayList<Paper> papersList = this.getPapers();

		// conta il numero di occorrenze della keyword s nel corpus
		for(Paper p : papersList) {
			TreeMap<String, Integer> keywordSet = p.getKeywordSetWithOccurrences();
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
