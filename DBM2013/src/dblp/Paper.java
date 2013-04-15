package dblp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Paper {
	private int paperID;
	private String title;
	private int year;
	private String publisher;
	private String paperAbstract;
	private ArrayList<String> authors;
	private ArrayList<String> keywords;
	
	
	public Paper(int paperID, String title, int year, String publisher,
			String paperAbstract, ArrayList<String> authors,
			ArrayList<String> keywords) {
		super();
		this.paperID = paperID;
		this.title = title;
		this.year = year;
		this.publisher = publisher;
		this.paperAbstract = paperAbstract;
		this.authors = authors;
		this.keywords = keywords;
	}
	
	//Estrae l'insieme delle keyword, con il rispettivo numero di occorrenze
	public HashMap<String, Integer> getKeywordSet() {
		
		HashMap<String, Integer> keywordSet = new HashMap<String, Integer>();
		for(String k : keywords) {
			if (!keywordSet.containsKey(k)) {
				keywordSet.put(k, 1);
			}
			else {
				keywordSet.put(k, keywordSet.get(k) + 1);
			}
		}
		return keywordSet;
	}
	
	//Calcola il tf di un termine tra le keyword di un Paper
	public double getTF(String s) {
		
		HashMap<String, Integer> keywordSet = this.getKeywordSet();
		double tf = 0;
		if(keywordSet.containsKey(s)) {
			tf = (double) keywordSet.get(s) / keywordSet.size();
		}
		return tf; 	
	}
	
	//Calcola il tfidf di un termine su tutto il corpus
	public double getTFIDF(String s, Corpus c) throws Exception {
		
		double tf = 0;
		double idf = 0;
		double tfidf = 0;
		
		tf = this.getTF(s);
		idf = c.getIDF(s);
		tfidf = tf * idf;
		
		return tfidf; 
	}

	
	//Calcola il vettore di tf per ogni keyword
	public Map<String, Double> getTFVector() throws IOException {
		
		HashMap<String, Integer> keywordSet = this.getKeywordSet();
		Map<String, Double> keywordVectorTF = new TreeMap<String, Double>();
		double tf;
		
		Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
			tf = getTF(k.getKey());
			keywordVectorTF.put(k.getKey(), tf);
		}
		
		return keywordVectorTF;
	}
	
	public void printPaperTFVector() throws IOException {
		
		System.out.println("Modello TF per \"" + title + "\" (" + paperID + "):");
		Map<String, Double> keywordVector = this.getTFVector();
		for (Map.Entry<String, Double> entry : keywordVector.entrySet()) {
            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
        }
	}

	// calcolo di TFIDF per ogni keyword
	public Map<String, Double> getTFIDFVector(Corpus c) throws Exception {
		
		HashMap<String, Integer> keywordSet = this.getKeywordSet();
		Map<String, Double> keywordVectorTFIDF = new TreeMap<String, Double>();
		double tfidf;
		
		Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
			tfidf = getTFIDF(k.getKey(), c);
			keywordVectorTFIDF.put(k.getKey(), tfidf);
		}
		
		return keywordVectorTFIDF;
	}
	
//	// calcolo di TFIDF per ogni keyword
//	public Map<String, Double> key_TFIDF(ArrayList<String> keywords, Map<String, Double> tfVector, Corpus c) {
//		
//		Map<String, Double> keywordVectorTFIDF = new TreeMap<String, Double>();		
//		int N = c.getCardinality();	//Numero totale di documenti del corpus
//		int m = 0;	//Numero di documenti in cui la feature occorre
//		double idf = 0;
//		//double tfidf = 0; 
//		
//		for (String k : keywords) {
//			//contare il numero di paper nel db che contengono k
//			c.getPapers();
//			for(Paper p : c.getPapers()) {
//				if(p.getKeywords().contains(k)) {
//					m++;
//				}
//			}
//			idf = Math.log((double)m/N);
//			//TODO: per il momento calcolo solo l'idf 
//			keywordVectorTFIDF.put(k, idf);
//		}
//		
//		return keywordVectorTFIDF;
//	}
	
	

	public int getPaperID() {
		return paperID;
	}

	public String getTitle() {
		return title;
	}

	public int getYear() {
		return year;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getPaperAbstract() {
		return paperAbstract;
	}

	public ArrayList<String> getAuthors() {
		return authors;
	}

	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public void setPaperid(int paperID) {
		this.paperID = paperID;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setPaperAbstract(String paperAbstract) {
		this.paperAbstract = paperAbstract;
	}

	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}

	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Paper [paperID=" + paperID + ", title=" + title + ", year="
				+ year + ", publisher=" + publisher + ", paperAbstract="
				+ paperAbstract + ", authors=" + authors + ", keywords="
				+ keywords + "]";
	}

}
