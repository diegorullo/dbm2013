package utils;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.Test;

import dblp.Author;
import dblp.Corpus;
import dblp.Paper;
import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;

public class NormalizationTest {

	/**
	 * 2 autori, 3 paper, 1 in comune
	 * 
	 *
	 */
	@Test
	public void testNormalizedTFIDFVectorDummy() {

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

		TreeMap<String, Double> vettore = (TreeMap<String, Double>) paper1.getTFIDFVector(dummyCorpus);
		double epsilon = (double)1/1000000000;
		assertTrue(Normalization.isNormalized(vettore, epsilon));
	}

	/**
	 * 2 autori, 3 paper, 1 in comune
	 * @throws IOException 
	 * 
	 *
	 */
	@Test
	public void testNormalizedTFVectorDummy() throws IOException {

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

		TreeMap<String, Double> vettore = (TreeMap<String, Double>) paper1.getTFVector();
		double idf = dummyCorpus.getIDF("media");
		TreeMap<String, Double> vettoreTFIDF = new TreeMap<String, Double>();
		for (java.util.Map.Entry<String, Double> e : vettore.entrySet()) {
			vettoreTFIDF.put(e.getKey(), e.getValue() * idf);
		}
		double epsilon = (double)1/1000000000;
		assertTrue(Normalization.isNormalized(vettore, epsilon));
	}
	
	
	/**
	 * 2 autori, 3 paper, 1 in comune
	 * @throws AuthorWithoutPapersException 
	 * @throws IOException 
	 * 
	 *
	 */
	@Test
	public void testNormalizedWeightedTFVectorDummy() throws IOException, AuthorWithoutPapersException {

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

		TreeMap<String, Double> vettore = (TreeMap<String, Double>) authorStefania.getWeightedTFVector();
//		System.out.println(vettore);
		double epsilon = (double)1/1000000000;
		assertTrue(Normalization.isNormalized(vettore, epsilon));
	}
	
	/**
	 * 2 autori, 3 paper, 1 in comune
	 * 
	 *
	 */
	@Test
	public void testNormalizedWeightedTFIDFVectorDummy() {

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

		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus, listaPaperNelCorpus, listaPaperNelCorpus.size());

		TreeMap<String, Double> vettore = (TreeMap<String, Double>) authorStefania.getWeightedTFIDFVector(dummyCorpus);
//		System.out.println("ciao: " + vettore);
		double epsilon = (double)1/1000000000;
		assertTrue(Normalization.isNormalized(vettore, epsilon));
	}
	
	/**
	 * Caso 2 autori, 5 paper (3 a testa), 1 in comune
	 * @throws NoAuthorsWithSuchIDException 
	 * @throws AuthorWithoutPapersException 
	 * 
	 */	
	@Test
	public void testNormalizedTFIDF2VectorDummy() throws AuthorWithoutPapersException, NoAuthorsWithSuchIDException {

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
		
		TreeMap<String, Double> tfidf2VectorStefania = authorStefania.getTFIDF2Vector(dummyCorpus);
		double epsilon = (double)1/1000000000;
		assertTrue(Normalization.isNormalized(tfidf2VectorStefania, epsilon));
	}

	/**
	 * Caso 2 autori, 5 paper (3 a testa), 1 in comune
	 * @throws NoAuthorsWithSuchIDException 
	 * 
	 */	
	@Test
	public void testNormalizedPFVectorDummy() throws NoAuthorsWithSuchIDException {

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
		
		TreeMap<String, Double> pfVectorStefania = authorStefania.getPFVector(dummyCorpus);
		double epsilon = (double)1/1000000000;
//		System.out.println(pfVectorStefania);
		assertTrue(Normalization.isNormalized(pfVectorStefania, epsilon));
	}
	
}
