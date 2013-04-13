package lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class Corpus {

	private static Corpus instance = null; // riferimento all' istanza
	private ArrayList<Author> authors;
	private ArrayList<Paper> papers;
	private int cardinality;

	private Corpus() {// costruttore
		Connection conn = null;
		Statement stmt = null;
		String queryA = "SELECT personid FROM authors;";
		String queryP = "SELECT paperid FROM papers;";
		String queryC = "SELECT COUNT(*) FROM papers;";
		ResultSet res = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dblp", "root", "root");
			stmt = (Statement) conn.createStatement();
			res = stmt.executeQuery(queryA);

			while(res.next()) {
				authors.add(new Author(res.getInt("personid")));
			}
			res = stmt.executeQuery(queryP);
			
			while(res.next()) {
				papers.add(new Paper(res.getInt("paperid")));
			}
			
			res = stmt.executeQuery(queryC);			
			res.next();
			cardinality = res.getInt(1);
			
		} catch (SQLException e) {
			System.out.println("SQLException: Corpus");
		}
	}

	public static Corpus getInstance() {
		if (instance == null)
			instance = new Corpus();
		return instance;
	}
	
	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}

	public int getCardinality() {
		return cardinality;
	}

}
