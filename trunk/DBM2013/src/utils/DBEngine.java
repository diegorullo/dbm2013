package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import lab1.Author;
import lab1.Corpus;
import lab1.Paper;

import com.mysql.jdbc.Statement;

public class DBEngine {
	private static Connection conn;
	private static Statement stmt;
	private static final String dbAddress = "jdbc:mysql://localhost:3306/dblp";
	private static final String username = "root";
	private static final String password = "root";
	private ResultSet res = null;

	public void init() throws SQLException {
		if (conn != null)
			conn = DriverManager.getConnection(dbAddress, username, password);
	}
	
	public void shutdown() throws SQLException{
		if(conn!=null)
			conn.close();
	}
	
	public Paper newPaper(int paperID) throws SQLException, IOException {
		final String title;
		final int year;
		final String publisher;
		final String paperAbstract;
		final ArrayList<String> keywords;
		final ArrayList<String> authors = new ArrayList<String>();
		
		String query = "SELECT authors.name,papers.* FROM authors,papers,writtenby WHERE papers.paperid = "
				+ paperID
				+ " AND writtenby.personid=authors.personid AND writtenby.paperid=papers.paperid;";
		stmt = (Statement) conn.createStatement();
		stmt.executeQuery(query);
		
		res = stmt.executeQuery(query);
		
		res.next();
		title = res.getString("title");
		year = res.getInt("year");
		publisher = res.getString("publisher");
		paperAbstract = res.getString("abstract");
		
		keywords = Weights.removeStopWordsAndStem(paperAbstract);
		
		authors.add(res.getString("name"));
		
		while(res.next()) {
			authors.add(res.getString("name"));
		}
		
		Paper p = new Paper(paperID, title, year, publisher, paperAbstract, authors, keywords);
	
		return p;
	}
	
	public Author newAuthor(int personID) throws SQLException, IOException {
		final String name;
		final ArrayList<Paper> papers = new ArrayList<Paper>();
		
		String query = "SELECT authors.name,writtenby.paperid FROM authors,papers,writtenby WHERE authors.personid = "
				+ personID  +
				"AND writtenby.personid=authors.personid AND writtenby.paperid=papers.paperid;";
		stmt = (Statement) conn.createStatement();
		stmt.executeQuery(query);
		
		res = stmt.executeQuery(query);
		
		res.next();
		name = res.getString("name");
		papers.add(newPaper(res.getInt("paperid")));			
		while(res.next()) {
			papers.add(newPaper(res.getInt("paperid")));
		}
		
		Author a = new Author(personID, name, papers);
	
		return a;
	}
	
	public Corpus newCorpus() throws SQLException, IOException {
		final ArrayList<Author> authors = new ArrayList<Author>();
		final ArrayList<Paper> papers = new ArrayList<Paper>();
		final int cardinality;		
		
		String queryA = "SELECT personid FROM authors;";
		String queryP = "SELECT paperid FROM papers;";
		String queryC = "SELECT COUNT(*) FROM papers;";

			res = stmt.executeQuery(queryA);
			System.out.println("qui");

			while(res.next()) {
				System.out.println(res.getInt("personid"));
				authors.add(newAuthor(res.getInt("personid")));
			}
			res = stmt.executeQuery(queryP);
			
			while(res.next()) {
				papers.add(newPaper(res.getInt("paperid")));
			}
			
			res = stmt.executeQuery(queryC);			
			res.next();
			cardinality = res.getInt(1);
			
			Corpus c = new Corpus(authors, papers, cardinality);
			
			return c;
	}
	
//	public class Main{
//		DBEngine engine = new DBEngine();
//		
//		public void doSomething(){
//			try {
//				Paper p = this.engine.newPaper(null);
//			} catch (SQLException e) {
//				System.err.println("Dannazione!");
//			}
//		}
//		
//	}

}