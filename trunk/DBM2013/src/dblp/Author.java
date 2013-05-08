package dblp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class Author {
	//FIXME
	final static int titleWeight = 3;
	private int authorID;
	private String name;
	private ArrayList<Paper> papers;
	
	
	public Author(int personID, String name, ArrayList<Paper> papers) {
		super();
		this.authorID = personID;
		this.name = name;
		this.papers = papers;
	}
	
	/**
	 * Considera tutti gli articoli scritti da un certo autore per creare
	 * un ”combined keyword vector” dei tf per quell’autore.
	 * Nei ”combined keyword vector”, gli articoli piu’ recenti devono
	 * pesare di piu’ di quelli piu’ vecchi.
	 * 
	 * @return treemap dei tf di un autore, pesando gli articoli per eta'
	 * @throws IOException
	 */
	public Map<String, Double> getWeightedTFVector() throws IOException {
		Map<String, Double> wtfv;
		double weight = 0;
		double weightNormalizationFactor = 0;
		TreeMap<String, Double> weightedTFVector = new TreeMap<String, Double>();
		//FIXME controllare - aggiustare il metodo
		
		/* - vettore di tf per ogni paper dell'autore
		 * - età del paper
		 * - peso usando l'età
		 * - tfrkey[r] = sum(peso[i]*tfkey[i])/sum(peso[i]);
		 */
		
//		for (Paper p : papers) {
//			weight += p.getWeightBasedOnAge();			
//		}
//		weightNormalizationFactor = 1 / weight;
		
		for (Paper p : papers) {
			weight = p.getWeightBasedOnAge();
			wtfv = p.getWeightedTFVector(weight);
			Iterator<Entry<String, Double>> it = wtfv.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Double> k = (Map.Entry<String, Double>) it.next();
				if (!weightedTFVector.containsKey(k.getKey())) {
					weightedTFVector.put(k.getKey(), k.getValue());			
				}
				else {
					weightedTFVector.put(k.getKey(), weightedTFVector.get(k.getKey()) + k.getValue());
				}
			}			
		}
		
		
		Iterator<Entry<String, Double>> it = weightedTFVector.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Double> k = (Map.Entry<String, Double>) it.next();
			weightNormalizationFactor+=k.getValue();
		}
		
		Double testUno = 0.0;
		Iterator<Entry<String, Double>> itNorm = weightedTFVector.entrySet().iterator();
		while (itNorm.hasNext()) {
			Map.Entry<String, Double> k = (Map.Entry<String, Double>) itNorm.next();
			weightedTFVector.put(k.getKey(), k.getValue()/weightNormalizationFactor);
			testUno+=k.getValue();
		}
		
		return weightedTFVector;
	}
	
	/**
	 * Considera tutti gli articoli scritti da un certo autore per creare
	 * un ”combined keyword vector” dei tfidf per quell’autore.
	 * Nei ”combined keyword vector”, gli articoli piu’ recenti devono
	 * pesare di piu’ di quelli piu’ vecchi.
	 * 
	 * @return treemap dei tfidf di un autore, pesando gli articoli per eta'
	 * @throws Exception
	 */
	//FIXME: sistemare l'eccezione
	public Map<String, Double> getWTFIDFVector(Corpus c) throws Exception {
		Map<String, Double> wtfidfv;
		double weight = 0;
		double weightNormalizationFactor;
		Map<String, Double> WTFIDFVector = new TreeMap<String, Double>();

		/* - vettore di tfidf per ogni paper dell'autore
		 * - età del paper
		 * - peso usando l'età
		 * - tfidfrkey[r] = sum(peso[i]*tfidfkey[i])/sum(peso[i]);
		 */
		
		for (Paper p : papers) {
			weight += p.getWeightBasedOnAge();			
		}
		weightNormalizationFactor = 1 / weight;
		
		for (Paper p : papers) {
			weight = p.getWeightBasedOnAge();
			wtfidfv = p.getWeightedTFIDFVector(weight, c);
			Iterator<Entry<String, Double>> it = wtfidfv.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Double> k = (Map.Entry<String, Double>) it.next();
				if (!WTFIDFVector.containsKey(k.getKey())) {
					WTFIDFVector.put(k.getKey(), k.getValue() * weightNormalizationFactor);
				}
				else {
					WTFIDFVector.put(k.getKey(), WTFIDFVector.get(k.getKey()) + k.getValue() * weightNormalizationFactor);
				}
			}			
		}
		return WTFIDFVector;
	}
	
	/**
	 * Estrae l'insieme delle keyword presenti in tutti gli articoli dell'autore,
	 * con il rispettivo numero di occorrenze.
	 * 
	 * @return hashmap delle keyword in tutti gli articoli dell'autore, con occorrenze
	 */
	public HashMap<String, Integer> getCombinedKeywordSet() {
		HashMap<String, Integer> combinedKeywordSet = new HashMap<String, Integer>();
		HashMap<String, Integer> keywordSet = new HashMap<String, Integer>();

		for (Paper p : papers) {
			keywordSet = p.getKeywordSet();
			//System.out.println(keywordSet);
			Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
				if (!combinedKeywordSet.containsKey(k)) {
					combinedKeywordSet.put(k.getKey(), k.getValue());
				}
				else {
					combinedKeywordSet.put(k.getKey(), combinedKeywordSet.get(k) + k.getValue());
				}
			}			
		}
		return combinedKeywordSet;
	}
	
	/**
	 * Estrae i nomi dei coautori dell'autore corrente.
	 * 
	 * @return lista di stringhe: nomi dei coautori dell'autore corrente
	 */
	public List<String> getCoAuthorsNames() {
		List<String> coAuthorsNames = new ArrayList<String>();
		
		for (Paper p : this.getPapers()) {
			for (String coA : p.getAuthorsNames()) {
				if(!coAuthorsNames.contains(coA) && !this.getName().equals(coA)) {
					coAuthorsNames.add(coA);
				}
			}
		}		
		return coAuthorsNames;
	}
	
	/**
	 * Estrae gli id dei coautori dell'autore corrente.
	 * 
	 * @return lista di interi: id dei coautori dell'autore corrente
	 */
	public List<Integer> getCoAuthorsIDs() {
		List<Integer> coAuthorsIDs = new ArrayList<Integer>();
		
		for (Paper p : this.getPapers()) {
			for (int coA : p.getAuthors()) {
				if(!coAuthorsIDs.contains(coA) && this.getAuthorID()!=(coA)) {
					coAuthorsIDs.add(coA);
				}
			}
		}		
		return coAuthorsIDs;
	}
	
	/**
	 * Calcola il tf della keyword basandosi sull’insieme
	 * di tutti gli articoli scritti dall’autore dato.
	 * 
	 * @param keyword
	 * @return tf della keyword basandosi sugli articoli dell'autore
	 */	
	public double getRestrictedTF(String keyword) {
		double tf = 0.0;
		List<Paper> rc = this.getPapers();		
		// conta il numero di occorrenze della keyword nei papers di author	
		int n = 0;
		int K = 0;
		for(Paper p : rc) {
			HashMap<String, Integer> keywordSet = p.getKeywordSet();

			if (keywordSet.get(keyword) != null) {
				n += keywordSet.get(keyword);
			}
			if (keywordSet != null) {
				K += p.getKeywords().size() + p.getTitlesKeywords().size() * titleWeight;
			}				
		}
		tf =(double) n / K;
		return tf;
	}
	
	
	public int getAuthorID() {
		return authorID;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}

	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPapers(ArrayList<Paper> papers) {
		this.papers = papers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Author [personID=" + authorID + ", name=" + name + ", papers="
				+ papers + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + authorID;
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
		Author other = (Author) obj;
		if (authorID != other.authorID)
			return false;
		return true;
	}

}
