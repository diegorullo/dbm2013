package dblp;

import java.io.IOException;
import java.sql.SQLException;

import utils.DBEngine;

public class Factory {
	private static DBEngine db = DBEngine.getDBEngine();
	private static Corpus dblp;
		
	public Paper newPaper(int paperID) throws SQLException, IOException {		
		Paper p = db.newPaper(paperID);
		return p;
	}
	
	public Author newAuthor(int personID) throws SQLException, IOException {
		Author a = db.newAuthor(personID);
		return a;
	}
	
	/**
	 * Ritorna l'unica istanza di dblp
	 * @return Corpus dblp
	 * @throws SQLException
	 * @throws IOException
	 */
	public Corpus getCorpus() throws SQLException, IOException {
		if(dblp == null) {
			dblp = db.newCorpus();
		}
		return dblp;
	}

}
