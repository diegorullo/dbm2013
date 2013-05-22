package utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dblp.Author;
import dblp.Corpus;
import dblp.Factory;
import dblp.Paper;

public class MatlabEngineTest {
	
	private final static boolean DEBUG = true;
	static MatlabEngine me = new MatlabEngine();
	
	@BeforeClass
	public static void testSetup() throws SQLException, MatlabConnectionException, MatlabInvocationException {
		me.init();
	}

	@AfterClass
	public static void testCleanup() throws SQLException, MatlabInvocationException {
		me.shutdown();
	}
	
	@Test
	public void evalTestDummy() throws Exception {

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
		Paper paper2 = new Paper(2, "Calcolare insieme degli articoli", 1900,
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

		ArrayList<TreeMap<String, Double>> documentTermMatrix = authorStefania.getDocumentTermMatrix(dummyCorpus);
		if(DEBUG) {
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/Stefania.csv");
		}
		
		if(DEBUG) {
			me.eval("svd_IR","Stefania.csv");
			me.eval("pca_IR","Stefania.csv");
		}
	}
	
	@Test
	public void evalTestSVD() throws Exception {
		if(DEBUG) {
			
			Factory f = new Factory();
        	Corpus c = f.getCorpus();
			Author testAuthor = c.getAuthorByID(2390072);
			
			String fileName = testAuthor.getAuthorID() + ".csv";
			ArrayList<TreeMap<String, Double>> documentTermMatrix = testAuthor.getDocumentTermMatrix(c);
			
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/" + fileName);
			//System.out.println("Scritto su file: " + fileName);
			
			me.eval("svd_IR",fileName);
			ArrayList<ArrayList<Double>> matrix_v = IO.readDocumentTermMatrixFromFile("../data/" + testAuthor.getAuthorID() + ".csv");

			//System.out.println("Letta matrice V da file:");
			Printer.printMatrix(matrix_v);
		}
	}
}