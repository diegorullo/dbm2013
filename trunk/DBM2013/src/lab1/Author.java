package lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class Author {
	private int personid;
	private String name;
	private ArrayList<Paper> papers;

	public Author(int authorid) {
		Connection conn = null;
		Statement stmt = null;
		String query = "SELECT authors.name,writtenby.paperid FROM authors,papers,writtenby WHERE authors.personid = " + personid  + "AND writtenby.personid=authors.personid AND writtenby.paperid=papers.paperid;";
		ResultSet res = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dblp", "root", "root");
			stmt = (Statement) conn.createStatement();
			res = stmt.executeQuery(query);

			this.personid = authorid;
			res.next();
			name = res.getString("name");
			papers.add(new Paper(res.getInt("paperid")));			
			while(res.next()) {
				papers.add(new Paper(res.getInt("paperid")));
			}
			
		} catch (SQLException e) {
			System.out.println("SQLException: Author");
		}

	}

	public int getPersonid() {
		return personid;
	}

	public String getName() {
		return name;
	}
	
	public ArrayList<Paper> getPapers() {
		return papers;
	}
}
