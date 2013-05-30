package dblp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import exceptions.AuthorWithoutPapersException;

public class AuthorTest {
	
	@Test
	public void testAuthor() {
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
	public void testGetCombinedKeywordSet() {
//		fail("Not yet implemented");
	}

	/**
	 * 1 autore, 2 paper, la keyword "algorithm" compare in
	 * entrambi i paper;
	 * (vedi commento interno per il valore atteso)
	 * @throws Exception 
	 */
	@Test
	public void testGetRestrictedTF() throws Exception {
		
		ArrayList<String> authorsNames1 = new ArrayList<String>();
		authorsNames1.add("Stefania");
		ArrayList<Integer> authors1 = new ArrayList<Integer>();
		authors1.add(2013);
		
		// paper 1
		ArrayList<String> keywords1 = new ArrayList<String>();
		keywords1.add("media");
		keywords1.add("media");
		keywords1.add("keyword");
		keywords1.add("keyword");
		keywords1.add("keyword");
		ArrayList<String> titlesKeywords1 = new ArrayList<String>();
		titlesKeywords1.add("testare");
		titlesKeywords1.add("TF");
		titlesKeywords1.add("uno");
		Paper paper1 = new Paper(1, "Testare i TF a uno", 2013, "Gruppo DBM DLS", "media media keyword keyword keyword", authorsNames1, authors1, keywords1, titlesKeywords1);
		
		// paper 2
		ArrayList<String> keywords2 = new ArrayList<String>();
		keywords2.add("algorithm");
		keywords2.add("algorithm");
		keywords2.add("parser");
		keywords2.add("parser");
		ArrayList<String> titlesKeywords2 = new ArrayList<String>();
		titlesKeywords2.add("calcolare");
		titlesKeywords2.add("insieme");
		titlesKeywords2.add("degli");
		titlesKeywords2.add("articoli");
		Paper paper2 = new Paper(2, "Calcolare insieme degli articoli", 2013, "Gruppo DBM DLS", "algorithm algorithm parser parser", authorsNames1, authors1, keywords2, titlesKeywords2);
		
		ArrayList<Paper> paperList1 = new ArrayList<Paper>();
		paperList1.add(paper1);
		paperList1.add(paper2);
		Author a = new Author(2013, "Stefania", paperList1);	
		String keyword = "algorithm";
		Double tf = 0.0;
		
		/*
         *  "algorithm" compare 2 volte /
		 *  (il paper 1 ha 5 kw dall'abstract + 3 (peso) * 3 kw dal titolo + 
		 *   il paper 2 ha 4 kw dall'abstract + 3 (peso) * 4 kw dal titolo) =
		 *  -------------------------------------------------------------
		 *  valore atteso 2 / (14 + 16)
		 */
		Double valAtteso = (double) 2/30;
		tf = a.getRestrictedTF(keyword);
		double epsilon = (double)1/1000000000;
		assertEquals ("Keyword: " + keyword +", TF = "+ tf, valAtteso, tf, epsilon);
	}

	@Test
	public void testGetAuthorID() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetPapers() {
//		fail("Not yet implemented");
	}

	@Test
	public void testSetAuthorID() {
//		fail("Not yet implemented");
	}

	@Test
	public void testSetName() {
//		fail("Not yet implemented");
	}

	@Test
	public void testSetPapers() {
//		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
//		fail("Not yet implemented");
	}

}
