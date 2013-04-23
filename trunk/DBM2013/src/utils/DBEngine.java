package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import dblp.Author;
import dblp.Corpus;
import dblp.Paper;

public class DBEngine {
	private static Connection conn;
	private Statement stmt;
	private static final String dbAddress = "jdbc:mysql://localhost:3306/dblp";
	private static final String username = "root";
	private static final String password = "root";

	public void init() throws SQLException {
		if (conn == null)
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
		final ArrayList<String> authorsNames = new ArrayList<String>();
		final ArrayList<Integer> authors = new ArrayList<Integer>();
		String query = "SELECT authors.*,papers.* FROM authors,papers,writtenby WHERE papers.paperid = "
				+ paperID
				+ " AND writtenby.personid=authors.personid AND writtenby.paperid=papers.paperid;";
		stmt = (Statement) conn.createStatement();		
		ResultSet res = stmt.executeQuery(query);
		
		res.next();
		title = res.getString("title");
		year = res.getInt("year");
		publisher = res.getString("publisher");
		paperAbstract = res.getString("abstract");
		
		keywords = TextProcessor.removeStopWordsAndStem(paperAbstract);
		
		authorsNames.add(res.getString("name"));
		authors.add(res.getInt("personid"));
		
		while(res.next()) {
			authorsNames.add(res.getString("name"));
			authors.add(res.getInt("personid"));	
		}
		
		Paper p = new Paper(paperID, title, year, publisher, paperAbstract, authorsNames, authors, keywords);	
		return p;
	}
	
	public Author newAuthor(int personID) throws SQLException, IOException {
		final ArrayList<Paper> papers = new ArrayList<Paper>();
		final String name;
		
		String query = "SELECT authors.name,papers.paperid FROM authors left outer join writtenby on writtenby.personid=authors.personid left outer join papers on  writtenby.paperid=papers.paperid where authors.personid = " + personID;
		stmt = (Statement) conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);		
		ResultSet res = stmt.executeQuery(query);
		
		if(res.next()) {			
			name = res.getString("name");
			//Questo caso cattura l'eventualita' che esista un author senza papers
			//e quindi la query restituisce paperid = null
			if(res.getInt("paperid") != 0) {
				papers.add(newPaper(res.getInt("paperid")));
			}
			while(res.next()) {
				int id = res.getInt("paperid");
				Paper p = newPaper(id);
				papers.add(p);
			}
		}
		else throw new SQLException("An author with paperID " + personID + " is not in the DB.");

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

		stmt = (Statement) conn.createStatement();
		ResultSet resA = stmt.executeQuery(queryA);			

		while(resA.next()) {
			authors.add(newAuthor(resA.getInt("personid")));
		}		
		
		ResultSet resP = stmt.executeQuery(queryP);
		while(resP.next()) {
			papers.add(newPaper(resP.getInt("paperid")));
		}
		
		ResultSet resC = stmt.executeQuery(queryC);			
		resC.next();
		cardinality = resC.getInt(1);
		
		Corpus c = new Corpus(authors, papers, cardinality);		
		return c;
	}
}