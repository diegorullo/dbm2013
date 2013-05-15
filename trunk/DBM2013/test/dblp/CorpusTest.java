package dblp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.DBEngine;
import utils.Normalization;

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
//		fail("Not yet implemented");
	}

	@Test
	public void testGetIDF() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetAuthorByName() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetAuthorByID() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetCoAuthors() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetCoAuthorsAndSelf() {
//		fail("Not yet implemented");
	}

	/**
	 * Restituisce la lista degli articoli data una lista di autori.
	 * Caso in cui gli autori non hanno paper in comune.
	 */
	@Test
	public void testGetRestrictedCorpusSenzaPaperInComune() {
		
		// -- PAPER --
		
		ArrayList<String> authorsNames1 = new ArrayList<String>();
		authorsNames1.add("Stefania");
		ArrayList<Integer> authors1 = new ArrayList<Integer>();
		authors1.add(1001);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(1002);
						
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
				
		
		// -- AUTORI --
		
		ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
		paperListStefania.add(paper1);
		paperListStefania.add(paper2);
		
		ArrayList<Paper> paperListLuca = new ArrayList<Paper>();
		paperListLuca.add(paper3);
		paperListLuca.add(paper4);
				
		Author authorStefania = new Author(1001, "Stefania", paperListStefania);
		Author authorLuca = new Author(1002, "Luca", paperListLuca);
		
		
		// -- CORPUS
		
		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
		listaAutoriNelCorpus.add(authorStefania);
		listaAutoriNelCorpus.add(authorLuca);
		
		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
		listaPaperNelCorpus.add(paper1);
		listaPaperNelCorpus.add(paper2);
		listaPaperNelCorpus.add(paper3);
		listaPaperNelCorpus.add(paper4);
		
		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus, listaPaperNelCorpus, listaPaperNelCorpus.size());
				
		ArrayList<Paper> restrictedCorpus = (ArrayList<Paper>)dummyCorpus.getRestrictedCorpus(listaAutoriNelCorpus);
				
		assertEquals("Gli autori hanno globalmente " + (authorStefania.getPapers().size() + authorLuca.getPapers().size()) + " articoli, mentre l'unione degli articoli conta " + restrictedCorpus.size() + " articoli.",  authorStefania.getPapers().size() + authorLuca.getPapers().size(), restrictedCorpus.size());
	}
	
	/**
	 * Restituisce la lista degli articoli data una lista di autori.
	 * Caso in cui gli autori hanno un paper in comune.
	 */
	@Test
	public void testGetRestrictedCorpusConUnPaperInComune() {
		// -- PAPER --
		
		ArrayList<String> authorsNames1 = new ArrayList<String>();
		authorsNames1.add("Stefania");
		ArrayList<Integer> authors1 = new ArrayList<Integer>();
		authors1.add(1001);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(1002);
				
		ArrayList<String> authorsNames3 = new ArrayList<String>();
		authorsNames3.add("Stefania");
		authorsNames3.add("Luca");
		ArrayList<Integer> authors3 = new ArrayList<Integer>();
		authors3.add(1001);
		authors3.add(1002);
		
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
		titlesKeywords5.add("metodi");
		titlesKeywords5.add("matematici");
		Paper paper5 = new Paper(5, "Metodi Matematici", 2013, "Gruppo DBM DLS", "token token mathematics", authorsNames3, authors3, keywords5, titlesKeywords5);
		
		
		// -- AUTORI --
		
		ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
		paperListStefania.add(paper1);
		paperListStefania.add(paper2);
		paperListStefania.add(paper5);
		
		ArrayList<Paper> paperListLuca = new ArrayList<Paper>();
		paperListLuca.add(paper3);
		paperListLuca.add(paper4);
		paperListLuca.add(paper5);
		
		Author authorStefania = new Author(1001, "Stefania", paperListStefania);
		Author authorLuca = new Author(1002, "Luca", paperListLuca);
		
		
		// -- CORPUS
		
		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
		listaAutoriNelCorpus.add(authorStefania);
		listaAutoriNelCorpus.add(authorLuca);
		
		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
		listaPaperNelCorpus.add(paper1);
		listaPaperNelCorpus.add(paper2);
		listaPaperNelCorpus.add(paper3);
		listaPaperNelCorpus.add(paper4);
		listaPaperNelCorpus.add(paper5);
		
		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus, listaPaperNelCorpus, listaPaperNelCorpus.size());
		
		ArrayList<Paper> restrictedCorpus = (ArrayList<Paper>)dummyCorpus.getRestrictedCorpus(listaAutoriNelCorpus);
				
		assertEquals("Gli autori hanno globalmente " + (authorStefania.getPapers().size() + authorLuca.getPapers().size()) + " articoli, mentre l'unione degli articoli conta " + restrictedCorpus.size() + " articoli.",  authorStefania.getPapers().size() + authorLuca.getPapers().size() - 1, restrictedCorpus.size());
	}



	/**
	 * Recupera i paper dei coautori, senza il self.
	 * In questo caso, non ci sono paper in comune.
	 * @throws Exception 
	 */
	@Test
	public void testGetCoAuthorsPapersSenzaPaperInComune() throws Exception {
		
		// -- PAPER --
		
		ArrayList<String> authorsNames1 = new ArrayList<String>();
		authorsNames1.add("Stefania");
		ArrayList<Integer> authors1 = new ArrayList<Integer>();
		authors1.add(1001);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(1002);
						
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
		

		// -- AUTORI --
		
		ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
		paperListStefania.add(paper1);
		paperListStefania.add(paper2);
		
		ArrayList<Paper> paperListLuca = new ArrayList<Paper>();
		paperListLuca.add(paper3);
		paperListLuca.add(paper4);
		
		Author authorStefania = new Author(1001, "Stefania", paperListStefania);
		Author authorLuca = new Author(1002, "Luca", paperListLuca);
		
		
		// -- CORPUS
		
		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
		listaAutoriNelCorpus.add(authorStefania);
		listaAutoriNelCorpus.add(authorLuca);
		
		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
		listaPaperNelCorpus.add(paper1);
		listaPaperNelCorpus.add(paper2);
		listaPaperNelCorpus.add(paper3);
		listaPaperNelCorpus.add(paper4);
		
		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus, listaPaperNelCorpus, listaPaperNelCorpus.size());
		
		List<Paper> listaPaperCoautoriStefania = new ArrayList<Paper>();
		listaPaperCoautoriStefania = dummyCorpus.getCoAuthorsPapers(authorStefania);
		
		List<Paper> listaPaperCoautoriLuca = new ArrayList<Paper>();
		listaPaperCoautoriLuca = dummyCorpus.getCoAuthorsPapers(authorLuca);

		assertEquals("L'insieme degli articoli dei coautori di " + authorStefania.getName() + ".",  0, listaPaperCoautoriStefania.size());
		assertEquals("L'insieme degli articoli dei coautori di " + authorLuca.getName() + ".", 0, listaPaperCoautoriLuca.size());
	}	
	

	/**
	 * Recupera i paper dei coautori, senza il self.
	 * In questo caso, paper 3, 4, 5.
	 * @throws Exception 
	 */
	@Test
	public void testGetCoAuthorsPapersConUnPaperInComune() throws Exception {
		
		// -- PAPER --
		
		ArrayList<String> authorsNames1 = new ArrayList<String>();
		authorsNames1.add("Stefania");
		ArrayList<Integer> authors1 = new ArrayList<Integer>();
		authors1.add(1001);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(1002);
				
		ArrayList<String> authorsNames3 = new ArrayList<String>();
		authorsNames3.add("Stefania");
		authorsNames3.add("Luca");
		ArrayList<Integer> authors3 = new ArrayList<Integer>();
		authors3.add(1001);
		authors3.add(1002);
		
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
		titlesKeywords5.add("metodi");
		titlesKeywords5.add("matematici");
		Paper paper5 = new Paper(5, "Metodi Matematici", 2013, "Gruppo DBM DLS", "token token mathematics", authorsNames3, authors3, keywords5, titlesKeywords5);
		
		
		// -- AUTORI --
		
		ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
		paperListStefania.add(paper1);
		paperListStefania.add(paper2);
		paperListStefania.add(paper5);
		
		ArrayList<Paper> paperListLuca = new ArrayList<Paper>();
		paperListLuca.add(paper3);
		paperListLuca.add(paper4);
		paperListLuca.add(paper5);
		
		Author authorStefania = new Author(1001, "Stefania", paperListStefania);
		Author authorLuca = new Author(1002, "Luca", paperListLuca);
		
		
		// -- CORPUS
		
		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
		listaAutoriNelCorpus.add(authorStefania);
		listaAutoriNelCorpus.add(authorLuca);
		
		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
		listaPaperNelCorpus.add(paper1);
		listaPaperNelCorpus.add(paper2);
		listaPaperNelCorpus.add(paper3);
		listaPaperNelCorpus.add(paper4);
		listaPaperNelCorpus.add(paper5);
		
		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus, listaPaperNelCorpus, listaPaperNelCorpus.size());
		
		List<Paper> listaPaperCoautoriStefania = new ArrayList<Paper>();
		listaPaperCoautoriStefania = dummyCorpus.getCoAuthorsPapers(authorStefania);
		
		List<Paper> listaPaperCoautoriLuca = new ArrayList<Paper>();
		listaPaperCoautoriLuca = dummyCorpus.getCoAuthorsPapers(authorLuca);

		assertEquals("L'insieme degli articoli dei coautori di " + authorStefania.getName() + ".",  3, listaPaperCoautoriStefania.size());
		assertEquals("L'insieme degli articoli dei coautori di " + authorLuca.getName() + ".", 3, listaPaperCoautoriLuca.size());
	}
	
	/**
	 * Controlla che il numero di paper del corpus ristretto
	 * sia uguale al numero di paper dell'autore
	 * (4 paper, 2 di Stefania, 2 di Luca, 0 in comune).
	 *  
	 * @throws Exception
	 */
	@Test
	public void testGetCoAuthorsAndSelfPapersSenzaPaperInComune() throws Exception {
		
		// -- PAPER --
		
		ArrayList<String> authorsNames1 = new ArrayList<String>();
		authorsNames1.add("Stefania");
		ArrayList<Integer> authors1 = new ArrayList<Integer>();
		authors1.add(1001);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(1002);
				
		
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
		
		
		// -- AUTORI --
		
		ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
		paperListStefania.add(paper1);
		paperListStefania.add(paper2);
		
		ArrayList<Paper> paperListLuca = new ArrayList<Paper>();
		paperListLuca.add(paper3);
		paperListLuca.add(paper4);
		
		Author authorStefania = new Author(1001, "Stefania", paperListStefania);
		Author authorLuca = new Author(1002, "Luca", paperListLuca);
		
		
		// -- CORPUS
		
		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
		listaAutoriNelCorpus.add(authorStefania);
		listaAutoriNelCorpus.add(authorLuca);
		
		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
		listaPaperNelCorpus.add(paper1);
		listaPaperNelCorpus.add(paper2);
		listaPaperNelCorpus.add(paper3);
		listaPaperNelCorpus.add(paper4);

		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus, listaPaperNelCorpus, listaPaperNelCorpus.size());
		
		List<Paper> listaPapersAuthorsAndSelfStefania = dummyCorpus.getCoAuthorsAndSelfPapers(authorStefania);
		List<Paper> listaPapersAuthorsAndSelfLuca = dummyCorpus.getCoAuthorsAndSelfPapers(authorLuca);
		
		assertEquals("I papers di " + authorStefania.getName() + " e dei suoi coautori conta " + listaPapersAuthorsAndSelfStefania.size() + ".", authorStefania.getPapers().size(), listaPapersAuthorsAndSelfStefania.size());
		assertEquals("I papers di " + authorLuca.getName() + " e dei suoi coautori conta " + listaPapersAuthorsAndSelfLuca.size() + ".", authorLuca.getPapers().size(), listaPapersAuthorsAndSelfLuca.size());
	}

	/**
	 * Controlla che il numero di paper del corpus ristretto
	 * sia uguale al numero di paper di Stefania e dei suoi
	 * coautori (5 paper, 3 di Stefania, 3 di Luca, 1 in comune - il 5).
	 *  
	 * @throws Exception
	 */
	@Test
	public void testGetCoAuthorsAndSelfPapersConUnPaperInComune() throws Exception {
		
		// -- PAPER --
		
		ArrayList<String> authorsNames1 = new ArrayList<String>();
		authorsNames1.add("Stefania");
		ArrayList<Integer> authors1 = new ArrayList<Integer>();
		authors1.add(1001);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(1002);
				
		ArrayList<String> authorsNames3 = new ArrayList<String>();
		authorsNames3.add("Stefania");
		authorsNames3.add("Luca");
		ArrayList<Integer> authors3 = new ArrayList<Integer>();
		authors3.add(1001);
		authors3.add(1002);
		
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
		titlesKeywords5.add("metodi");
		titlesKeywords5.add("matematici");
		Paper paper5 = new Paper(5, "Metodi Matematici", 2013, "Gruppo DBM DLS", "token token mathematics", authorsNames3, authors3, keywords5, titlesKeywords5);
		
		
		// -- AUTORI --
		
		ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
		paperListStefania.add(paper1);
		paperListStefania.add(paper2);
		paperListStefania.add(paper5);
		
		ArrayList<Paper> paperListLuca = new ArrayList<Paper>();
		paperListLuca.add(paper3);
		paperListLuca.add(paper4);
		paperListLuca.add(paper5);
		
		Author authorStefania = new Author(1001, "Stefania", paperListStefania);
		Author authorLuca = new Author(1002, "Luca", paperListLuca);
		
		
		// -- CORPUS
		
		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
		listaAutoriNelCorpus.add(authorStefania);
		listaAutoriNelCorpus.add(authorLuca);
		
		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
		listaPaperNelCorpus.add(paper1);
		listaPaperNelCorpus.add(paper2);
		listaPaperNelCorpus.add(paper3);
		listaPaperNelCorpus.add(paper4);
		listaPaperNelCorpus.add(paper5);		

		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus, listaPaperNelCorpus, listaPaperNelCorpus.size());
		
		List<Paper> listaPapersAuthorsAndSelfStefania = dummyCorpus.getCoAuthorsAndSelfPapers(authorStefania);
		List<Paper> listaPapersAuthorsAndSelfLuca = dummyCorpus.getCoAuthorsAndSelfPapers(authorLuca);
		
		assertEquals("I papers di " + authorStefania.getName() + " e dei suoi coautori conta " + dummyCorpus.getPapers().size() + ".", dummyCorpus.getPapers().size(), listaPapersAuthorsAndSelfStefania.size());
		assertEquals("I papers di " + authorLuca.getName() + " e dei suoi coautori conta " + dummyCorpus.getPapers().size() + ".", dummyCorpus.getPapers().size(), listaPapersAuthorsAndSelfLuca.size());
	}

	/**
	 * 2 autori, 3 paper, 1 in comune
	 * @throws Exception
	 */
	@Test
	public void testGetRestrictedIDF() throws Exception {
		
		// -- PAPER --
		
		ArrayList<String> authorsNames1 = new ArrayList<String>();
		authorsNames1.add("Stefania");
		ArrayList<Integer> authors1 = new ArrayList<Integer>();
		authors1.add(1001);
		
		ArrayList<String> authorsNames2 = new ArrayList<String>();
		authorsNames2.add("Luca");
		ArrayList<Integer> authors2 = new ArrayList<Integer>();
		authors2.add(1002);
				
		ArrayList<String> authorsNames3 = new ArrayList<String>();
		authorsNames3.add("Stefania");
		authorsNames3.add("Luca");
		ArrayList<Integer> authors3 = new ArrayList<Integer>();
		authors3.add(1001);
		authors3.add(1002);
		
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
		
		// paper 3 (in comune)
		ArrayList<String> keywords3 = new ArrayList<String>();
		keywords3.add("program");
		keywords3.add("algorithm");
		keywords3.add("parser");
		ArrayList<String> titlesKeywords3 = new ArrayList<String>();
		titlesKeywords3.add("program");
		titlesKeywords3.add("agile");
		titlesKeywords3.add("method");
		Paper paper3 = new Paper(3, "programming with agile method", 2013, "Gruppo DBM DLS", "program algorithm parser", authorsNames3, authors3, keywords3, titlesKeywords3);
		
		
		// -- AUTORI --
		
		ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
		paperListStefania.add(paper1);
		paperListStefania.add(paper3);
		
		ArrayList<Paper> paperListLuca = new ArrayList<Paper>();
		paperListLuca.add(paper2);
		paperListLuca.add(paper3);

		Author authorStefania = new Author(1001, "Stefania", paperListStefania);
		Author authorLuca = new Author(1002, "Luca", paperListLuca);
		
		
		// -- CORPUS
		
		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
		listaAutoriNelCorpus.add(authorStefania);
		listaAutoriNelCorpus.add(authorLuca);
		
		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
		listaPaperNelCorpus.add(paper1);
		listaPaperNelCorpus.add(paper2);
		listaPaperNelCorpus.add(paper3);		

		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus, listaPaperNelCorpus, listaPaperNelCorpus.size());
		
		
		String keyword = "algorithm";
		Double idf = 0.0;
		
		/*
		 *  il numero totale di paper nel corpus ristretto � 3 /
         *  "algorithm" compare 2 volte =
		 *  -------------------------------------------------------------
		 *  valore atteso = log (3 / 2)
		 */
		Double valAttesoIDF = Math.log((double)3/2);
		idf = dummyCorpus.getRestrictedIDF(keyword, authorStefania);
		double epsilon = (double)1/1000000000;
		assertEquals("Keyword: " + keyword +", IDF = "+ idf, valAttesoIDF, idf, epsilon);
	}
	
	@Test
	public void testR_withoutKey() {
//		fail("Not yet implemented");
	}

	@Test
	public void testN_withoutKey() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetU_ij() {
//		fail("Not yet implemented");
	}

	
	/**
	 * 2 autori, 3 paper, 1 in comune
	 * @throws Exception
	 */
	@Test
	public void testGetPFVector() throws Exception {
		// -- PAPER --

				ArrayList<String> authorsNames1 = new ArrayList<String>();
				authorsNames1.add("Stefania");
				ArrayList<Integer> authors1 = new ArrayList<Integer>();
				authors1.add(1001);

				ArrayList<String> authorsNames2 = new ArrayList<String>();
				authorsNames2.add("Luca");
				ArrayList<Integer> authors2 = new ArrayList<Integer>();
				authors2.add(1002);

				ArrayList<String> authorsNames3 = new ArrayList<String>();
				authorsNames3.add("Stefania");
				authorsNames3.add("Luca");
				ArrayList<Integer> authors3 = new ArrayList<Integer>();
				authors3.add(1001);
				authors3.add(1002);

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
				Paper paper1 = new Paper(1, "Testare i TF a uno", 2013,
						"Gruppo DBM DLS", "media media keyword keyword keyword",
						authorsNames1, authors1, keywords1, titlesKeywords1);

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
				Paper paper2 = new Paper(2, "Calcolare insieme degli articoli", 2013,
						"Gruppo DBM DLS", "algorithm algorithm parser parser",
						authorsNames1, authors1, keywords2, titlesKeywords2);

				// paper 3 (in comune)
				ArrayList<String> keywords3 = new ArrayList<String>();
				keywords3.add("program");
				keywords3.add("algorithm");
				keywords3.add("parser");
				ArrayList<String> titlesKeywords3 = new ArrayList<String>();
				titlesKeywords3.add("program");
				titlesKeywords3.add("agile");
				titlesKeywords3.add("method");
				Paper paper3 = new Paper(3, "programming with agile method", 2013,
						"Gruppo DBM DLS", "program algorithm parser", authorsNames3,
						authors3, keywords3, titlesKeywords3);

				// -- AUTORI --

				ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
				paperListStefania.add(paper1);
				paperListStefania.add(paper3);

				ArrayList<Paper> paperListLuca = new ArrayList<Paper>();
				paperListLuca.add(paper2);
				paperListLuca.add(paper3);

				Author authorStefania = new Author(1001, "Stefania", paperListStefania);
				Author authorLuca = new Author(1002, "Luca", paperListLuca);

				// -- CORPUS

				ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
				listaAutoriNelCorpus.add(authorStefania);
				listaAutoriNelCorpus.add(authorLuca);

				ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
				listaPaperNelCorpus.add(paper1);
				listaPaperNelCorpus.add(paper2);
				listaPaperNelCorpus.add(paper3);

				Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus,
						listaPaperNelCorpus, listaPaperNelCorpus.size());

		
		Map<String, Double> pfVectorStefania = dummyCorpus.getPFVector(authorStefania);

		//Printer.printVector(pfVectorStefania, 1);
		double epsilon = (double)1/1000000000;
		assertTrue(Normalization.isNormalized(pfVectorStefania, epsilon));
	}

	
	@Test
	public void testGetAuthors() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetPapers() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetCardinality() {
//		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
//		fail("Not yet implemented");
	}
	
	/**
	 * 1 autore, 3 paper
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDocumentTermMatrixDummy() throws Exception {

		// -- PAPER --

		ArrayList<String> authorsNames1 = new ArrayList<String>();
		authorsNames1.add("Stefania");
		ArrayList<Integer> authors1 = new ArrayList<Integer>();
		authors1.add(1001);


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
		Paper paper1 = new Paper(1, "Testare i TF a uno", 1900,
				"Gruppo DBM DLS", "media media keyword keyword keyword",
				authorsNames1, authors1, keywords1, titlesKeywords1);

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
		Paper paper2 = new Paper(2, "Calcolare insieme degli articoli", 2013,
				"Gruppo DBM DLS", "algorithm algorithm parser parser",
				authorsNames1, authors1, keywords2, titlesKeywords2);

		// paper 3 (in comune)
		ArrayList<String> keywords3 = new ArrayList<String>();
		keywords3.add("program");
		keywords3.add("algorithm");
		keywords3.add("parser");
		ArrayList<String> titlesKeywords3 = new ArrayList<String>();
		titlesKeywords3.add("program");
		titlesKeywords3.add("agile");
		titlesKeywords3.add("method");
		Paper paper3 = new Paper(3, "programming with agile method", 2013,
				"Gruppo DBM DLS", "program algorithm parser", authorsNames1,
				authors1, keywords3, titlesKeywords3);

		// -- AUTORI --

		ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
		paperListStefania.add(paper1);
		paperListStefania.add(paper2);
		paperListStefania.add(paper3);

		Author authorStefania = new Author(1001, "Stefania", paperListStefania);

		// -- CORPUS

		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
		listaAutoriNelCorpus.add(authorStefania);

		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
		listaPaperNelCorpus.add(paper1);
		listaPaperNelCorpus.add(paper2);
		listaPaperNelCorpus.add(paper3);

		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus,listaPaperNelCorpus, listaPaperNelCorpus.size());
		
		ArrayList<HashMap<String, Double>> documentTermMatrix = dummyCorpus.getDocumentTermMatrix(authorStefania);
		
		System.out.println("Document-term matrix");
		for(String s : authorStefania.getKeywordSet()) {
			System.out.print(s + ",\t\t\t");
		}
		System.out.print("\n");
		for(HashMap<String, Double> riga : documentTermMatrix) {
			for(Map.Entry<String, Double> cella : riga.entrySet()) {
				System.out.printf("%2f", cella.getValue()); System.out.print(",\t\t");
			}
			System.out.print("\n");
		}
		
		//System.out.println(dummyCorpus.getDocumentTermMatrix(authorStefania));
	}
	
//	/**
//	 * 1 autore, 3 paper
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	public void testPrintDocumentTermMatrixDummy() throws Exception {
//
//		// -- PAPER --
//
//		ArrayList<String> authorsNames1 = new ArrayList<String>();
//		authorsNames1.add("Stefania");
//		ArrayList<Integer> authors1 = new ArrayList<Integer>();
//		authors1.add(1001);
//
//
//		// paper 1
//		ArrayList<String> keywords1 = new ArrayList<String>();
//		keywords1.add("media");
//		keywords1.add("media");
//		keywords1.add("keyword");
//		keywords1.add("keyword");
//		keywords1.add("keyword");
//		ArrayList<String> titlesKeywords1 = new ArrayList<String>();
//		titlesKeywords1.add("testare");
//		titlesKeywords1.add("TF");
//		titlesKeywords1.add("uno");
//		Paper paper1 = new Paper(1, "Testare i TF a uno", 2013,
//				"Gruppo DBM DLS", "media media keyword keyword keyword",
//				authorsNames1, authors1, keywords1, titlesKeywords1);
//
//		// paper 2
//		ArrayList<String> keywords2 = new ArrayList<String>();
//		keywords2.add("algorithm");
//		keywords2.add("algorithm");
//		keywords2.add("parser");
//		keywords2.add("parser");
//		ArrayList<String> titlesKeywords2 = new ArrayList<String>();
//		titlesKeywords2.add("calcolare");
//		titlesKeywords2.add("insieme");
//		titlesKeywords2.add("degli");
//		titlesKeywords2.add("articoli");
//		Paper paper2 = new Paper(2, "Calcolare insieme degli articoli", 1900,
//				"Gruppo DBM DLS", "algorithm algorithm parser parser",
//				authorsNames1, authors1, keywords2, titlesKeywords2);
//
//		// paper 3 (in comune)
//		ArrayList<String> keywords3 = new ArrayList<String>();
//		keywords3.add("program");
//		keywords3.add("algorithm");
//		keywords3.add("parser");
//		ArrayList<String> titlesKeywords3 = new ArrayList<String>();
//		titlesKeywords3.add("program");
//		titlesKeywords3.add("agile");
//		titlesKeywords3.add("method");
//		Paper paper3 = new Paper(3, "programming with agile method", 2013,
//				"Gruppo DBM DLS", "program algorithm parser", authorsNames1,
//				authors1, keywords3, titlesKeywords3);
//
//		// -- AUTORI --
//
//		ArrayList<Paper> paperListStefania = new ArrayList<Paper>();
//		paperListStefania.add(paper1);
//		paperListStefania.add(paper2);
//		paperListStefania.add(paper3);
//
//		Author authorStefania = new Author(1001, "Stefania", paperListStefania);
//
//		// -- CORPUS
//
//		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
//		listaAutoriNelCorpus.add(authorStefania);
//
//		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
//		listaPaperNelCorpus.add(paper1);
//		listaPaperNelCorpus.add(paper2);
//		listaPaperNelCorpus.add(paper3);
//
//		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus,listaPaperNelCorpus, listaPaperNelCorpus.size());
//		
//		System.out.println("Stampa su file di Document-term matrix");
//		dummyCorpus.printDocumentTermMatrix(authorStefania);
//
//		//System.out.println(dummyCorpus.getDocumentTermMatrix(authorStefania));
//	}
	
}
