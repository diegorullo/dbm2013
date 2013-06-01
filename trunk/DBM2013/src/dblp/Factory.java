package dblp;

import java.sql.SQLException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import utils.DBEngine;

public class Factory {
	private static DBEngine dbe;
	private static Corpus dblp;
	
	public Factory() throws SQLException, MatlabConnectionException, MatlabInvocationException {
		dbe = getDBEngine();
	}
		
	public Paper newPaper(int paperID) throws Exception {		
		Paper p = dbe.newPaper(paperID);
		return p;
	}
	
	public Author newAuthor(int personID) throws Exception {
		Author a = dbe.newAuthor(personID);
		return a;
	}
	
	/**
	 * Ritorna l'unica istanza di dblp
	 * @return Corpus dblp
	 * @throws Exception 
	 */
	public Corpus getCorpus() throws Exception {
		if(dblp == null) {
			dblp = dbe.newCorpus();
		}
		return dblp;
	}
	
	public DBEngine getDBEngine() throws SQLException {
		if(dbe == null) {
			dbe = DBEngine.getDBEngine();
		}
		return dbe;
	}
	
}
