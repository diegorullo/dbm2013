package dblp;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.DBEngine;

public class AuthorTest {
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
	public void testAuthor() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWTFVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWTFIDFVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCombinedKeywordSet() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRestrictedTF() {
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
		String keyword = "algorithm";
		Double tf = 0.0;
		Double valAtteso = (double) 2/30;
		tf = a.getRestrictedTF(keyword);
		System.out.println("Keyword: " + keyword +", TF = "+ tf);
		assertEquals ("Keyword: " + keyword +", TF = "+ tf, valAtteso, tf, 1/1000000);
	}

	@Test
	public void testGetAuthorID() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPapers() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetAuthorID() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetPapers() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

}
