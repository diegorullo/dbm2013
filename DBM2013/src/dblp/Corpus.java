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
	
	
	public double getInverseDocumentFrequency(String s) throws Exception {
	
		ArrayList<Paper> papers = this.getPapers();
		double idf = 0;
		int m = 0;
		int N = this.getCardinality();
		
		if(papers.contains(this))
		{
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
			
			idf = Math.log((double)N/m);
			return idf;
		}
		else {
			throw(new Exception("Il paper non è tra quelli del corpus!"));
		}
	
	}
	
//	// calcolo di IDF per ogni keyword
//	public Map<String, Double> key_TFIDF(ArrayList<String> keywords, Map<String, Double> tfVector) {
//		Map<String, Double> keywordVectorTFIDF = new TreeMap<String, Double>();
//		
//		int N = this.getCardinality();	//Numero totale di documenti del corpus
//		int m = 0;	//Numero di documenti in cui la feature occorre
//		double idf = 0;
//		//double tfidf = 0; 
//		
//		for (String k : keywords) {
//			//contare il numero di paper nel db che contengono k
//			this.getPapers();
//			for(Paper p : this.getPapers()) {
//				if(p.getKeywords().contains(k)) {
//					m++;
//				}
//			}
//			idf = Math.log(m/N);
//			//TODO: per il momento calcolo solo l'idf 
//			keywordVectorTFIDF.put(k, idf);
//		}
//		
//		return keywordVectorTFIDF;
//	}
	
	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}

	public int getCardinality() {
		return cardinality;
	}
}
