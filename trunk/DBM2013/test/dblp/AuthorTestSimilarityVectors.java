package dblp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.BeforeClass;
import org.junit.Test;

import dblp.Author;
import dblp.Corpus;
import dblp.Factory;
import dblp.Paper;
import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;

public class AuthorTestSimilarityVectors {
	
	private final static boolean ALL_VS_ALL = true;
	private final static boolean ONE_VS_ALL = true;
	private final static boolean PRINT = true;
	
	static Author authorStefania;
	static Author authorLuca;
	static Corpus dummyCorpus;
	static double epsilon = 1.0/1000000;
	
	static Author authorCandan;
	static Author authorSapino;
	static Corpus dblp;
	
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
		
	@BeforeClass
	public static void getEnvironment() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException {
		Factory f = new Factory();
		dblp = f.getCorpus();
		//Autore "Maria Luisa Sapino"
		authorSapino = dblp.getAuthorByID(1677020);
		
		//Autore "K. Selcuk Candan" (esiste anche un "K.S. Candan" che pero' non ha papers)
		authorCandan = dblp.getAuthorByID(1636579);
		
	}
	
	@Test
	public void testGetCosineSimilarityTFIDF2VectorAllVSAllBigData() throws AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		if(ALL_VS_ALL) {		
			ArrayList<Author> authors1 = dblp.getAuthors();
			ArrayList<Author> authors2 = dblp.getAuthors();
			@SuppressWarnings("unused")
			int a1ID, a2ID;
			double s12, s21;
			for(Author a1 : authors1) {
				a1ID = a1.getAuthorID();
				for(Author a2 : authors2) {
					a2ID = a2.getAuthorID();
					s12 = a1.getSimilarityOnTFIDF2Vector(a2, dblp);
					s21 = a2.getSimilarityOnTFIDF2Vector(a1, dblp);
					
//					System.out.println("S(" + a1ID + "," + a2ID + ")=" + s12);
//					System.out.println("S(" + a2ID + "," + a1ID + ")=" + s21);
					assertEquals(s12, s21, 0);
				}
			}
		}
	}
	
	@Test
	public void testGetCosineSimilarityPFVectorAllVSAllBigData() throws NoAuthorsWithSuchIDException {
		if(ALL_VS_ALL) {		
			ArrayList<Author> authors1 = dblp.getAuthors();
			ArrayList<Author> authors2 = dblp.getAuthors();
			@SuppressWarnings("unused")
			int a1ID, a2ID;
			double s12, s21;
			for(Author a1 : authors1) {
				a1ID = a1.getAuthorID();
				for(Author a2 : authors2) {
					a2ID = a2.getAuthorID();
					s12 = a1.getSimilarityOnPFVector(a2, dblp);
					s21 = a2.getSimilarityOnPFVector(a1, dblp);
					
//					System.out.println("S(" + a1ID + "," + a2ID + ")=" + s12);
//					System.out.println("S(" + a2ID + "," + a1ID + ")=" + s21);
					assertEquals(s12, s21, 0);
				}
			}
		}
	}
	
	@Test
	public void testGetCosineSimilarityKeywordVectorAllVSAllBigData() {
		if(ALL_VS_ALL) {		
			ArrayList<Author> authors1 = dblp.getAuthors();
			ArrayList<Author> authors2 = dblp.getAuthors();
			@SuppressWarnings("unused")
			int a1ID, a2ID;
			double s12, s21;
			for(Author a1 : authors1) {
				a1ID = a1.getAuthorID();
				for(Author a2 : authors2) {
					a2ID = a2.getAuthorID();
					s12 = a1.getSimilarityOnKeywordVector(a2, dblp);
					s21 = a2.getSimilarityOnKeywordVector(a1, dblp);
					
//					System.out.println("S(" + a1ID + "," + a2ID + ")=" + s12);
//					System.out.println("S(" + a2ID + "," + a1ID + ")=" + s21);
					assertEquals(s12, s21, 0);
				}
			}
		}
	}
	
	@Test
	public void testGetCosineSimilarityKeywordVectorOneVSAllSapinoBigData() {
		if(ONE_VS_ALL) {
			ArrayList<Author> authors2 = dblp.getAuthors();
			int a1ID, a2ID;
			double s12, s21;
			a1ID = authorSapino.getAuthorID();
			if(PRINT) {
				System.out.println("Sapino (" + a1ID + "):");
			}
			for(Author a2 : authors2) {
				a2ID = a2.getAuthorID();
				s12 = authorSapino.getSimilarityOnKeywordVector(a2, dblp);
				s21 = a2.getSimilarityOnKeywordVector(authorSapino, dblp);
				if(PRINT) {
					System.out.println("S(" + a1ID + "," + a2ID + ")=" + s12);
					System.out.println("S(" + a2ID + "," + a1ID + ")=" + s21);
				}
				assertEquals(s12, s21, 0);
			}
			
		}
	}
	
	@Test
	public void testGetCosineSimilarityKeywordVectorOneVSAllCandanBigData() {
		if(ONE_VS_ALL) {		
			ArrayList<Author> authors2 = dblp.getAuthors();
			int a1ID, a2ID;
			double s12, s21;
			a1ID = authorCandan.getAuthorID();
			if(PRINT) {
				System.out.println("Candan(" + a1ID + "):");
			}
			for(Author a2 : authors2) {
				a2ID = a2.getAuthorID();
				s12 = authorCandan.getSimilarityOnKeywordVector(a2, dblp);
				s21 = a2.getSimilarityOnKeywordVector(authorCandan, dblp);
				if(PRINT) {
					System.out.println("S(" + a1ID + "," + a2ID + ")=" + s12);
					System.out.println("S(" + a2ID + "," + a1ID + ")=" + s21);
				}
				assertEquals(s12, s21, 0);
			}			
		}
	}
	
	@Test
	public void testGetCosineSimilarityTFIDF2VectorOneVSAllSapinoBigData() throws AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {
			ArrayList<Author> authors2 = dblp.getAuthors();
			int a1ID, a2ID;
			double s12, s21;
			a1ID = authorSapino.getAuthorID();
			if(PRINT) {
				System.out.println("Sapino (" + a1ID + "):");
			}
			for(Author a2 : authors2) {
				a2ID = a2.getAuthorID();
				s12 = authorSapino.getSimilarityOnTFIDF2Vector(a2, dblp);
				s21 = a2.getSimilarityOnTFIDF2Vector(authorSapino, dblp);
				if(PRINT) {
					System.out.println("S(" + a1ID + "," + a2ID + ")=" + s12);
					System.out.println("S(" + a2ID + "," + a1ID + ")=" + s21);
				}
				assertEquals(s12, s21, 0);
			}
			
		}
	}
	
	@Test
	public void testGetCosineSimilarityTFIDF2VectorOneVSAllCandanBigData() throws AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {		
			ArrayList<Author> authors2 = dblp.getAuthors();
			int a1ID, a2ID;
			double s12, s21;
			a1ID = authorCandan.getAuthorID();
			if(PRINT) {
				System.out.println("Candan(" + a1ID + "):");
			}
			for(Author a2 : authors2) {
				a2ID = a2.getAuthorID();
				s12 = authorCandan.getSimilarityOnTFIDF2Vector(a2, dblp);
				s21 = a2.getSimilarityOnTFIDF2Vector(authorCandan, dblp);
				if(PRINT) {
					System.out.println("S(" + a1ID + "," + a2ID + ")=" + s12);
					System.out.println("S(" + a2ID + "," + a1ID + ")=" + s21);
				}
				assertEquals(s12, s21, 0);
			}
			
		}
	}
	
	@Test
	public void testGetCosineSimilarityPFVectorOneVSAllSapinoBigData() throws NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {
			ArrayList<Author> authors2 = dblp.getAuthors();
			int a1ID, a2ID;
			double s12, s21;
			a1ID = authorSapino.getAuthorID();
			if(PRINT) {
				System.out.println("Sapino (" + a1ID + "):");
			}
			for(Author a2 : authors2) {
				a2ID = a2.getAuthorID();
				s12 = authorSapino.getSimilarityOnPFVector(a2, dblp);
				s21 = a2.getSimilarityOnPFVector(authorSapino, dblp);
				if(PRINT) {
					System.out.println("S(" + a1ID + "," + a2ID + ")=" + s12);
					System.out.println("S(" + a2ID + "," + a1ID + ")=" + s21);
				}
				assertEquals(s12, s21, 0);
			}
			
		}
	}
	
	@Test
	public void testGetCosineSimilarityPFVectorOneVSAllCandanBigData() throws NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {		
			ArrayList<Author> authors2 = dblp.getAuthors();
			int a1ID, a2ID;
			double s12, s21;
			a1ID = authorCandan.getAuthorID();
			if(PRINT) {
				System.out.println("Candan(" + a1ID + "):");
			}
			for(Author a2 : authors2) {
				a2ID = a2.getAuthorID();
				s12 = authorCandan.getSimilarityOnPFVector(a2, dblp);
				s21 = a2.getSimilarityOnPFVector(authorCandan, dblp);
				if(PRINT) {
					System.out.println("S(" + a1ID + "," + a2ID + ")=" + s12);
					System.out.println("S(" + a2ID + "," + a1ID + ")=" + s21);
				}
				assertEquals(s12, s21, 0);
			}			
		}
	}
	
	@Test
	public void testGetSimilarAuthorsRankedByKeywordVectorSapino() throws NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {
			
			HashMap<String,Double> top10 = authorSapino.getSimilarAuthorsRankedByKeywordVector(dblp);
			
			if(PRINT) {
				System.out.println("10 autori più simili a Sapino (Keyword Vector):\n" + top10);
			}			
		}
	}
	
	@Test
	public void testGetSimilarAuthorsRankedByKeywordVectorCandan() throws NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {
			
			HashMap<String,Double> top10 = authorCandan.getSimilarAuthorsRankedByKeywordVector(dblp);
			
			if(PRINT) {
				System.out.println("10 autori più simili a Candan (Keyword Vector):\n" + top10);
			}			
		}
	}
	
	@Test
	public void testGetSimilarAuthorsRankedByPFVectorSapino() throws NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {
			
			HashMap<String,Double> top10 = authorSapino.getSimilarAuthorsRankedByPFVector(dblp);
			
			if(PRINT) {
				System.out.println("10 autori più simili a Sapino (PF Vector):\n" + top10);
			}			
		}
	}
	
	@Test
	public void testGetSimilarAuthorsRankedByPFVectorCandan() throws NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {
			
			HashMap<String,Double> top10 = authorCandan.getSimilarAuthorsRankedByPFVector(dblp);
			
			if(PRINT) {
				System.out.println("10 autori più simili a Candan (PF Vector):\n" + top10);
			}			
		}
	}
	
	@Test
	public void testGetSimilarAuthorsRankedByTFIDF2VectorSapino() throws NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {
			
			HashMap<String,Double> top10 = authorSapino.getSimilarAuthorsRankedByTFIDF2Vector(dblp);
			
			if(PRINT) {
				System.out.println("10 autori più simili a Sapino (TFIDF2 Vector):\n" + top10);
			}			
		}
	}
	
	@Test
	public void testGetSimilarAuthorsRankedByTFIDF2VectorCandan() throws NoAuthorsWithSuchIDException {
		if(ONE_VS_ALL) {
			
			HashMap<String,Double> top10 = authorCandan.getSimilarAuthorsRankedByTFIDF2Vector(dblp);
			
			if(PRINT) {
				System.out.println("10 autori più simili a Candan (TFIDF2 Vector):\n" + top10);
			}			
		}
	}
}
