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
	static MatlabEngine me = new MatlabEngine();
	@BeforeClass
	public static void testSetup() throws SQLException, MatlabConnectionException, MatlabInvocationException {
		db.init();
		me.init();
	}

	@AfterClass
	public static void testCleanup() throws SQLException, MatlabInvocationException {
		db.shutdown();
		me.shutdown();
	}

	
	@Test
	public void evalTestDummy() throws MatlabConnectionException, MatlabInvocationException {
		if(DEBUG) {			
			me.eval("svd_IR");
			me.eval("pca_IR");
		}
	}
	
	@Test
	public void evalTestSVD() throws Exception {			
		Corpus dblp = db.newCorpus();
		
		Author testAuthor = dblp.getAuthorByID(2390072);
		ArrayList<TreeMap<String, Double>> documentTermMatrix = dblp.getDocumentTermMatrix(testAuthor);
		IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/" + "X" + ".csv");
		//IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/" + testAuthor.getAuthorID() + ".csv");
		me.eval("svd_IR");
		ArrayList<ArrayList<Double>> matrix_v =IO.readDocumentTermMatrixFromFile("../data/" +"V" + ".csv");
		System.out.println("Matrice V:");
		Printer.printMatrix(matrix_v);
	}
}
