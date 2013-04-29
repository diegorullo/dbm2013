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
	
	// calcola l'idf di una keyword
	public double getIDF(String s) throws Exception {
		double idf = 0;
		int m = 0;
		int N = this.getCardinality();

		// conta il numero di occorrenze della keyword s nel corpus
		for(Paper p : papers) {
			HashMap<String, Integer> keywordSet = p.getKeywordSet();
			Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
				if (k.getKey().equals(s)) {
					m += k.getValue();
				}
			}				
		}
		if (N > 0 && m > 0)
		idf = Math.log((double)N/m);
		return idf;
	}
	
	// restituisce l'oggetto Author associato al nome dato
	public Author getAuthorByName(String name) throws NameNotFoundException {
		for (Author a : authors) {
			if (name.equals(a.getName())) {
				return a;
			}
		}
		throw new NameNotFoundException("There is no author named '" + name + "' in the Corpus.");
	}
	
	// restituisce l'oggetto Author associato all'id dato
	public Author getAuthorByID(int id) throws Exception {
		for (Author a : authors) {
			if (id==a.getAuthorID()) {
				return a;
			}
		}
		//FIXME: sistemare con eccezione appropriata
		throw new Exception();
	}
	
	// estrae i nomi dei coautori di un autore dato
	public List<String> getCoAuthorsNames(Author a) {
		List<String> coAuthorsNames = new ArrayList<String>();
		
		for (Paper p : a.getPapers()) {
			for (String coA : p.getAuthorsNames()) {
				if(!coAuthorsNames.contains(coA) && !a.getName().equals(coA)) {
					coAuthorsNames.add(coA);
				}
			}
		}		
		return coAuthorsNames;
	}
	
	// estrae gli id dei coautori di un autore dato
	public List<Integer> getCoAuthorsIDs(Author a) {
		List<Integer> coAuthorsIDs = new ArrayList<Integer>();
		
		for (Paper p : a.getPapers()) {
			for (int coA : p.getAuthors()) {
				if(!coAuthorsIDs.contains(coA) && a.getAuthorID()!=(coA)) {
					coAuthorsIDs.add(coA);
				}
			}
		}		
		return coAuthorsIDs;
	}
	
	// estrae i coautori di un autore dato
	//FIXME: sostituire con exception appropriata
	public List<Author> getCoAuthors(Author a) throws Exception {
		List<Author> coAuthors = new ArrayList<Author>();
		List<Integer> coAuthorsIDs = this.getCoAuthorsIDs(a);
		
		for (int coA : coAuthorsIDs) {
			coAuthors.add(getAuthorByID(coA));
		}
		
		return coAuthors;
	}
	
	
	// estrae i coautori di un autore dato insieme all'autore stesso
	//FIXME: sostituire con exception appropriata
	public List<Author> getCoAuthorsAndSelf(Author a) throws Exception {
		List<Author> coAuthorsAndSelf = this.getCoAuthors(a);
		coAuthorsAndSelf.add(a);

		return coAuthorsAndSelf;
	}
	
	// estrae la lista dei paper comuni ad una lista di autori (corpus ristretto)
	public List<Paper> getRestrictedCorpus(Author author, List<Author> authors) {
		List<Paper> papers = new ArrayList<Paper>();
		
		for (Author a : authors) {
			for (Paper p : a.getPapers()) {
				if (p.getAuthors().contains(author)) {
					//if (!papers.contains(p)) {
						papers.add(p);
					//}
				}
			}
		}
		
		return papers;
	} 
	
	// conta il numero di occorrenze della keyword s nel corpus ristretto
	public double getRestrictedIDF(String s, Author author, List<Author> authors) {
		double idf = 0;
		int m = 0;
		List<Paper> rc = getRestrictedCorpus(author, authors);
		int N = rc.size();		

		// conta il numero di occorrenze della keyword s nel corpus ristretto		
		for(Paper p : rc) {
			HashMap<String, Integer> keywordSet = p.getKeywordSet();
			Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
				if (k.getKey().equals(s)) {
					m += k.getValue();
				}
			}				
		}
		
		if (N > 0 && m > 0) {
			idf = Math.log((double)N/m);
		}
		
		return idf;
	}
	
	//FIXME: chiedere se è davvero convinta...
	public List<Paper> coauthor_papers(Author a) throws Exception {
		return getCoAuthorsPapers(a);
	}
	
	// estrae i paper dei coautori di un autore dato
	//FIXME: sostituire con exception appropriata
	public List<Paper> getCoAuthorsPapers(Author a) throws Exception {
		List<Author> coAuthors = this.getCoAuthors(a);
		List<Paper> coAuthorsPapers = new ArrayList<Paper>();

		List<Paper> authorsPapers = new ArrayList<Paper>();
		for (Author coA : coAuthors) {
			authorsPapers = null;
			authorsPapers = coA.getPapers();
			for (Paper p : authorsPapers) {
				if(!coAuthorsPapers.contains(p)) {
					coAuthorsPapers.add(p);
				}
			}
		}
		
		return coAuthorsPapers;
	}
	
	
	//FIXME: chiedere se è convinta...
	public List<Paper> coauthor_and_self(Author a) throws Exception {
		return getCoAuthorsAndSelfPapers(a);
	}
	
	// estrae i paper dei coautori di un autore dato, insieme a quelli dell'autore stesso
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
	
	//r_ij e’ il numero di articoli dei coautori in coauthor_papers(a_i) che non contengono la chiave k_j;
	//FIXME: sostituire con exception appropriata
	public int r_notKey(Author a_i, String k_j) throws Exception {
		List<Paper> coAuthorsPapers = this.coauthor_papers(a_i);
		int r_ij = 0;
		
		for (Paper p : coAuthorsPapers) {
			if(!p.containsKeyword(k_j)) {
				r_ij++;
			}
		}		
		
		return r_ij;
	}
	
	public int n_notKey(Author a_i, String k_j) throws Exception {
		List<Paper> coAuthorsPapers = this.coauthor_and_self(a_i);
		int n_ij = 0;
		
		for (Paper p : coAuthorsPapers) {
			if(!p.containsKeyword(k_j)) {
				n_ij++;
			}
		}		
		
		return n_ij;
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
