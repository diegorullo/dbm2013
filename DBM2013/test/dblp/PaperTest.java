package dblp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.DBEngine;

public class PaperTest {

	static DBEngine db = new DBEngine();
	@BeforeClass
	public static void testSetup() throws SQLException {
		db.init();
	}

	@AfterClass
	public static void testCleanup() throws SQLException {
		db.shutdown();
	}

	/**
	 * Versione dummy test del metodo getKeywordSet
	 * verifica la mera creazione dell'HashMap di keyword.
	 * In realtà, se il paper ha titolo ed abstract nullo, 
	 * il test fallisce: l'HashMap viene creato ma rimane nullo.
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test
	public void testGetKeywordSetDummy() throws SQLException, IOException {		
		int paperid = 943390;
		Paper paper = db.newPaper(paperid);
		Map<String, Integer> ks = paper.getKeywordSet();
		org.junit.Assert.assertNotNull("keywordset paper "+ paperid + "creato correttamente", ks);		
	}
	
	/**
	 * Testa il metodo getKeywordSet istanziando un paper
	 * di keyword note.
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test
	public void testGetKeywordSet() throws SQLException, IOException {		
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
		Map<String, Integer> ks = paper.getKeywordSet(); 
		Map<String, Integer> mapAtteso = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{		    
		    put("TF", 1*3);
		    put("keyword", 3);
		    put("media", 2);
		    put("uno", 1*3);
		    put("testare", 1*3);
		}};
		org.junit.Assert.assertEquals(mapAtteso, ks);	
	}

	/**
	 * Parte dal paper fittizio e recupera il TF 
	 * di una keyword prefissata.
	 * Confronta il TF calcolato dal metodo con
	 * il TF atteso, calcolato a mano.
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
		//Map<String, Integer> ks = paper.getKeywordSet(); 	
		
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
