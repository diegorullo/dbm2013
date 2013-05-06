package dblp;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	
	/**
	 * Restituisce la lista degli articoli data una lista di autori.
	 * Caso in cui gli autori non hanno paper in comune.
	 */
	@Test
	public void testGetRestrictedCorpusSenzaPaperInComune() {
		ArrayList<String> authorsNames = new ArrayList<String>();
		authorsNames.add("Stefania");
		ArrayList<Integer> authors = new ArrayList<Integer>();
		authors.add(2013);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(2014);
		
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
		keywords2.add("algorithm");
		keywords2.add("algorithm");
		keywords2.add("parser");
		keywords2.add("parser");
		ArrayList<String> titlesKeywords2 = new ArrayList<String>();
		titlesKeywords2.add("calcolare");
		titlesKeywords2.add("insieme");
		titlesKeywords2.add("degli");
		titlesKeywords2.add("articoli");
		Paper paper2 = new Paper(2, "Calcolare insieme degli articoli", 2013, "Gruppo DBM DLS", "algorithm algorithm parser parser", authorsNames, authors, keywords2, titlesKeywords2);
		
		// paper 3
		ArrayList<String> keywords3 = new ArrayList<String>();
		keywords3.add("program");
		keywords3.add("algorithm");
		keywords3.add("parser");
		ArrayList<String> titlesKeywords3 = new ArrayList<String>();
		titlesKeywords3.add("program");
		titlesKeywords3.add("agile");
		titlesKeywords3.add("method");
		Paper paper3 = new Paper(3, "programming with agile method", 2013, "Gruppo DBM DLS", "program algorithm parser", authorsNames2, authors2, keywords3, titlesKeywords3);
		
		// paper 4
		ArrayList<String> keywords4 = new ArrayList<String>();
		keywords4.add("method");
		keywords4.add("token");
		keywords4.add("parser");
		keywords4.add("parser");
		ArrayList<String> titlesKeywords4 = new ArrayList<String>();
		titlesKeywords4.add("insieme");
		titlesKeywords4.add("dei");
		titlesKeywords4.add("token");
		Paper paper4 = new Paper(4, "insieme dei token", 2013, "Gruppo DBM DLS", "method token parser parser", authorsNames2, authors2, keywords4, titlesKeywords4);
		
		// paper 5
		ArrayList<String> keywords5 = new ArrayList<String>();
		keywords5.add("token");
		keywords5.add("token");
		keywords5.add("mathematics");
		ArrayList<String> titlesKeywords5 = new ArrayList<String>();
		titlesKeywords5.add("Metodi");
		titlesKeywords5.add("Matematici");
		Paper paper5 = new Paper(5, "Metodi Matematici", 2013, "Gruppo DBM DLS", "token token mathematics", authorsNames2, authors2, keywords5, titlesKeywords5);
		
		ArrayList<Paper> paperList = new ArrayList<Paper>();
		paperList.add(paper);
		paperList.add(paper2);
		
		ArrayList<Paper> paperList2 = new ArrayList<Paper>();
		paperList2.add(paper3);
		paperList2.add(paper4);
		paperList2.add(paper5);
		
		Author a = new Author(2013, "Stefania", paperList);
		Author a2 = new Author(2014, "Luca", paperList2);
		ArrayList<Author> aaa = new ArrayList<Author>();
		aaa.add(a);
		aaa.add(a2);
		
		List<Paper> ppp = new ArrayList<Paper>();
		ppp = Corpus.getRestrictedCorpus(aaa);
//		for (Paper p : ppp) {
//			System.out.println(p);
//		}
		assertEquals("Gli autori hanno globalmente " + (paperList.size() + paperList2.size()) + " articoli, mentre l'unione degli articoli conta " + ppp.size() + " articoli.",  paperList.size() + paperList2.size(), ppp.size());
	}
	
	/**
	 * Restituisce la lista degli articoli data una lista di autori.
	 * Caso in cui gli autori hanno un paper in comune.
	 */
	@Test
	public void testGetRestrictedCorpusConUnPaperInComune() {
		ArrayList<String> authorsNames = new ArrayList<String>();
		authorsNames.add("Stefania");
		ArrayList<Integer> authors = new ArrayList<Integer>();
		authors.add(2013);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(2014);
		
		
		ArrayList<String> authorsNames3 = new ArrayList<String>();
		authorsNames3.add("Luca");
		ArrayList<Integer> authors3 = new ArrayList<Integer>();
		authors3.add(2013);
		authors3.add(2014);
		
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
		keywords2.add("algorithm");
		keywords2.add("algorithm");
		keywords2.add("parser");
		keywords2.add("parser");
		ArrayList<String> titlesKeywords2 = new ArrayList<String>();
		titlesKeywords2.add("calcolare");
		titlesKeywords2.add("insieme");
		titlesKeywords2.add("degli");
		titlesKeywords2.add("articoli");
		Paper paper2 = new Paper(2, "Calcolare insieme degli articoli", 2013, "Gruppo DBM DLS", "algorithm algorithm parser parser", authorsNames, authors, keywords2, titlesKeywords2);
		
		// paper 3
		ArrayList<String> keywords3 = new ArrayList<String>();
		keywords3.add("program");
		keywords3.add("algorithm");
		keywords3.add("parser");
		ArrayList<String> titlesKeywords3 = new ArrayList<String>();
		titlesKeywords3.add("program");
		titlesKeywords3.add("agile");
		titlesKeywords3.add("method");
		Paper paper3 = new Paper(3, "programming with agile method", 2013, "Gruppo DBM DLS", "program algorithm parser", authorsNames2, authors2, keywords3, titlesKeywords3);
		
		// paper 4
		ArrayList<String> keywords4 = new ArrayList<String>();
		keywords4.add("method");
		keywords4.add("token");
		keywords4.add("parser");
		keywords4.add("parser");
		ArrayList<String> titlesKeywords4 = new ArrayList<String>();
		titlesKeywords4.add("insieme");
		titlesKeywords4.add("dei");
		titlesKeywords4.add("token");
		Paper paper4 = new Paper(4, "insieme dei token", 2013, "Gruppo DBM DLS", "method token parser parser", authorsNames2, authors2, keywords4, titlesKeywords4);
		
		// paper 5 (in comune)
		ArrayList<String> keywords5 = new ArrayList<String>();
		keywords5.add("token");
		keywords5.add("token");
		keywords5.add("mathematics");
		ArrayList<String> titlesKeywords5 = new ArrayList<String>();
		titlesKeywords5.add("Metodi");
		titlesKeywords5.add("Matematici");
		Paper paper5 = new Paper(5, "Metodi Matematici", 2013, "Gruppo DBM DLS", "token token mathematics", authorsNames3, authors3, keywords5, titlesKeywords5);
		
		ArrayList<Paper> paperList = new ArrayList<Paper>();
		paperList.add(paper);
		paperList.add(paper2);
		
		ArrayList<Paper> paperList2 = new ArrayList<Paper>();
		paperList2.add(paper3);
		paperList2.add(paper4);
		paperList2.add(paper5);
		
		Author a = new Author(2013, "Stefania", paperList);
		Author a2 = new Author(2014, "Luca", paperList2);
		ArrayList<Author> aaa = new ArrayList<Author>();
		aaa.add(a);
		aaa.add(a2);
		
		List<Paper> ppp = new ArrayList<Paper>();
		ppp = Corpus.getRestrictedCorpus(aaa);
//		for (Paper p : ppp) {
//			System.out.println(p);
//		}
		assertEquals("Gli autori hanno globalmente " + (paperList.size() + paperList2.size()) + " articoli, mentre l'unione degli articoli conta " + ppp.size() + " articoli.",  paperList.size() + paperList2.size(), ppp.size());
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
//		idf = dblp.getRestrictedIDF(keyword, a, dblp.getCoAuthorsAndSelf(a));
//		System.out.println("Keyword: " + keyword +", IDF = "+ idf);
//		assertEquals ("Keyword: " + keyword +", IDF = "+ idf, valAtteso, idf, 1/1000000);
	}


	/**
	 * Recupera i paper dei coautori, senza il self.
	 * In questo caso, paper 3, 4, 5.
	 * @throws Exception 
	 */
	@Test
	public void testGetCoAuthorsPapersConPaperInComune() throws Exception {
		ArrayList<String> authorsNames = new ArrayList<String>();
		authorsNames.add("Stefania");
		ArrayList<Integer> authors = new ArrayList<Integer>();
		authors.add(2013);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(2014);
		
		
		ArrayList<String> authorsNames3 = new ArrayList<String>();
		authorsNames3.add("Luca");
		ArrayList<Integer> authors3 = new ArrayList<Integer>();
		authors3.add(2013);
		authors3.add(2014);
		
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
		keywords2.add("algorithm");
		keywords2.add("algorithm");
		keywords2.add("parser");
		keywords2.add("parser");
		ArrayList<String> titlesKeywords2 = new ArrayList<String>();
		titlesKeywords2.add("calcolare");
		titlesKeywords2.add("insieme");
		titlesKeywords2.add("degli");
		titlesKeywords2.add("articoli");
		Paper paper2 = new Paper(2, "Calcolare insieme degli articoli", 2013, "Gruppo DBM DLS", "algorithm algorithm parser parser", authorsNames, authors, keywords2, titlesKeywords2);
		
		// paper 3
		ArrayList<String> keywords3 = new ArrayList<String>();
		keywords3.add("program");
		keywords3.add("algorithm");
		keywords3.add("parser");
		ArrayList<String> titlesKeywords3 = new ArrayList<String>();
		titlesKeywords3.add("program");
		titlesKeywords3.add("agile");
		titlesKeywords3.add("method");
		Paper paper3 = new Paper(3, "programming with agile method", 2013, "Gruppo DBM DLS", "program algorithm parser", authorsNames2, authors2, keywords3, titlesKeywords3);
		
		// paper 4
		ArrayList<String> keywords4 = new ArrayList<String>();
		keywords4.add("method");
		keywords4.add("token");
		keywords4.add("parser");
		keywords4.add("parser");
		ArrayList<String> titlesKeywords4 = new ArrayList<String>();
		titlesKeywords4.add("insieme");
		titlesKeywords4.add("dei");
		titlesKeywords4.add("token");
		Paper paper4 = new Paper(4, "insieme dei token", 2013, "Gruppo DBM DLS", "method token parser parser", authorsNames2, authors2, keywords4, titlesKeywords4);
		
		// paper 5 (in comune)
		ArrayList<String> keywords5 = new ArrayList<String>();
		keywords5.add("token");
		keywords5.add("token");
		keywords5.add("mathematics");
		ArrayList<String> titlesKeywords5 = new ArrayList<String>();
		titlesKeywords5.add("Metodi");
		titlesKeywords5.add("Matematici");
		Paper paper5 = new Paper(5, "Metodi Matematici", 2013, "Gruppo DBM DLS", "token token mathematics", authorsNames3, authors3, keywords5, titlesKeywords5);
		
		ArrayList<Paper> paperList = new ArrayList<Paper>();
		paperList.add(paper);
		paperList.add(paper2);
		
		ArrayList<Paper> paperList2 = new ArrayList<Paper>();
		paperList2.add(paper3);
		paperList2.add(paper4);
		paperList2.add(paper5);
		
		Author a = new Author(2013, "Stefania", paperList);
		Author a2 = new Author(2014, "Luca", paperList2);
		ArrayList<Author> aaa = new ArrayList<Author>();
		aaa.add(a);
		aaa.add(a2);
		
		ArrayList<Paper> pppList = (ArrayList<Paper>)paperList;
		pppList.addAll(paperList2);
		
		Corpus dblp = new Corpus(aaa, pppList, pppList.size());
			
		
		List<Paper> pp1 = new ArrayList<Paper>();
		pp1 = dblp.getCoAuthorsPapers(a);
		
		List<Paper> pp2 = new ArrayList<Paper>();
		pp2 = dblp.getCoAuthorsPapers(a2);

		assertEquals("L'insieme degli articoli dei coautori di " + a.getName() + "",  3, pp1.size());
		assertEquals("L'insieme degli articoli dei coautori di " + a2.getName() + " è vuoto.", 3, pp2.size());
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
