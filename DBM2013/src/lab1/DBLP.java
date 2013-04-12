package lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class DBLP {
	private ArrayList<Author> authors;
	private ArrayList<Paper> papers;
	private int cardinality;

	private static DBLP istance = null; // riferimento all' istanza

	private DBLP() {// costruttore
		Connection conn = null;
		Statement stmt = null;
		String queryA = "SELECT authorid FROM authors;";
		String queryP = "SELECT paperid FROM papers;";
		String queryC = "SELECT COUNT(*) FROM papers;";
		ResultSet res = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dblp", "root", "root");
			stmt = (Statement) conn.createStatement();
			res = stmt.executeQuery(queryA);
			
			while(res.next()) {
				authors.add(new Author(res.getInt("authorid")));
			}
			res = stmt.executeQuery(queryP);
			
			while(res.next()) {
				papers.add(new Paper(res.getInt("paperid")));
			}
			
			res = stmt.executeQuery(queryC);			
			res.next();
			cardinality = res.getInt(1);
			
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
	}

	public static DBLP getIstance() {
		if (istance == null)
			istance = new DBLP();
		return istance;
	}

}
