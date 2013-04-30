package dblp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	private ArrayList<String> authorsNames;
	private ArrayList<Integer> authors;
	private ArrayList<String> keywords;
	private ArrayList<String> titlesKeywords;
	
	
	public Paper(int paperID, String title, int year, String publisher,
			String paperAbstract, ArrayList<String> authorsNames, ArrayList<Integer> authors,
			ArrayList<String> keywords, ArrayList<String> titlesKeywords) {
		super();
		this.paperID = paperID;
		this.title = title;
		this.year = year;
		this.publisher = publisher;
		this.paperAbstract = paperAbstract;
		this.authorsNames = authorsNames;
		this.authors = authors;
		this.keywords = keywords;
		this.titlesKeywords = titlesKeywords;
	}
	
	public HashMap<String, Integer> getTitleKeywordSet(int weight) {
		HashMap<String, Integer> titlesKeywordSet = new HashMap<String, Integer>();
		
		for(String k : titlesKeywords) {
			if (!titlesKeywordSet.containsKey(k)) {
				titlesKeywordSet.put(k, weight);
			}
			else {
				titlesKeywordSet.put(k, titlesKeywordSet.get(k) + weight);
			}
		}
		
		return titlesKeywordSet;
	}
	
	//Estrae l'insieme delle keyword, con il rispettivo numero di occorrenze
	public HashMap<String, Integer> getKeywordSet() {
		
		HashMap<String, Integer> keywordSet = new HashMap<String, Integer>();
		int weight = 3; // in questo modo le keyword del titolo pesano 3 volte tanto
		HashMap<String, Integer> titlesKeywordSet = this.getTitleKeywordSet(weight);
		
		// keywords provenienti dall'abstract
		for(String k : keywords) {
			if (!keywordSet.containsKey(k)) {
				keywordSet.put(k, 1);
			}
			else {
				keywordSet.put(k, keywordSet.get(k) + 1);
			}
		}
		
//		// keywords provenienti dal titolo	(pesate 'weight' volte)	
//		Iterator<Entry<String, Integer>> it = titlesKeywordSet.entrySet().iterator();
//		while(it.hasNext()) {
//			Map.Entry<String, Integer> k = (Entry<String, Integer>)it.next();
//			if (!keywordSet.containsKey(k.getKey())) {
//				keywordSet.put(k.getKey(), k.getValue());
//			}
//			else {
//				keywordSet.put(k.getKey(), keywordSet.get(k.getKey()) + k.getValue());
//			}
//		}
		
		return keywordSet;
	}
	
	//Calcola il tf di un termine tra le keyword di un Paper
	public double getTF(String s) {
		
		HashMap<String, Integer> keywordSet = this.getKeywordSet();
		double tf = 0;
		int n = 0;
		int K = 0;
		if (keywordSet.get(s) != null) {
			n = keywordSet.get(s);
		}
		if (keywordSet != null) {
			K = keywords.size();
		}
		if(keywordSet.containsKey(s)) {
			tf = (double) n / K;
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

	
	/**
	  *  restituisce il keyword vector sotto forma di sequenza di coppie <keyword,weight>
	  *  rispetto al modello di pesi TF 
	  * @return keywordVector pesato in base al TF
	  */
	public Map<String, Double> getTFVector() throws IOException {
		
		HashMap<String, Integer> keywordSet = this.getKeywordSet();
		Map<String, Double> keywordVectorTF = new TreeMap<String, Double>();
		double tf = 0.0;
		
				Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
			tf = getTF(k.getKey());
			keywordVectorTF.put(k.getKey(), tf);
		}
		
		return keywordVectorTF;
	}

	/**
	  *  restituisce il keyword vector sotto forma di sequenza di coppie <keyword,weight>
	  *  rispetto al modello di pesi TFIDF 
	  * @return keywordVector pesato in base al TFIDF
	  */
	public Map<String, Double> getTFIDFVector(Corpus c) throws Exception {
		
		HashMap<String, Integer> keywordSet = this.getKeywordSet();
		Map<String, Double> keywordVectorTFIDF = new TreeMap<String, Double>();
		double tfidf;
		String key;
		
		Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
			key = k.getKey();
			tfidf = getTFIDF(key, c);
			keywordVectorTFIDF.put(key, tfidf);
		}
		return keywordVectorTFIDF;
	}
	
	/** restituisce l'età del paper corrente
	 *
	 * @return età del paper corrente
	 */
	public int getAge() {
		int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()));
		return currentYear - this.year; 
	}
	
	/** calcola il peso del paper basato sull`età
	 * 
	 * @return peso del paper
	 */
	public double getWeightBasedOnAge() {
		return (double) 1 / (1 + this.getAge());
	}
	
	/** restituisce il TF della keyword pesato secondo l`età del paper
	 * 
	 * @param keyword 
	 * @param weight
	 * @return tf pesato della keyword
	 */
	public double getWTF(String keyword, double weight) {
		double tf = getTF(keyword);
		return  weight * tf;
	}
	
	/** restituisce il vettore dei TF pesati secondo l`età dei papers
	 *  
	 * @param weight
	 * @return vettore dei TF pesati
	 * @throws IOException
	 */
	public Map<String, Double> getWTFVector(double weight) throws IOException {
		Map<String, Double> TFVector = this.getTFVector();
		Map<String, Double> WTFVector = new TreeMap<String, Double>();
		String key;
		Double wtf;
		
		Iterator<Entry<String, Double>> it = TFVector.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Double> k = (Map.Entry<String, Double>) it.next();
			key = k.getKey();
			wtf = (Double) k.getValue() * weight;
			WTFVector.put(key,wtf);
		}

		return WTFVector;
	}
	
	/** restituisce il vettore dei TFIDF pesati secondo l`età dei papers
	 * 
	 * @param weight    
	 * @param c         Corpus dei papers
	 * @return vettore dei TFIDF pesati
	 * @throws Exception
	 */
	public Map<String, Double> getWTFIDFVector(double weight, Corpus c) throws Exception {
		Map<String, Double> TFIDFVector = this.getTFIDFVector(c);
		Map<String, Double> WTFIDFVector = new TreeMap<String, Double>();
		String key;
		Double wtfidf;
		
		Iterator<Entry<String, Double>> it = TFIDFVector.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Double> k = (Map.Entry<String, Double>) it.next();
			key = k.getKey();
			wtfidf = (Double) k.getValue() * weight;
			WTFIDFVector.put(key,wtfidf);
		}

		return WTFIDFVector;
	}
	
	/** controlla se la keyword è contenuta nel paper
	 * 
	 * @param key
	 * @return true se la keyword è presente, false altrimenti
	 */
	public boolean containsKeyword(String key){
		return this.keywords.contains(key);
	}
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

	public ArrayList<String> getAuthorsNames() {
		return authorsNames;
	}

	public ArrayList<Integer> getAuthors() {
		return authors;
	}
	
	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public ArrayList<String> getTitlesKeywords() {
		return titlesKeywords;
	}

	public void setPaperID(int paperID) {
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

	public void setAuthorsNames(ArrayList<String> authorsNames) {
		this.authorsNames = authorsNames;
	}
	
	public void setAuthors(ArrayList<Integer> authors) {
		this.authors = authors;
	}

	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}
	
	public void setKeywordsTitle(ArrayList<String> titlesKeywords) {
		this.titlesKeywords = titlesKeywords;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + paperID;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paper other = (Paper) obj;
		if (paperID != other.paperID)
			return false;
		return true;
	}

}
