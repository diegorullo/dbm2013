package dblp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import utils.Normalization;

public class Paper {
	//FIXME
	final static int titleWeight = 3;
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
	
	/**
	 * Estrae le keyword contenute nel titolo e le inserisce in
	 * un'hashmap attribuendo a ciascuna di esse il peso.
	 * 
	 * @param weight: peso parametrizzato
	 * @return hasmap delle keyword del titolo pesate n.occ * weight
	 */
	public TreeMap<String, Integer> getTitleKeywordSetWithOccurrences(int weight) {
		TreeMap<String, Integer> titlesKeywordSetWithOccurrences = new TreeMap<String, Integer>();
		
		for(String k : titlesKeywords) {
			if (!titlesKeywordSetWithOccurrences.containsKey(k)) {
				titlesKeywordSetWithOccurrences.put(k, weight);
			}
			else {
				titlesKeywordSetWithOccurrences.put(k, titlesKeywordSetWithOccurrences.get(k) + weight);
			}
		}
		
		return titlesKeywordSetWithOccurrences;
	}
	
	/**
	 * Estrae l'insieme delle keyword dal testo dell'abstract del documento
	 * unitamente a quelle dal titolo, con il rispettivo numero di occorrenze. 
	 * 
	 * @return l'hashmap delle keyword e rispettivo numero di occorrenze
	 */
	public TreeMap<String, Integer> getKeywordSetWithOccurrences() {
		
		TreeMap<String, Integer> keywordSetWithOccurrences = new TreeMap<String, Integer>();
		//FIXME documentare correttamente la scelta
		TreeMap<String, Integer> titlesKeywordSet = this.getTitleKeywordSetWithOccurrences(titleWeight);
		
		// keywords provenienti dall'abstract
		for(String k : keywords) {
			if (!keywordSetWithOccurrences.containsKey(k)) {
				keywordSetWithOccurrences.put(k, 1);
			}
			else {
				keywordSetWithOccurrences.put(k, keywordSetWithOccurrences.get(k) + 1);
			}
		}
		
		// keywords provenienti dal titolo	(ognuna pesata 'weight' volte)	
		for (Map.Entry<String, Integer> k : titlesKeywordSet.entrySet()) {
			if (!keywordSetWithOccurrences.containsKey(k.getKey())) {
				keywordSetWithOccurrences.put(k.getKey(), k.getValue());
			}
			else {
				keywordSetWithOccurrences.put(k.getKey(), keywordSetWithOccurrences.get(k.getKey()) + k.getValue());
			}
		}
		
		return keywordSetWithOccurrences;
	}
	
	/**
	 * Estrae le keyword contenute nel titolo e le inserisce in
	 * un'arraylist, ordinate lessicograficamente.
	 * 
	 * @return arraylist delle keyword del titolo, ordinato
	 */
	public ArrayList<String> getTitleKeywordSet() {
		ArrayList<String> titlesKeywordSet = new  ArrayList<String>();
		
		for(String k : titlesKeywords) {
			if (!titlesKeywordSet.contains(k)) {
				titlesKeywordSet.add(k);
			}
		}
		
		Collections.sort(titlesKeywordSet);
		
		return titlesKeywordSet;
	}
	
	/**
	 * Estrae l'insieme delle keyword dal testo dell'abstract del documento
	 * unitamente a quelle dal titolo, ordinato lessicograficamente. 
	 * 
	 * @return l'arraylist delle keyword e rispettivo numero di occorrenze, ordinato
	 */
	public ArrayList<String> getKeywordSet() {
		
		ArrayList<String> keywordSet = new ArrayList<String>();
		//FIXME documentare correttamente la scelta
		ArrayList<String> titlesKeywordSet = this.getTitleKeywordSet();
		
		// keywords provenienti dall'abstract
		for(String k : keywords) {
			if (!keywordSet.contains(k)) {
				keywordSet.add(k);
			}
		}
		
		// keywords provenienti dal titolo	(ognuna pesata 'weight' volte)	
		for (String k : titlesKeywordSet) {
			if (!keywordSet.contains(k)) {
				keywordSet.add(k);
			}
		}
		
		Collections.sort(keywordSet);
		
		return keywordSet;
	}
	
	/** 
	 * Calcola il tf di un termine tra le keyword di un articolo
	 * tf = n. occorrenze / somma delle occorrenze di tutte le keyword.
	 * 
	 * @param keyword
	 * @return n. occorrenze keyword / (somma delle occorrenze di tutte le keyword 
	 * dell'abstract + somma occorrenze pesate delle keyword del titolo)
	 */	
	public double getTF(String keyword) {
		
		TreeMap<String, Integer> keywordSet = this.getKeywordSetWithOccurrences();
		double tf = 0;
		int n = 0;
		int K = 0;		
		if (keywordSet.get(keyword) != null) {
			n = keywordSet.get(keyword);
		}
		if (keywordSet != null) {
			K = keywords.size() + titlesKeywords.size() * titleWeight;
		}
		if(keywordSet.containsKey(keyword)) {
			tf = (double) n / K;
		}
		
		return tf; 	
	}
	
