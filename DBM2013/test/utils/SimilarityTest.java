package utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dblp.Author;
import dblp.Corpus;
import dblp.Factory;

public class SimilarityTest {
	
	private final static boolean DEBUG = true;
	
	@Test
	public void testGetCosineSimilarityWithSelf() throws Exception {
		if (DEBUG) {
			Factory f = new Factory();
			Corpus dblp = f.getCorpus();
			
			//Autore "K. Selcuk Candan" (esiste anche un "K.S. Candan", che pero' non ha papers)
			Author authorCandan = dblp.getAuthorByID(1636579);
			
			double similarity = authorCandan.getSimilarityOnKeywordVector(authorCandan, dblp);
			
			double epsilon = 1/1000000;			
			assertEquals(similarity, 1.0, epsilon);
		}
	}
	
	@Test
	public void testGetCosineSimilarityReflexiveCandanSapino() throws Exception {
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
			
			double epsilon = 1/1000000;			
			assertEquals(similarityCS, similaritySC, epsilon);
		}
	}
}
