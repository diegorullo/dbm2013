package dblp;

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
