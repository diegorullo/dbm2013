package dblp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.BeforeClass;
import org.junit.Test;

import utils.IO;
import utils.Printer;

import com.google.common.collect.Table;

import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;

public class CorpusTestAdvanced {
	
	private final static boolean DEBUG = true;
	private final static boolean PRINT = true;
	private final static boolean PRINT_ON_FILE = true;
	
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
	public void testGetAuthorAuthorSimilarityMatrixOnKeywordVector() {
		if(DEBUG) {
			Table<Integer, Integer, Double> similarityMatrix = dblp.getAuthorAuthorSimilarityMatrixOnKeywordVector();
			if(PRINT) {
				System.out.println("Matrice di similarita' autore-autore (DBLP)");
				Printer.printAuthorAuthorSimilarityTableWithCaptions(similarityMatrix);
			}
			if(PRINT_ON_FILE) {
				String startingDirectory = System.getProperty("user.dir");
	            String ioDirectory = startingDirectory + "/../data/";
	            String fileName = "SimilarityMatrix.csv";
				
				IO.printTableOnFile(similarityMatrix, ioDirectory, fileName);
			}
		}
	}
	
	@Test
	public void testGetAuthorAuthorSimilarityMatrixOnKeywordVectorDummy() {
		if(DEBUG) {
			Table<Integer, Integer, Double> dummySimilarityMatrix = dummyCorpus.getAuthorAuthorSimilarityMatrixOnKeywordVector();
			if(PRINT) {
				System.out.println("Matrice di similarita' autore-autore (Dummy)");
				Printer.printAuthorAuthorSimilarityTableWithCaptions(dummySimilarityMatrix);
			}
			if(PRINT_ON_FILE) {
				String startingDirectory = System.getProperty("user.dir");
	            String ioDirectory = startingDirectory + "/../data/";
	            String fileName = "SimilarityMatrixDummy.csv";
				
				IO.printTableOnFile(dummySimilarityMatrix, ioDirectory, fileName);
			}
		}
	}
	
	@Test
	public void testGetTop3SVD() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		if(DEBUG) {
			String startingDirectory = System.getProperty("user.dir");
            String ioDirectory = startingDirectory + "/../data/";
            String fileName = "SimilarityMatrix.csv";
			ArrayList<ArrayList<Double>> top3SVDMatrix = dblp.getTop3SVD(ioDirectory, fileName);
			if(PRINT) {
				System.out.println("Top 3 SVD");
				Printer.printMatrix(top3SVDMatrix);
			}
		}
	}

	@Test
	public void testGetTop3SVDDummy() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		if(DEBUG) {
			String startingDirectory = System.getProperty("user.dir");
            String ioDirectory = startingDirectory + "/../data/";
            String fileName = "SimilarityMatrixDummy.csv";
			ArrayList<ArrayList<Double>> top3SVDMatrixDummy = dummyCorpus.getTop3SVD(ioDirectory, fileName);
			if(PRINT) {
				System.out.println("Top 3 SVD (Dummy)");
				Printer.printMatrix(top3SVDMatrixDummy);
			}
		}
	}
	
}
