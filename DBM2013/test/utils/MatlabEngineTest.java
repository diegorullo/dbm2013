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
	
	private final static boolean DEBUG = true;
	
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
			me.eval("svd_IR","Stefania.csv");
			me.eval("pca_IR","Stefania.csv");
		}
	}
	
	@Test
	public void evalTestSVD() throws Exception {
		if(DEBUG) {
			Corpus dblp = db.newCorpus();
			Author testAuthor = dblp.getAuthorByID(2390072);
			
			String fileName = testAuthor.getAuthorID() + ".csv";
			ArrayList<TreeMap<String, Double>> documentTermMatrix = dblp.getDocumentTermMatrix(testAuthor);
			
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/" + fileName);
			System.out.println("Scritto su file: " + fileName);
			
			me.eval("svd_IR",fileName);
			ArrayList<ArrayList<Double>> matrix_v = IO.readDocumentTermMatrixFromFile("../data/" + testAuthor.getAuthorID() + ".csv");

			System.out.println("Letta matrice V da file:");
			Printer.printMatrix(matrix_v);
		}
	}
}
