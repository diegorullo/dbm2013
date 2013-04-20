package dblp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Author {
	private int personID;
	private String name;
	private ArrayList<Paper> papers;
	
	
	public Author(int personID, String name, ArrayList<Paper> papers) {
		super();
		this.personID = personID;
		this.name = name;
		this.papers = papers;
	}
	
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
			wtfidfv = p.getWTFIDFVector(weight, c);
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


	public Map<String, Double> getWTFVector() throws IOException {
		Map<String, Double> wtfv;
		double weight = 0;
		double weightNormalizationFactor;
		Map<String, Double> WTFVector = new TreeMap<String, Double>();

		/* - vettore di tf per ogni paper dell'autore
		 * - età del paper
		 * - peso usando l'età
		 * - tfrkey[r] = sum(peso[i]*tfkey[i])/sum(peso[i]);
		 */
		
		for (Paper p : papers) {
			weight += p.getWeightBasedOnAge();			
		}
		weightNormalizationFactor = 1 / weight;
		
		for (Paper p : papers) {
			weight = p.getWeightBasedOnAge();
			wtfv = p.getWTFVector(weight);
			Iterator<Entry<String, Double>> it = wtfv.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Double> k = (Map.Entry<String, Double>) it.next();
				if (!WTFVector.containsKey(k.getKey())) {
					WTFVector.put(k.getKey(), k.getValue() * weightNormalizationFactor);
				}
				else {
					WTFVector.put(k.getKey(), WTFVector.get(k.getKey()) + k.getValue() * weightNormalizationFactor);
				}
			}			
		}
		return WTFVector;
	}

	
	//Estrae l'insieme delle keyword, con il rispettivo numero di occorrenze
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
	
	public double getCombinedTF(String keyword) {
		double combinedTF = 0;
				
		return combinedTF;
	}

	public int getPersonID() {
		return personID;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
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
		return "Author [personID=" + personID + ", name=" + name + ", papers="
				+ papers + "]";
	}	
}
