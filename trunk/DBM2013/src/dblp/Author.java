package dblp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.Normalization;


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
//		double weightNormalizationFactor = 0;
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
			for(Map.Entry<String, Double> k : wtfv.entrySet()) {
				if (!weightedTFVector.containsKey(k.getKey())) {
					weightedTFVector.put(k.getKey(), k.getValue());			
				}
				else {
					weightedTFVector.put(k.getKey(), weightedTFVector.get(k.getKey()) + k.getValue());
				}
			}			
		}
		
		//FIXME
		/* escluso i for di normalizzazione e passato vettore al metodo normalizaTreeMap
		 * implementato metodo NormalizationTest.testNormalizedWeightedTFVectorDummy
		 * Esito negativo.
		 */
		
		Map<String, Double> normalizedWeightedTFVector = Normalization.normalizeTreeMap(weightedTFVector);		
		return normalizedWeightedTFVector;
//		
//		for(Map.Entry<String, Double> k : weightedTFVector.entrySet()) {
//			weightNormalizationFactor+=k.getValue();
//		}
//		
//		Double testUno = 0.0;
//		for(Map.Entry<String, Double> k : weightedTFVector.entrySet()) {
//			weightedTFVector.put(k.getKey(), k.getValue()/weightNormalizationFactor);
//			testUno+=k.getValue();
//		}
//		
//		return weightedTFVector;
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
	public Map<String, Double> getWeightedTFIDFVector(Corpus c) throws Exception {
		TreeMap<String, Double> wtfidfv;
		double weight = 0;
		double weightNormalizationFactor;
		TreeMap<String, Double> weightedTFIDFVector = new TreeMap<String, Double>();

		/* - vettore di tfidf per ogni paper dell'autore
		 * - età del paper
		 * - peso usando l'età
		 * - tfidfrkey[r] = sum(peso[i]*tfidfkey[i])/sum(peso[i]);
		 */
		
		for (Paper p : papers) {
			weight += p.getWeightBasedOnAge();			
		}
		weightNormalizationFactor = 1 / weight;
		//FIXME
		TreeMap<String, Double> newVector = new TreeMap<String, Double>();
		
		for (Paper p : papers) {
			weight = p.getWeightBasedOnAge();
			wtfidfv = (TreeMap<String, Double>) p.getWeightedTFIDFVector(weight, c);
			//FIXME
			//System.out.println("Il vettore wtfidfv del paper " + p.getPaperID() +  " è normalizzato? " + Normalization.isNormalized(wtfidfv, 0.0));
			for(Map.Entry<String, Double> k : wtfidfv.entrySet()) {
				if (!weightedTFIDFVector.containsKey(k.getKey())) {
					newVector.put(k.getKey(), k.getValue());
					weightedTFIDFVector.put(k.getKey(), k.getValue() * weightNormalizationFactor);
				}
				else {
					weightedTFIDFVector.put(k.getKey(), weightedTFIDFVector.get(k.getKey()) + k.getValue() * weightNormalizationFactor);
					newVector.put(k.getKey(), weightedTFIDFVector.get(k.getKey()) + k.getValue());
				}
			}		
			
			double epsilon = (double) 1/100000000;
			//FIXME
			System.out.println("Il vettore weightedTFIDFVector del paper " + p.getPaperID() + " è normalizzato? " + Normalization.isNormalized(weightedTFIDFVector, epsilon));
			
			//FIXME
			System.out.println("Il vettore newVector del paper " + p.getPaperID() + " è normalizzato? " + Normalization.isNormalized(newVector, epsilon));
			
			//FIXME
			TreeMap<String, Double> newVector2 = Normalization.normalizeTreeMap(newVector);
			
			//FIXME
			System.out.println("Il vettore newVector2 del paper " + p.getPaperID() + " è normalizzato? " + Normalization.isNormalized(newVector2, epsilon));
			
			//FIXME
			System.out.println("Vettore normalizzato a posteriori (newVector2): " + newVector2);
			
		}
		

		
		
		return weightedTFIDFVector;
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
			for(Map.Entry<String, Integer> k : keywordSet.entrySet()) {
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
