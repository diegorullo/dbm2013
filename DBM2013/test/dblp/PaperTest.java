package dblp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.Test;

import exceptions.NoPaperWithSuchIDException;

public class PaperTest {

	@Test
	public void testGetKeywordSetOnePaper() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoPaperWithSuchIDException {
		Factory f = new Factory();
		Corpus dblp = f.getCorpus();
		int paperid = 943390;
		Paper paper = dblp.getPaperByID(paperid);
		ArrayList<String> ks = paper.getKeywordSet();
		//System.out.println(ks);
		org.junit.Assert.assertNotNull("keywordset paper "+ paperid + "creato correttamente", ks);		
	}
	
	/**
	 * Versione dummy test del metodo getKeywordSet
	 * verifica la mera creazione del TreeMap di keyword.
	 * In realtà, se il paper ha titolo ed abstract nullo, 
	 * il test fallisce: il TreeMap viene creato ma rimane nullo.
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws NoPaperWithSuchIDException 
	 * @throws Exception 
	 */
	@Test
	public void testGetKeywordSetWithOccurrencesOnePaper() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoPaperWithSuchIDException {
		Factory f = new Factory();
		Corpus dblp = f.getCorpus();
		int paperid = 943390;
		Paper paper = dblp.getPaperByID(paperid);
		TreeMap<String, Integer> ks = paper.getKeywordSetWithOccurrences();
		org.junit.Assert.assertNotNull("keywordset paper "+ paperid + "creato correttamente", ks);		
	}
	
	/**
	 * Testa il metodo getKeywordSetWithOccurrences istanziando un paper
	 * di keyword note.
	 * @throws Exception 
	 */
	@Test
	public void testGetKeywordSetWithOccurrences() {		
		ArrayList<String> authorsNames = new ArrayList<String>();
		authorsNames.add("Stefania");
		ArrayList<Integer> authors = new ArrayList<Integer>();
		authors.add(2013);
		ArrayList<String> keywords = new ArrayList<String>();
		keywords.add("media");
		keywords.add("media");
		keywords.add("keyword");
		keywords.add("keyword");
		keywords.add("keyword");
		ArrayList<String> titlesKeywords = new ArrayList<String>();
		titlesKeywords.add("testare");
		titlesKeywords.add("TF");
		titlesKeywords.add("uno");
		Paper paper = new Paper(1, "Testare i TF a uno", 2013, "Gruppo DBM DLS", "media media keyword keyword keyword", authorsNames, authors, keywords, titlesKeywords);
		TreeMap<String, Integer> ks = paper.getKeywordSetWithOccurrences(); 
		TreeMap<String, Integer> treemapAtteso = new TreeMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{		    
		    put("TF", 1*3);
		    put("keyword", 3);
		    put("media", 2);
		    put("uno", 1*3);
		    put("testare", 1*3);
		}};
		org.junit.Assert.assertEquals(treemapAtteso, ks);	
	}

	/**
	 * Parte dal paper fittizio e recupera il TF 
	 * di una keyword prefissata.
	 * Confronta il TF calcolato dal metodo con
	 * il TF atteso, calcolato a mano.
	 * @throws Exception 
	 */
	@Test
	public void testGetTF() {
		ArrayList<String> authorsNames = new ArrayList<String>();
		authorsNames.add("Stefania");
		ArrayList<Integer> authors = new ArrayList<Integer>();
		authors.add(2013);
		ArrayList<String> keywords = new ArrayList<String>();
		keywords.add("media");
		keywords.add("media");
		keywords.add("keyword");
		keywords.add("keyword");
		keywords.add("keyword");
		ArrayList<String> titlesKeywords = new ArrayList<String>();
		titlesKeywords.add("testare");
		titlesKeywords.add("TF");
		titlesKeywords.add("uno");
		Paper paper = new Paper(1, "Testare i TF a uno", 2013, "Gruppo DBM DLS", "media media keyword keyword keyword", authorsNames, authors, keywords, titlesKeywords);
		//TreeMap<String, Integer> ks = paper.getKeywordSet(); 	
		
		String testKeyword = "media";
		Double testTf = paper.getTF(testKeyword);
		// media occorre 2 volte su 14, 2/14 = 0.142857 periodico
		Double tfAtteso = 0.14285714285714285;
		double epsilon = (double)1/1000000000;
		assertEquals("Il tf per la kw " + testKeyword + " vale: " + testTf, tfAtteso, testTf, epsilon);	
	}

	@Test
	public void testGetTFIDF() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetTFVector() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetTFIDFVector() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetAge() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetWeightBasedOnAge() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetWTF() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetWTFVector() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetWTFIDFVector() {
//		fail("Not yet implemented");
	}

	@Test
	public void testContainsKeyword() {
//		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
//
//		Author a = db.newAuthor(2390072);
//		
//		Paper p = 
//		fail("Not yet implemented");
	}

}
