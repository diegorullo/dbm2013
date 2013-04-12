package lab1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.lucene.analysis.TokenStream;

import utils.Weights;

import com.mysql.jdbc.Statement;

public class Paper {
	private int paperid;
	private String title;
	private int year;
	private String publisher;
	private String paperAbstract;
	private ArrayList<String> keywords;

	public Paper(int paperid) {
		Connection conn = null;
		Statement stmt = null;
		String query = "SELECT * FROM papers WHERE paperid = " + paperid + ";";
		ResultSet res = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dblp", "root", "root");
			stmt = (Statement) conn.createStatement();
			res = stmt.executeQuery(query);

			this.paperid = paperid;
			res.next();
			title = res.getString("title");
			year = res.getInt("year");
			publisher = res.getString("publisher");
			paperAbstract = res.getString("abstract");
			try {
				keywords = Weights.removeStopWordsAndStem(paperAbstract);
			} catch (IOException e) {
				System.out.println("IO Exception - no keyword");
				keywords = null;
			}
		} catch (SQLException e) {
			System.out.println("SQLException");
		}

	}

	public int getPaperid() {
		return paperid;
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

	public ArrayList<String> getKeywords() {
		return keywords;
	}

}
