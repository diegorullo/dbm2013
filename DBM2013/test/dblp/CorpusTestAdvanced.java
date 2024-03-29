package dblp;

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

import com.google.common.collect.Table;

import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.WrongClusteringException;

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
	            String path = startingDirectory + "/../data/";
	            String fileName = "SimilarityMatrixAuthor.csv";
				
				IO.printTableOnFile(similarityMatrix, path, fileName);
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
	            String path = startingDirectory + "/../data/";
	            String fileName = "SimilarityMatrixAuthorDummy.csv";
				
				IO.printTableOnFile(dummySimilarityMatrix, path, fileName);
			}
		}
	}
	
	@Test
	public void testGetTop3SVDOnAuthorAuthorSimilarityMatrix() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		if(DEBUG) {
			String startingDirectory = System.getProperty("user.dir");
            String path = startingDirectory + "/../data/";
            String fileName = "SimilarityMatrixAuthor.csv";
			ArrayList<ArrayList<Double>> top3SVDMatrixAuthor = dblp.getTop3SVDAuthor(path, fileName);
			if(PRINT) {
				System.out.println("Top 3 SVD (Author)");
				Printer.printMatrix(top3SVDMatrixAuthor);
			}
		}
	}

	@Test
	public void testGetTop3SVDOnAuthorAuthorSimilarityMatrixDummy() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		if(DEBUG) {
			String startingDirectory = System.getProperty("user.dir");
            String path = startingDirectory + "/../data/";
            String fileName = "SimilarityMatrixAuthorDummy.csv";
			ArrayList<ArrayList<Double>> top3SVDMatrixAuthorDummy = dummyCorpus.getTop3SVDAuthor(path, fileName);
			if(PRINT) {
				System.out.println("Top 3 SVD (Author - Dummy)");
				Printer.printMatrix(top3SVDMatrixAuthorDummy);
			}
		}
	}
	
	@Test
	public void testGetCoAuthorCoAuthorSimilarityMatrixOnKeywordVector() throws NoAuthorsWithSuchIDException {
		if(DEBUG) {
			Table<Integer, Integer, Double> similarityMatrixCoAuthor = dblp.getCoAuthorCoAuthorSimilarityMatrixOnKeywordVector();
			if(PRINT) {
				System.out.println("Matrice di similarita' coautore-coautore (DBLP)");
				Printer.printAuthorAuthorSimilarityTableWithCaptions(similarityMatrixCoAuthor);
			}
			if(PRINT_ON_FILE) {
				String startingDirectory = System.getProperty("user.dir");
	            String path = startingDirectory + "/../data/";
	            String fileName = "SimilarityMatrixCoAuthor.csv";
				
				IO.printTableOnFile(similarityMatrixCoAuthor, path, fileName);
			}
		}
	}
	
	@Test
	public void testGetCoAuthorCoAuthorSimilarityMatrixOnKeywordVectorDummy() throws NoAuthorsWithSuchIDException {
		if(DEBUG) {
			Table<Integer, Integer, Double> dummySimilarityMatrixCoAuthor = dummyCorpus.getCoAuthorCoAuthorSimilarityMatrixOnKeywordVector();
			if(PRINT) {
				System.out.println("Matrice di similarita' coautore-coautore (Dummy)");
				Printer.printAuthorAuthorSimilarityTableWithCaptions(dummySimilarityMatrixCoAuthor);
			}
			if(PRINT_ON_FILE) {
				String startingDirectory = System.getProperty("user.dir");
	            String path = startingDirectory + "/../data/";
	            String fileName = "SimilarityMatrixCoAuthorDummy.csv";
				
				IO.printTableOnFile(dummySimilarityMatrixCoAuthor, path, fileName);
			}
		}
	}
	
	@Test
	public void testGetTop3SVDOnCoAuthorCoAuthorSimilarityMatrix() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		if(DEBUG) {
			String startingDirectory = System.getProperty("user.dir");
            String path = startingDirectory + "/../data/";
            String fileName = "SimilarityMatrixCoAuthor.csv";
			ArrayList<ArrayList<Double>> top3SVDMatrixCoAuthor = dblp.getTop3SVDCoAuthor(path, fileName);
			if(PRINT) {
				System.out.println("Top 3 SVD (CoAuthor)");
				Printer.printMatrix(top3SVDMatrixCoAuthor);
			}
		}
	}

	@Test
	public void testGetTop3SVDOnCoAuthorCoAuthorSimilarityMatrixDummy() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		if(DEBUG) {
			String startingDirectory = System.getProperty("user.dir");
            String path = startingDirectory + "/../data/";
            String fileName = "SimilarityMatrixCoAuthorDummy.csv";
			ArrayList<ArrayList<Double>> top3SVDMatrixCoAuthorDummy = dummyCorpus.getTop3SVDCoAuthor(path, fileName);
			if(PRINT) {
				System.out.println("Top 3 SVD (CoAuthor - Dummy)");
				Printer.printMatrix(top3SVDMatrixCoAuthorDummy);
			}
		}
	}
	
	@Test
	public void testGetClustersBasedOnConcepts() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoAuthorsWithSuchIDException, WrongClusteringException {
		if(DEBUG) {
			TreeMap<Integer, ArrayList<Author>> clustersBasedOnConcepts;
			
			String startingDirectory = System.getProperty("user.dir");
            String path = startingDirectory + "/../data/";
            String fileName = "SimilarityMatrixAuthorForClusters.csv";
			
			clustersBasedOnConcepts = dblp.getClustersBasedOnConcepts(path, fileName);
			System.out.println("Gruppi di autori, clusterizzati in base al \n grado di appartenenza alle 3 semantiche \n ottenute dalla matrice di similarita' author-author");
			System.out.println("Gruppo autori caratterizzati dalla semantica 1:");
			Printer.printAuthorsList(clustersBasedOnConcepts.get(0));
			System.out.println("Gruppo autori caratterizzati dalla semantica 2:");
			Printer.printAuthorsList(clustersBasedOnConcepts.get(1));
			System.out.println("Gruppo autori caratterizzati dalla semantica 3:");
			Printer.printAuthorsList(clustersBasedOnConcepts.get(2));
		}
	}
	
	@Test
	public void testGetConceptsKeywordVectors() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		if(DEBUG){
			Table<Integer, String, Double> conceptsKeywordVectors;
			
			String startingDirectory = System.getProperty("user.dir");
            String path = startingDirectory + "/../data/";
            String fileName = "SimilarityMatrixAuthorForConceptsVectors.csv";
            
            conceptsKeywordVectors = dblp.getConceptsKeywordVectors(path, fileName);
            System.out.println("Matrice Concetti Keyword");
            System.out.println("-----------------------------------------------");
            Printer.printConceptsKeywordsTableWithCaptions(conceptsKeywordVectors);
		}
	}

}
