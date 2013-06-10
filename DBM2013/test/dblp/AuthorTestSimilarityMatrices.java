package dblp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.BeforeClass;
import org.junit.Test;

import utils.IO;
import utils.Printer;

import dblp.Author;
import dblp.Corpus;
import dblp.Factory;
import dblp.Paper;
import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoSuchTechniqueException;

public class AuthorTestSimilarityMatrices {
	
	private final static boolean DEBUG = true;
	private final static boolean PRINT = true;
	private final static boolean PRINT_ON_FILE = !true;
	
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
	public void testGetTop5SVDCandan() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		
		if(DEBUG) {
			String authorCandanID = authorCandan.getAuthorID().toString();
			ArrayList<ArrayList<Double>> vMatrix = authorCandan.getSVD(dblp, authorCandanID, 5);
			ArrayList<TreeMap<String, Double>> topNMatrix = authorCandan.getTopN(vMatrix, 5);
			if(PRINT_ON_FILE) {
				IO.printDocumentTermMatrixOnFile(topNMatrix, "../data/SVD_Top5_" + authorCandan.getAuthorID() + ".csv");
			}
			if(PRINT) {
				System.out.println("Matrice V (" + authorCandan.getAuthorID() + "):");
				Printer.printMatrix(vMatrix);
				System.out.println("Matrice top 5 SVD (" + authorCandan.getAuthorID() + "): " + topNMatrix);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetTop5SVDStefania() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		
		if(DEBUG) {
			String authorStefaniaID = authorStefania.getAuthorID().toString();
			ArrayList<ArrayList<Double>> vMatrix = authorStefania.getSVD(dummyCorpus, authorStefaniaID, 5);
			ArrayList<TreeMap<String, Double>> topNMatrix = authorStefania.getTopN(vMatrix, 5);
			if(PRINT_ON_FILE) {
				IO.printDocumentTermMatrixOnFile(topNMatrix, "../data/SVD_Top5_" + authorStefania.getAuthorID() + ".csv");
			}
			if(PRINT) {
				System.out.println("Matrice V (" + authorStefania.getAuthorID() + "):");			
				Printer.printMatrix(vMatrix);
				System.out.println("Matrice top 5 SVD (" + authorStefania.getAuthorID() + "): " + topNMatrix);
				System.out.println("----------------------------------------------------------------------\n");
			}		
		}
	}
	
	@Test
	public void testGetTop5SVDSapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		
		if(DEBUG) {
			String authorSapinoID = authorSapino.getAuthorID().toString();
			ArrayList<ArrayList<Double>> vMatrix = authorSapino.getSVD(dblp, authorSapinoID, 5);
			ArrayList<TreeMap<String, Double>> topNMatrix = authorSapino.getTopN(vMatrix, 5);
			if(PRINT_ON_FILE) {
				IO.printDocumentTermMatrixOnFile(topNMatrix, "../data/SVD_Top5_" + authorSapino.getAuthorID() + ".csv");
			}
			if(PRINT) {
				System.out.println("Matrice V (" + authorSapino.getAuthorID() + "):");
				Printer.printMatrix(vMatrix);
				System.out.println("Matrice top 5 SVD (" + authorSapino.getAuthorID() + "): " + topNMatrix);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetTop5PCACandan() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		
		if(DEBUG) {
			String fileName = authorCandan.getAuthorID().toString();
			ArrayList<ArrayList<Double>> scoreLatentMatrix = authorCandan.getPCA(dblp, fileName, 5);
			ArrayList<TreeMap<String, Double>> topNMatrix = authorCandan.getTopN(scoreLatentMatrix, 5);
			if(PRINT_ON_FILE) {
				IO.printDocumentTermMatrixOnFile(topNMatrix, "../data/PCA_Top5_" + authorCandan.getAuthorID() + ".csv");
			}
			if(PRINT) {
				System.out.println("Matrice Score(" + authorCandan.getAuthorID() + "): " );
				Printer.printMatrix(scoreLatentMatrix);
				System.out.println("Matrice top 5 PCA (" + authorCandan.getAuthorID() + "): " + topNMatrix);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	
	@Test
	public void testGetTop5PCAStefania() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		
		if(DEBUG) {
			String fileName = authorStefania.getAuthorID().toString();
			ArrayList<ArrayList<Double>> scoreLatentMatrix = authorStefania.getPCA(dummyCorpus, fileName, 5);
			ArrayList<TreeMap<String, Double>> topNMatrix = authorStefania.getTopN(scoreLatentMatrix, 5);
			if(PRINT_ON_FILE) {
				IO.printDocumentTermMatrixOnFile(topNMatrix, "../data/PCA_Top5_" + authorStefania.getAuthorID() + ".csv");
			}
			if(PRINT) {
				System.out.println("Matrice Score(" + authorStefania.getAuthorID() + "): " );
				Printer.printMatrix(scoreLatentMatrix);
				System.out.println("Matrice top 5 PCA (" + authorStefania.getAuthorID() + "): " + topNMatrix);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetTop5PCASapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		
		if(DEBUG) {
			String fileName = authorSapino.getAuthorID().toString();
			ArrayList<ArrayList<Double>> scoreLatentMatrix = authorSapino.getPCA(dblp, fileName, 5);
			ArrayList<TreeMap<String, Double>> topNMatrix = authorSapino.getTopN(scoreLatentMatrix, 5);
			if(PRINT_ON_FILE) {
				IO.printDocumentTermMatrixOnFile(topNMatrix, "../data/PCA_Top5_" + authorSapino.getAuthorID() + ".csv");
			}
			if(PRINT) {
				System.out.println("Matrice Score(" + authorSapino.getAuthorID() + "): " );
				Printer.printMatrix(scoreLatentMatrix);
				System.out.println("Matrice top 5 PCA (" + authorSapino.getAuthorID() + "): " + topNMatrix);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilarityPCACandanSapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similCandan = authorCandan.getSimilarityOnConceptsMatrix(authorSapino, dblp, "PCA");
			if(PRINT) {
				System.out.println("Similarit� Candan-Sapino (PCA): " + similCandan);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilarityPCACandanSapinoReflexive() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		if(DEBUG) {
			Double similCandanSapino = authorCandan.getSimilarityOnConceptsMatrix(authorSapino, dblp, "PCA");
			Double similSapinoCandan = authorSapino.getSimilarityOnConceptsMatrix(authorCandan, dblp, "PCA");
			assertTrue(similCandanSapino.equals(similSapinoCandan));
			if(PRINT) {
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilarityPCAStefaniaLucaReflexive() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		if(DEBUG) {
			Double similStefaniaLuca = authorStefania.getSimilarityOnConceptsMatrix(authorLuca, dummyCorpus, "PCA");
			Double similLucaStefania = authorLuca.getSimilarityOnConceptsMatrix(authorStefania, dummyCorpus, "PCA");
			assertTrue(similStefaniaLuca.equals(similLucaStefania));
			if(PRINT) {
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilarityPCAStefaniaLuca() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similStefania = authorStefania.getSimilarityOnConceptsMatrix(authorLuca, dummyCorpus, "PCA");
			if(PRINT) {
				System.out.println("Similarit� Stefania-Luca (PCA): " + similStefania);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilarityPCAStefania() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similStefania = authorStefania.getSimilarityOnConceptsMatrix(authorStefania, dummyCorpus, "PCA");
			if(PRINT) {
				System.out.println("Similarit� Stefania-Stefania (PCA): " + similStefania);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilarityPCACandanCandan() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similCandan = authorCandan.getSimilarityOnConceptsMatrix(authorCandan, dblp, "PCA");
			if(PRINT) {
				System.out.println("Similarit� Candan-Candan (PCA): " + similCandan);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilarityPCASapinoSapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similSapino = authorSapino.getSimilarityOnConceptsMatrix(authorSapino, dblp, "PCA");
			if(PRINT) {
				System.out.println("Similarit� Sapino-Sapino (PCA): " + similSapino);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilaritySVDCandanSapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similCandan = authorCandan.getSimilarityOnConceptsMatrix(authorSapino, dblp, "SVD");
			if(PRINT) {
				System.out.println("Similarit� Candan-Sapino (SVD): " + similCandan);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilaritySVDCandanSapinoReflexive() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		if(DEBUG) {
			Double similCandanSapino = authorCandan.getSimilarityOnConceptsMatrix(authorSapino, dblp, "SVD");
			Double similSapinoCandan = authorSapino.getSimilarityOnConceptsMatrix(authorCandan, dblp, "SVD");
			assertTrue(similCandanSapino.equals(similSapinoCandan));
			if(PRINT) {
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilaritySVDStefaniaLucaReflexive() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		if(DEBUG) {
			Double similStefaniaLuca = authorStefania.getSimilarityOnConceptsMatrix(authorLuca, dummyCorpus, "SVD");
			Double similLucaStefania = authorLuca.getSimilarityOnConceptsMatrix(authorStefania, dummyCorpus, "SVD");
			assertTrue(similStefaniaLuca.equals(similLucaStefania));
			if(PRINT) {
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilaritySVDStefaniaLuca() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similStefania = authorStefania.getSimilarityOnConceptsMatrix(authorLuca, dummyCorpus, "SVD");
			if(PRINT) {
				System.out.println("Similarit� Stefania-Luca (SVD): " + similStefania);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilaritySVDStefania() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similStefania = authorStefania.getSimilarityOnConceptsMatrix(authorStefania, dummyCorpus, "SVD");
			if(PRINT) {
				System.out.println("Similarit� Stefania-Stefania (SVD): " + similStefania);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilaritySVDCandanCandan() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similCandan = authorCandan.getSimilarityOnConceptsMatrix(authorCandan, dblp, "SVD");
			if(PRINT) {
				System.out.println("Similarit� Candan-Candan (SVD): " + similCandan);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSimilaritySVDSapinoSapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		if(DEBUG) {
			Double similSapino = authorSapino.getSimilarityOnConceptsMatrix(authorSapino, dblp, "SVD");
			if(PRINT) {
				System.out.println("Similarit� Sapino-Sapino (SVD): " + similSapino);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testZero() {
		@SuppressWarnings("all")
		boolean test1 = (0.0 == -0.0);
		assertTrue(test1);
		
		Double zeroPlus = 0.0, zeroMinus = -0.0;
		boolean test2 = (zeroPlus.toString().equals(zeroMinus.toString()));
		assertFalse(test2);
		
		if(PRINT) {
			System.out.println("0.0 == -0.0? " + test1);
			System.out.println("0.0.toString() == -0.0.toString()? " + test2);
			System.out.println("----------------------------------------------------------------------\n");
		}
	}
	
}