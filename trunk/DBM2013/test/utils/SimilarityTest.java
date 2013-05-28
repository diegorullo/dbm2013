package utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import junit.framework.Assert;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.Test;

import dblp.Author;
import dblp.Corpus;
import dblp.Factory;

public class SimilarityTest {
	
	private final static boolean DEBUG = false;
	
	@Test
	public void testGetCosineSimilarity() throws Exception {
		if (DEBUG) {
			Factory f = new Factory();
			Corpus dblp = f.getCorpus();
			//"K.S. Candan"
			Author authorCandan1 = dblp.getAuthorByID(2540868);
			//Author authorCandan1 = dblp.getAuthorByName("K.S. Candan");
			
			//"K. Selcuk Candan"
			Author authorCandan2 = dblp.getAuthorByID(1636579);
			
//			Similarity.getCosineSimilarity(a_vector, b_vector)

//			String fileName = testAuthor.getAuthorID() + ".csv";
//			ArrayList<TreeMap<String, Double>> documentTermMatrix = testAuthor
//					.getDocumentTermMatrix(dblp);
//
//			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/"
//					+ fileName);
//			// IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/" +
//			// "X" + ".csv");
//
//			ArrayList<ArrayList<Double>> matrixFromFile = IO
//					.readDocumentTermMatrixFromFile("../data/" + fileName);
//			// ArrayList<ArrayList<Double>> matrixFromFile
//			// =IO.readDocumentTermMatrixFromFile("../data/" +"X" + ".csv");
//			Printer.printMatrix(matrixFromFile);
		}
	}
}