	/**
	 * Calcola il tfidf di una keyword su tutto il corpus.
	 * 
	 * @param keyword
	 * @param corpus
	 * @return tfidf d
	 * @throws Exception
	 */
	public double getTFIDF(String keyword, Corpus corpus) throws Exception {
		
		double tf = 0;
		double idf = 0;
		double tfidf = 0;
				
		tf = this.getTF(keyword);
		idf = corpus.getIDF(keyword);
		tfidf = tf * idf;
		
		return tfidf; 
	}
	
	/**
	 * Restituisce il keyword vector sotto forma di sequenza di coppie <keyword,weight>
	 * rispetto al modello di pesi tf.
	 * 
	 * @return keywordVector pesato in base al tf
	 */
	public TreeMap<String, Double> getTFVector() throws IOException {
		
		TreeMap<String, Integer> keywordSet = this.getKeywordSetWithOccurrences();
		TreeMap<String, Double> TFVector = new TreeMap<String, Double>();		
		double tf = 0.0;		
		
		for(Map.Entry<String, Integer> k : keywordSet.entrySet()) {
			tf = getTF(k.getKey());
			TFVector.put(k.getKey(), tf);
		}
		
		//Superfluo, poichè il vettore dei tf è normalizzato
		TreeMap<String, Double> normalizedTFVector = Normalization.normalizeTreeMap(TFVector);
		
		return normalizedTFVector;
	}

	/**
	 * Restituisce il keyword vector sotto forma di sequenza di coppie <keyword,weight>
	 * rispetto al modello di pesi tfidf.
	 * 
	 * @param corpus
	 * @return keywordVector pesato in base al tfidf
	 */
	public TreeMap<String, Double> getTFIDFVector(Corpus corpus) throws Exception {
		
		TreeMap<String, Integer> keywordSet = this.getKeywordSetWithOccurrences();
		TreeMap<String, Double> TFIDFVector = new TreeMap<String, Double>();
		double tfidf;
		String key;
		
		for(Map.Entry<String, Integer> k : keywordSet.entrySet()) {
			key = k.getKey();
			tfidf = this.getTFIDF(key, corpus);
			TFIDFVector.put(key, tfidf);
		}
		
		TreeMap<String, Double> normalizedTFIDFVector = Normalization.normalizeTreeMap(TFIDFVector);
		
		return normalizedTFIDFVector;
	}
	
	/**
	 * Calcola l'età dell'articolo corrente come differenza
	 * tra l'anno corrente e l'anno di pubblicazione. 
	 *
	 * @return età dell'articolo corrente
	 */
	public int getAge() {
		int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()));
		return currentYear - this.year; 
	}
	
	/**
	 * Calcola il peso dell'articolo basato sull`età.
	 * 
	 * @return peso dell'articolo
	 */
	public double getWeightBasedOnAge() {
		return (double) 1 / (1 + this.getAge());
	}
	
	/**
	 * Restituisce il tf della keyword, pesato secondo l'età dell'articolo.
	 * 
	 * @param keyword 
	 * @param weight
	 * @return tf pesato della keyword
	 */
	public double getWeightedTF(String keyword, double weight) {
		double tf = getTF(keyword);
		return  weight * tf;
	}
	
	/**
	 * Restituisce il vettore dei tf, pesati secondo l'età degli articoli.
	 *  
	 * @param weight
	 * @return vettore dei TF pesati
	 * @throws IOException
	 */
	public TreeMap<String, Double> getWeightedTFVector(double weight) throws IOException {
		TreeMap<String, Double> TFVector = this.getTFVector();
		TreeMap<String, Double> weightedTFVector = new TreeMap<String, Double>();
		String key;
		Double wtf;
		
		for(Map.Entry<String, Double> k : TFVector.entrySet()) {
			key = k.getKey();
			wtf = (Double) k.getValue() * weight;
			weightedTFVector.put(key,wtf);
		}
		
		TreeMap<String, Double> normalizedWeightedTFVector = Normalization.normalizeTreeMap(weightedTFVector);
		
		return normalizedWeightedTFVector;
	}
	
	/**
	 * Restituisce il vettore dei tfidf, pesati secondo l`età degli articoli.
	 * 
	 * @param weight    
	 * @param corpus         Corpus degli articoli
	 * @return vettore dei tfidf pesati
	 * @throws Exception
	 */
	public TreeMap<String, Double> getWeightedTFIDFVector(double weight, Corpus corpus) throws Exception {
		TreeMap<String, Double> TFIDFVector = this.getTFIDFVector(corpus);
		TreeMap<String, Double> weightedTFIDFVector = new TreeMap<String, Double>();
		String key;
		Double wtfidf;
		
		for(Map.Entry<String, Double> k : TFIDFVector.entrySet()) {
			key = k.getKey();
			wtfidf = (Double) k.getValue() * weight;
			weightedTFIDFVector.put(key,wtfidf);
		}

		TreeMap<String, Double> normalizedWeightedTFIDFVector = Normalization.normalizeTreeMap(weightedTFIDFVector);
		
		return normalizedWeightedTFIDFVector;
	}
	
	/** 
	 * Controlla se la keyword è contenuta nell'articolo.
	 * 
	 * @param keyword
	 * @return true se la keyword è presente, false altrimenti
	 */
	public boolean containsKeyword(String keyword){
		return this.keywords.contains(keyword);
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
				+ paperAbstract + ", authorsNames=" + authorsNames
				+ ", authors=" + authors + ", keywords=" + keywords
				+ ", titlesKeywords=" + titlesKeywords + "]";
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
