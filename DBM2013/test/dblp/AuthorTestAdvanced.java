package dblp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import utils.Similarity;

public class AuthorTestAdvanced {
	static Author authorStefania;
	static Author authorLuca;
	static Corpus dummyCorpus;
	static double epsilon = 1.0/1000000;

	@BeforeClass	
	public static void getDummyEnvironment() {
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

		authorStefania = new Author(1001, "Stefania", paperListStefania);
		authorLuca = new Author(1002, "Luca", paperListLuca);

		// -- CORPUS

		ArrayList<Author> listaAutoriNelCorpus = new ArrayList<Author>();
		listaAutoriNelCorpus.add(authorStefania);
		listaAutoriNelCorpus.add(authorLuca);

		ArrayList<Paper> listaPaperNelCorpus = new ArrayList<Paper>();
		listaPaperNelCorpus.add(paper1);
		listaPaperNelCorpus.add(paper2);
		listaPaperNelCorpus.add(paper3);
		listaPaperNelCorpus.add(paper4);

		dummyCorpus = new Corpus(listaAutoriNelCorpus,listaPaperNelCorpus, listaPaperNelCorpus.size());
	}
		
	@Test
	public void testGetCosineSimilarityKeywordVectorWithSelf() throws Exception {
		// 1b.1 keyword vectors	
		Double similarityStefaniaStefania = authorStefania.getSimilarityOnKeywordVector(authorStefania, dummyCorpus);
		Double similarityLucaLuca = authorLuca.getSimilarityOnKeywordVector(authorLuca, dummyCorpus);
		
//		System.out.println("Similarità Stefania-Stefania: " + similarityStefaniaStefania);
//		System.out.println("Similarità Luca-Luca: " + similarityLucaLuca);
		assertEquals(1.0, similarityStefaniaStefania, epsilon);
		assertEquals(1.0, similarityLucaLuca, epsilon);
	}
	
	@Test
	public void testGetCosineSimilarityKeywordVectorReflexive() throws Exception {
		// 1b.1 keyword vectors	
		Double similarityStefaniaLuca = authorStefania.getSimilarityOnKeywordVector(authorLuca, dummyCorpus);
		Double similarityLucaStefania = authorLuca.getSimilarityOnKeywordVector(authorStefania, dummyCorpus);
		
//		System.out.println("Similarità Stefania-Luca: " + similarityStefaniaLuca);
//		System.out.println("Similarità Luca-Stefania: " + similarityLucaStefania);
		assertEquals(similarityStefaniaLuca, similarityLucaStefania, epsilon);
	}
	
	@Test
	public void testGetCosineSimilarityTFIDF2VectorWithSelf() throws Exception {
		// 1b.2 TF-IDF2
		Double similarityStefaniaStefania = authorStefania.getSimilarityOnTFIDF2Vector(authorStefania, dummyCorpus);
		Double similarityLucaLuca = authorLuca.getSimilarityOnTFIDF2Vector(authorLuca, dummyCorpus);
		
//		System.out.println("Similarità Stefania-Stefania: " + similarityStefaniaStefania);
//		System.out.println("Similarità Luca-Luca: " + similarityLucaLuca);
		assertEquals(1.0, similarityStefaniaStefania, epsilon);
		assertEquals(1.0, similarityLucaLuca, epsilon);
	}
	
	@Test
	public void testGetCosineSimilarityTFIDF2VectorReflexive() throws Exception {
		// 1b.2 TF-IDF2		
		Double similarityStefaniaLuca = authorStefania.getSimilarityOnTFIDF2Vector(authorLuca, dummyCorpus);
		Double similarityLucaStefania = authorLuca.getSimilarityOnTFIDF2Vector(authorStefania, dummyCorpus);
		
//		System.out.println("Similarità Stefania-Luca: " + similarityStefaniaLuca);
//		System.out.println("Similarità Luca-Stefania: " + similarityLucaStefania);
		assertEquals(similarityStefaniaLuca, similarityLucaStefania, epsilon);
	}
	
	@Test
	public void testGetCosineSimilarityPFVectorWithSelf() throws Exception {
		// 1b.3 PF
		Double similarityStefaniaStefania = authorStefania.getSimilarityOnPFVector(authorStefania, dummyCorpus);
		Double similarityLucaLuca = authorLuca.getSimilarityOnPFVector(authorLuca, dummyCorpus);
		
//		System.out.println("Similarità Stefania-Stefania: " + similarityStefaniaStefania);
//		System.out.println("Similarità Luca-Luca: " + similarityLucaLuca);
		assertEquals(1.0, similarityStefaniaStefania, epsilon);
		assertEquals(1.0, similarityLucaLuca, epsilon);
	}	

	@Test
	public void testGetCosineSimilarityPFVectorReflexive() throws Exception {
		// 1b.3 PF
		Double similarityStefaniaLuca = authorStefania.getSimilarityOnPFVector(authorLuca, dummyCorpus);
		Double similarityLucaStefania = authorLuca.getSimilarityOnPFVector(authorStefania, dummyCorpus);
		
//		System.out.println("Similarità Stefania-Luca: " + similarityStefaniaLuca);
//		System.out.println("Similarità Luca-Stefania: " + similarityLucaStefania);
		assertEquals(similarityStefaniaLuca, similarityLucaStefania, epsilon);
	}
	
	@Test
	public void testGetCosineSimilarityReflexive() throws Exception {		
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
