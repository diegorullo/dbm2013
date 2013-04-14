package lab1;

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

//	public Paper(int paperid) {
//		Connection conn = null;
//		Statement stmt = null;
//		String query = "SELECT authors.name,papers.* FROM authors,papers,writtenby WHERE papers.paperid = " + paperid + " AND writtenby.personid=authors.personid AND writtenby.paperid=papers.paperid;";
//		ResultSet res = null;
//		try {			
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dblp", "root", "root");			
//			stmt = (Statement) conn.createStatement();
//			res = stmt.executeQuery(query);
//			
//			this.paperid = paperid;
//			res.next();
//			title = res.getString("title");
//			year = res.getInt("year");
//			publisher = res.getString("publisher");
//			paperAbstract = res.getString("abstract");
//			
//			try {
//				keywords = Weights.removeStopWordsAndStem(paperAbstract);
//			} catch (IOException e) {
//				System.out.println("IO Exception - no keyword");
//				keywords = null;
//			}
//			
//			while(res.next()) {
//				authors.add(res.getString("name"));
//			}
//			//System.out.println(authors.toString());
//			
//		} catch (SQLException e) {
//			System.out.println("SQLException: Paper");
//		}
//
//	}
	
	public Map<String, Double> key_TF(ArrayList<String> keywords) throws IOException {
		
		int n = 0;
		int K = keywords.size();
		double tf;
		HashMap<String, Integer> keywordSet = new HashMap<String, Integer>();
		Map<String, Double> keywordVectorTF = new TreeMap<String, Double>();
		
		for(String k : keywords) {
			if (!keywordSet.containsKey(k)) {
				keywordSet.put(k, 1);
			}
			else {
				keywordSet.put(k, keywordSet.get(k) + 1);
			}
		} 		

		Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
			n = k.getValue();
			tf = (double)n/K;
			keywordVectorTF.put(k.getKey(), tf);
		}
		
		return keywordVectorTF;
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
