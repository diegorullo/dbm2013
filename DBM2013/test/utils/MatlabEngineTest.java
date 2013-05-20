package utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dblp.Author;
import dblp.Corpus;

public class MatlabEngineTest {
	
	private final static boolean DEBUG = false;
	
	static DBEngine db = new DBEngine();
	@BeforeClass
	public static void testSetup() throws SQLException {
		db.init();
	}

	@AfterClass
	public static void testCleanup() throws SQLException {
		db.shutdown();
	}

	@Test
	public void initTest() throws MatlabConnectionException, MatlabInvocationException {
		if(DEBUG) {
			MatlabEngine me = new MatlabEngine();
			me.init();
			me.shutdown();
		}
	}
	
	@Test
	public void evalTest() throws MatlabConnectionException, MatlabInvocationException {
		if(DEBUG) {
			MatlabEngine me = new MatlabEngine();
			me.init();
			
			me.eval("svd_IR");
			me.eval("pca_IR");
			me.shutdown();
		}
	}
	
	@Test
	public void IOTest() throws Exception {
		MatlabEngine me = new MatlabEngine();
			me.init();
			
			Corpus dblp = db.newCorpus();
			
			Author testAuthor = dblp.getAuthorByID(2390072);
			ArrayList<TreeMap<String, Double>> documentTermMatrix = dblp.getDocumentTermMatrix(testAuthor);
			

			me.shutdown();
	}
}
