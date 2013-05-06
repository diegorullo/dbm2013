package dblp;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.DBEngine;

public class CorpusTest {
	
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
	public void testCorpus() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIDF() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAuthorByName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAuthorByID() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoAuthors() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoAuthorsAndSelf() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRestrictedCorpus() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRestrictedIDF() throws Exception {
		//FIXME
		ArrayList<String> authorsNames = new ArrayList<String>();
		authorsNames.add("Stefania");
		ArrayList<Integer> authors = new ArrayList<Integer>();
		authors.add(2013);
		
		// paper 1
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
		
		// paper 2
		ArrayList<String> keywords2 = new ArrayList<String>();
		keywords.add("algorithm");
		keywords.add("algorithm");
		keywords.add("parser");
		keywords.add("parser");
		ArrayList<String> titlesKeywords2 = new ArrayList<String>();
		titlesKeywords.add("calcolare");
		titlesKeywords.add("insieme");
		titlesKeywords.add("degli");
		titlesKeywords.add("articoli");
		Paper paper2 = new Paper(2, "Calcolare insieme degli articoli", 2013, "Gruppo DBM DLS", "algorithm algorithm parser parser", authorsNames, authors, keywords2, titlesKeywords2);
		ArrayList<Paper> paperList = new ArrayList<Paper>();
		paperList.add(paper);
		paperList.add(paper2);
		Author a = new Author(2013, "Stefania", paperList);	
		Corpus dblp = db.newCorpus();
		String keyword = "algorithm";
		Double idf = 0.0;
		Double valAtteso = (double) 2/30;
		idf = dblp.getRestrictedIDF(keyword, a, dblp.getCoAuthorsAndSelf(a));
		System.out.println("Keyword: " + keyword +", IDF = "+ idf);
		assertEquals ("Keyword: " + keyword +", IDF = "+ idf, valAtteso, idf, 1/1000000);
	}


	@Test
	public void testGetCoAuthorsPapers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoAuthorsAndSelfPapers() {
		fail("Not yet implemented");
	}

	@Test
	public void testR_withoutKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testN_withoutKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testU_ij() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAuthors() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPapers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCardinality() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
