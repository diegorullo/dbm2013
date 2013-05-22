package dblp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NameNotFoundException;

import utils.TextProcessor;

import com.mysql.jdbc.Statement;
//FIXME
//import utils.DBEngine;

public class Corpus {

	private static Corpus corpusInstance = null; // riferimento all' istanza
	private ArrayList<Author> authors;
	private ArrayList<Paper> papers;
	private int cardinality;

	private static Connection conn;
	private static Statement stmt;
	private static final String dbAddress = "jdbc:mysql://localhost:3306/dblp";
	private static final String username = "root";
	private static final String password = "root";

	/**
	 * inizializza la connessione al db
	 */
	public void init() throws SQLException {
		if (conn == null)
			conn = DriverManager.getConnection(dbAddress, username, password);
	}
	
	/**
	 * chiude la connessione al db
	 */
	public void shutdown() throws SQLException{
		if(conn!=null)
			conn.close();
	}
	
//	public static Statement getStatement() throws SQLException{
//		return (Statement) conn.createStatement();	
//	}
	
	/**
	 * istanzia il corpus popolando:
	 * - l'elenco di tutti gli autori del corpus stesso
	 * - l'elenco di tutti i papers 
	 * - la cardinalità, numero di papers del corpus.
	 * 
	 * @return corpus	corpus con autori, papers e cardinalità
	 */
	private Corpus(ArrayList<Author> authors, ArrayList<Paper> papers, int cardinality) {
		super();
		this.authors = authors;
		this.papers = papers;
		this.cardinality = cardinality;
	}
	
	public static synchronized Corpus getCorpus() throws SQLException, IOException{
		if (corpusInstance == null) {
			final ArrayList<Author> authors = new ArrayList<Author>();
			final ArrayList<Paper> papers = new ArrayList<Paper>();
			final int cardinality;	
			Statement stmt;
			
					
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
			
			corpusInstance = new Corpus(authors, papers, cardinality);
			
		}
		return corpusInstance;
	}
	
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}
	
	
	/**
	 * istanzia un paper a partire dal relativo id
	 * @param paperID   id del paper
	 * @return paper	paper
	 */
	public static Paper newPaper(int paperID) throws SQLException, IOException {
		final String title;
		final int year;
		final String publisher;
		final String paperAbstract;
		final ArrayList<String> keywords;
		final ArrayList<String> keywordsTitle;
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
		keywordsTitle = TextProcessor.removeStopWordsAndStem(title);
		
		authorsNames.add(res.getString("name"));
		authors.add(res.getInt("personid"));
		
		while(res.next()) {
			authorsNames.add(res.getString("name"));
			authors.add(res.getInt("personid"));	
		}
		
		Paper p = new Paper(paperID, title, year, publisher, paperAbstract, authorsNames, authors, keywords, keywordsTitle);	
		return p;
	}
	
	/**
	 * istanzia un autore a partire dal relativo id
	 * @param personID   id dell'autore
	 * @return author	 autore
	 */
	public static Author newAuthor(int personID) throws SQLException, IOException {
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
	

	


	
	/**
	 * Restituisce l'oggetto Author associato al nome dato.
	 * 
	 * @param name
	 * @return oggetto Author associato al nome dato
	 * @throws NameNotFoundException
	 */
	public Author getAuthorByName(String name) throws NameNotFoundException {
		for (Author a : authors) {
			if (name.equals(a.getName())) {
				return a;
			}
		}
		throw new NameNotFoundException("There is no author named '" + name + "' in the Corpus.");
	}
	
	/**
	 * Restituisce l'oggetto Author associato all'id dato.
	 * 
	 * @param id
	 * @return oggetto Author associato all`id dato
	 * @throws Exception
	 */
	public Author getAuthorByID(int id) throws Exception {
		for (Author a : authors) {
			if (id==a.getAuthorID()) {
				return a;
			}
		}
		//FIXME: sistemare con eccezione appropriata
		throw new Exception();
	}
	
	/**
	 * Restituisce l'idf di una keyword.
	 * 
	 * @param keyword
	 * @return idf di una keyword
	 * @throws Exception
	 */
	//FIXME Sostituire con eccezione appropriata
	public double getIDF(String keyword) throws Exception {
		double idf = 0;
		int m = 0;
		
		//FIXME 
		Corpus dblp = Corpus.getCorpus();
		int N = dblp.getCardinality();

		// conta il numero di occorrenze della keyword s nel corpus
		for(Paper p : papers) {
			HashMap<String, Integer> keywordSet = p.getKeywordSetWithOccurrences();
			for(Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				if (k.getKey().equals(keyword)) {
					m++;
				}
			}				
		}
		if (N > 0 && m > 0) {
			idf = Math.log((double)N/m);
		}
		return idf;
	}
		

	
	/**
	 * Estrae la lista dei paper comuni ad una lista di autori (corpus ristretto).
	 * 
	 * @param authors: i coautori dell'autore in esame
	 * @return lista di Paper: paper comuni ad una lista di autori
	 */
	
	//FIXME
	//vedere dove va piazzato (forse author e statico)
//	public List<Paper> getRestrictedCorpus(List<Author> authors) {
//		List<Paper> restrictedCorpus = new ArrayList<Paper>();
//		
//		for (Author a : authors) {
//			for (Paper p : a.getPapers()) {
//				if (!restrictedCorpus.contains(p)) {
//					restrictedCorpus.add(p);
//				}
//			}
//		}
//		
//		return restrictedCorpus;
//	} 
	
	
	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}

	public int getCardinality() {
		return cardinality;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Corpus [authors=" + authors + ", papers=" + papers
				+ ", cardinality=" + cardinality + "]";
	}
}
