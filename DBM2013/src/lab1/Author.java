package lab1;

import java.util.ArrayList;

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


	/**
	 * @return the personid
	 */
	public int getPersonID() {
		return personID;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the papers
	 */
	public ArrayList<Paper> getPapers() {
		return papers;
	}


	/**
	 * @param personid the personid to set
	 */
	public void setPersonID(int personID) {
		this.personID = personID;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @param papers the papers to set
	 */
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
	
//	public Author(int authorid) {
//		Connection conn = null;
//		Statement stmt = null;
//		String query = "SELECT authors.name,writtenby.paperid FROM authors,papers,writtenby WHERE authors.personid = " + personid  + "AND writtenby.personid=authors.personid AND writtenby.paperid=papers.paperid;";
//		ResultSet res = null;
//		try {
//			conn = DriverManager.getConnection(
//					"jdbc:mysql://localhost:3306/dblp", "root", "root");
//			stmt = (Statement) conn.createStatement();
//			res = stmt.executeQuery(query);
//
//			this.personid = authorid;
//			res.next();
//			name = res.getString("name");
//			papers.add(new Paper(res.getInt("paperid")));			
//			while(res.next()) {
//				papers.add(new Paper(res.getInt("paperid")));
//			}
//			
//		} catch (SQLException e) {
//			System.out.println("SQLException: Author");
//		}
//
//	}

	
	
}
