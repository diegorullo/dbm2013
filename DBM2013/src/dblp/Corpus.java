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
	
	/** restituisce l`IDF di una keyword
	 * 
	 * @param keyword
	 * @return IDF di una keyword
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
	
	/** restituisce l'oggetto Author associato al nome dato
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
	
	/** restituisce l'oggetto Author associato all`id dato
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
	
	/** estrae i nomi dei coautori di un autore dato
	 * 
	 * @param a
	 * @return lista di stringhe: nomi dei coautori di un autore dato
	 */
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
	
	/** estrae gli id dei coautori di un autore dato
	 * 
	 * @param a
	 * @return lista di interi: id dei coautori di un autore dato
	 */
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
	
	/** estrae i coautori di un autore dato
	 * 
	 * @param a
	 * @return lista di Author: coautori di un autore dato
	 * @throws Exception
	 */
	//FIXME: sostituire con exception appropriata
	public List<Author> getCoAuthors(Author a) throws Exception {
		List<Author> coAuthors = new ArrayList<Author>();
		List<Integer> coAuthorsIDs = this.getCoAuthorsIDs(a);
		
		for (int coA : coAuthorsIDs) {
			coAuthors.add(getAuthorByID(coA));
		}
		
		return coAuthors;
	}
	
	
	/** estrae i coautori di un autore dato insieme all'autore stesso
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
	
	/** estrae la lista dei paper comuni ad una lista di autori (corpus ristretto)
	 * 
	 * @param author
	 * @param authors
	 * @return lista di Paper: paper comuni ad una lista di autori
	 */
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
	
	/** conta il numero di occorrenze di una keyword nel corpus ristretto
	 * ovvero il corpus formato dai paper di un autore e dei suoi coautori
	 * 
	 * @param keyword
	 * @param author
	 * @param authors
	 * @return IDF calcolato in base al corpus ristretto
	 */
	public double getRestrictedIDF(String keyword, Author author, List<Author> authors) {
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
				if (k.getKey().equals(keyword)) {
					m += k.getValue();
				}
			}				
		}
		
		if (N > 0 && m > 0) {
			idf = Math.log((double)N/m);
		}
		
		return idf;
	}
	
	/** restiruisce la lista dei paper dei coautori di un autore dato
	 * 
	 * @param a
	 * @return lista di Paper: i paper dei coautori di un autore dato
	 * @throws Exception
	 */
	//FIXME: chiedere se è davvero convinta...
	public List<Paper> coauthor_papers(Author a) throws Exception {
		return getCoAuthorsPapers(a);
	}
	
	/** estrae i paper dei coautori di un autore dato
	 * 
	 * @param a
	 * @return lista di Paper: elenco dei paper dei coautori di un autore dato
	 * @throws Exception
	 */
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
	
	/** rinomina in base a specifiche della funzione getCoAuthorsAndSelfPapers che
	 * estrae i paper dei coautori di un autore dato insieme a quelli dell'autore stesso
	 * @param a
	 * @return lista di Paper: elenco dei paper di un autore dato insieme a quelli dell'autore stesso
	 * @throws Exception
	 */
	//FIXME: chiedere se è convinta...
	public List<Paper> coauthor_and_self(Author a) throws Exception {
		return getCoAuthorsAndSelfPapers(a);
	}
	
	/** estrae i paper dei coautori di un autore dato insieme a quelli dell'autore stesso
	 * @param a
	 * @return lista di Paper: elenco dei paper di un autore dato insieme a quelli dell'autore stesso
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
	
	/** restituisce il numero di articoli dei coautori in coauthor_papers(a_i) che non contengono la chiave k_j;
	 * 
	 * @param a_i
	 * @param k_j
	 * @return numero intero: gli articoli dei coautori in coauthor_papers(a_i) che non contengono la chiave k_j;
	 * @throws Exception
	 */
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
	
	/** restituisce il numero di articoli in coauthor_and_self(a_i) che non contengono la chiave k_j
	 * 
	 * @param a_i
	 * @param k_j
	 * @return numero intero: gli articoli in coauthor_and_self(a_i) che non contengono la chiave k_j
	 * @throws Exception
	 */
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
	
	/** restituisce il peso u_ij della chiave k_j per l'autore a_i
	 * 
	 * @param a_i
	 * @param k_j
	 * @return peso u_ij della chiave k_j per l'autore a_i
	 * @throws Exception
	 */
	//FIXME: sostituire con eccezione appropriata...
	public double u_ij(Author a_i, String k_j) throws Exception{
		double numLog = 0.0;
		double denLog = 0.0;
		double resLog = 0.0;
		double resAbs = 0.0;
		double u_ij = 0.0;
		double epsilon = 0.1; // costante che non fa andare a zero
		int r_ij = this.r_notKey(a_i,  k_j);
		int n_ij = this.n_notKey(a_i, k_j);
		int R_i = this.coauthor_papers(a_i).size();
		int N_i = this.coauthor_and_self(a_i).size();
		numLog = (double) (r_ij + epsilon)/ (R_i - r_ij + epsilon);
		denLog = (double) (n_ij - r_ij + epsilon) / (N_i - n_ij - R_i + r_ij + epsilon);
		resLog = Math.log(numLog / denLog);
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
