package lab1;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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
	
	// calcolo di TFIDF per ogni keyword
	public Map<String, Double> key_TFIDF(ArrayList<String> keywords, Map<String, Double> tfVector) {
		Map<String, Double> keywordVectorTFIDF = new TreeMap<String, Double>();
		
		int N = this.getCardinality();	//Numero totale di documenti del corpus
		int m = 0;	//Numero di documenti in cui la feature occorre
		double idf = 0;
		//double tfidf = 0; 
		
		for (String k : keywords) {
			//contare il numero di paper nel db che contengono k
			this.getPapers();
			for(Paper p : this.getPapers()) {
				if(p.getKeywords().contains(k)) {
					m++;
				}
			}
			idf = Math.log(m/N);
			//TODO: per il momento calcolo solo l'idf 
			keywordVectorTFIDF.put(k, idf);
		}
		
		return keywordVectorTFIDF;
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

}
