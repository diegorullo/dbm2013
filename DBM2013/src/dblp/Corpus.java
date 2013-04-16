package dblp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
	
	
	public double getIDF(String s) throws Exception {
		double idf = 0;
		int m = 0;
		int N = this.getCardinality();

			//contare il numero di occorrenze della keyword s nel corpus
			for(Paper p : papers)
			{
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
