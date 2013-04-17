package dblp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
	
	//Estrae l'insieme delle keyword, con il rispettivo numero di occorrenze
	public HashMap<String, Integer> getCombinedKeywordSet() {
		HashMap<String, Integer> combinedKeywordSet = new HashMap<String, Integer>();
		HashMap<String, Integer> keywordSet = new HashMap<String, Integer>();

		for (Paper p : papers) {
			keywordSet = p.getKeywordSet();
			System.out.println(keywordSet);
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
