package utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dblp.Author;
import dblp.Corpus;
import dblp.Factory;

public class SimilarityTest {
	
	private final static boolean DEBUG = true;
	
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
			
			double similarity12 = authorCandan1.getSimilarityOnKeywordVector(authorCandan2, dblp);
			double similarity21 = authorCandan2.getSimilarityOnKeywordVector(authorCandan1, dblp);
			
			double epsilon = 0.0;
			
			System.out.println("Candan1 vs Candan2: " + similarity12);
			System.out.println("Candan2 vs Candan1: " + similarity21);
			//assertEquals(similarity12, similarity21, epsilon);
		}
	}
}
