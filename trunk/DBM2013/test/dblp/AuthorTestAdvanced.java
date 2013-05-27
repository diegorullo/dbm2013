package dblp;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.Test;

import utils.Normalization;
import utils.Similarity;

public class AuthorTestAdvanced {

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

	@Test
	public void testGetRestrictedTF() {
//		fail("Not yet implemented");
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
	public void testGetCosineSimilaritySameAuthor() throws Exception {

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

		// paper 3
		ArrayList<String> keywords3 = new ArrayList<String>();
		keywords3.add("program");
		keywords3.add("algorithm");
		keywords3.add("parser");
		ArrayList<String> titlesKeywords3 = new ArrayList<String>();
		titlesKeywords3.add("program");
		titlesKeywords3.add("agile");
		titlesKeywords3.add("method");
		Paper paper3 = new Paper(3, "programming with agile method", 2013,
				"Gruppo DBM DLS", "program algorithm parser", authorsNames2,
				authors2, keywords3, titlesKeywords3);

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
		Paper paper4 = new Paper(4, "insieme dei token", 2013,
				"Gruppo DBM DLS", "method token parser parser", authorsNames2,
				authors2, keywords4, titlesKeywords4);

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

		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus,listaPaperNelCorpus, listaPaperNelCorpus.size());
		
		TreeMap<String, Double> weightedTFIDFVectorStefania = (TreeMap<String, Double>) authorStefania.getWeightedTFIDFVector(dummyCorpus);
		TreeMap<String, Double> weightedTFIDFVectorLuca = (TreeMap<String, Double>) authorLuca.getWeightedTFIDFVector(dummyCorpus);
		
//		System.out.println("Similarità Stefania-Stefania: " + Similarity.getCosineSimilarity(weightedTFIDFVectorStefania, weightedTFIDFVectorStefania));
		Assert.assertEquals(Similarity.getCosineSimilarity(weightedTFIDFVectorStefania, weightedTFIDFVectorStefania), 1.0);
		
//		System.out.println("Similarità Luca-Luca: " + Similarity.getCosineSimilarity(weightedTFIDFVectorLuca, weightedTFIDFVectorLuca));
		Assert.assertEquals(Similarity.getCosineSimilarity(weightedTFIDFVectorLuca, weightedTFIDFVectorLuca), 1.0);
	}
	
	@Test
	public void testGetCosineSimilarityReflexive() throws Exception {

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

		// paper 3
		ArrayList<String> keywords3 = new ArrayList<String>();
		keywords3.add("program");
		keywords3.add("algorithm");
		keywords3.add("parser");
		ArrayList<String> titlesKeywords3 = new ArrayList<String>();
		titlesKeywords3.add("program");
		titlesKeywords3.add("agile");
		titlesKeywords3.add("method");
		Paper paper3 = new Paper(3, "programming with agile method", 2013,
				"Gruppo DBM DLS", "program algorithm parser", authorsNames2,
				authors2, keywords3, titlesKeywords3);

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
		Paper paper4 = new Paper(4, "insieme dei token", 2013,
				"Gruppo DBM DLS", "method token parser parser", authorsNames2,
				authors2, keywords4, titlesKeywords4);

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

		Corpus dummyCorpus = new Corpus(listaAutoriNelCorpus,listaPaperNelCorpus, listaPaperNelCorpus.size());
		
		TreeMap<String, Double> weightedTFIDFVectorStefania = (TreeMap<String, Double>) authorStefania.getWeightedTFIDFVector(dummyCorpus);
		TreeMap<String, Double> weightedTFIDFVectorLuca = (TreeMap<String, Double>) authorLuca.getWeightedTFIDFVector(dummyCorpus);
		
//		System.out.println("Similarità Stefania-Luca: " + Similarity.getCosineSimilarity(weightedTFIDFVectorStefania, weightedTFIDFVectorLuca));
//		System.out.println("Similarità Luca-Stefania: " + Similarity.getCosineSimilarity(weightedTFIDFVectorLuca, weightedTFIDFVectorStefania));	
		Assert.assertEquals(Similarity.getCosineSimilarity(weightedTFIDFVectorStefania, weightedTFIDFVectorLuca), Similarity.getCosineSimilarity(weightedTFIDFVectorLuca, weightedTFIDFVectorStefania));
	}

	@Test
	public void testEqualsObject() {
//		fail("Not yet implemented");
	}

}
