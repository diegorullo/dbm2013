package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.TreeMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.Test;

import dblp.Author;
import dblp.Corpus;
import dblp.Factory;
import exceptions.NoAuthorsWithSuchIDException;

public class SimilarityTest {
	
	private final static boolean DEBUG = true;
	
	private final static double epsilon = 1.0/10000000;
	
	@Test
	public void testGetCosineSimilarityWithSelf() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException {
		if (DEBUG) {
			Factory f = new Factory();
			Corpus dblp = f.getCorpus();
			
			//Autore "K. Selcuk Candan" (esiste anche un "K.S. Candan", che pero' non ha papers)
			Author authorCandan = dblp.getAuthorByID(1636579);
			
			double similarity = authorCandan.getSimilarityOnKeywordVector(authorCandan, dblp);
			
			assertEquals(similarity, 1.0, epsilon);
		}
	}
	
	@Test
	public void testGetCosineSimilarityReflexiveCandanSapino() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException {
		if (DEBUG) {
			Factory f = new Factory();
			Corpus dblp = f.getCorpus();
			//Autore "Maria Luisa Sapino"
			Author authorSapino = dblp.getAuthorByID(1677020);
//			System.out.println("KS di Sapino (" + authorSapino.getKeywordSet().size() + "): " + authorSapino.getKeywordSet());
			
			//Autore "K. Selcuk Candan" (esiste anche un "K.S. Candan" che pero' non ha papers)
			Author authorCandan = dblp.getAuthorByID(1636579);
//			System.out.println("KS di Candan (" + authorCandan.getKeywordSet().size() + "): " + authorCandan.getKeywordSet());
			
			double similarityCS = authorCandan.getSimilarityOnKeywordVector(authorSapino, dblp);
			double similaritySC = authorSapino.getSimilarityOnKeywordVector(authorCandan, dblp);
			
//			System.out.println("Similarità Candan-Sapino: " + similarityCS);
//			System.out.println("Similarità Sapino-Candan: " + similaritySC);
			
		
			assertEquals(similarityCS, similaritySC, epsilon);
		}
	}
	
	@Test
	public void testGetCosineSimilarity() {
		TreeMap<String, Double> v1 = new TreeMap<String, Double>();
		v1.put("Acqua", 1.0);
		
		TreeMap<String, Double> v2 = new TreeMap<String, Double>();
		v2.put("Cane", 0.5);
		v2.put("Gatto", 0.3);
		v2.put("Banana", 0.2);
	
		assertEquals(0.0, Similarity.getCosineSimilarity(v1, v2), epsilon);
	}
	
	@Test
	public void testMoveRangeFromMinus1_1To0_1() {
		double a = -1.0, b = -0.25, c = 0.0, d = 0.25, e = 1.0;
		
		assertTrue(0.0 == Similarity.moveRangeFromMinus1_1To0_1(a));
		assertTrue(0.375 == Similarity.moveRangeFromMinus1_1To0_1(b));
		assertTrue(0.5 == Similarity.moveRangeFromMinus1_1To0_1(c));
		assertTrue(0.625 == Similarity.moveRangeFromMinus1_1To0_1(d));
		assertTrue(1.0 == Similarity.moveRangeFromMinus1_1To0_1(e));
	}
	
}
