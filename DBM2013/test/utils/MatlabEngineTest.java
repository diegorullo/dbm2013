package utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

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

public class MatlabEngineTest {
	
	private final static boolean DEBUG = true;
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
	public void evalTestDummy() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		MatlabEngine mle = MatlabEngine.getMatlabEngine();

		ArrayList<TreeMap<String, Double>> documentTermMatrix = authorStefania.getDocumentTermMatrix(dummyCorpus);
		if(DEBUG) {
			System.out.println("Scritto su file: Stefania.csv");
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/Stefania.csv");
			mle.eval("svd_V","Stefania.csv");
			mle.eval("pca_COEFF","Stefania.csv");
			System.out.println("----------------------------------------------------------------------\n");
		}
	}
	
	@Test
	public void evalTestSVDOn2390072() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
        	MatlabEngine mle = MatlabEngine.getMatlabEngine();
        	
			Author testAuthor = dblp.getAuthorByID(2390072);
			
			String fileName = testAuthor.getAuthorID() + ".csv";
			ArrayList<TreeMap<String, Double>> documentTermMatrix = testAuthor.getDocumentTermMatrix(dblp);
			
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/" + fileName);
			
			mle.eval("svd_V",fileName);
			ArrayList<ArrayList<Double>> matrix_v = IO.readDocumentTermMatrixFromFile("../data/V_" + fileName);

			if(PRINT) {
				System.out.println("Scritto su file: " + fileName);
				System.out.println("Letta matrice completa V da file:");
				Printer.printMatrix(matrix_v);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void evalTestPCAOn2390072() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
        	MatlabEngine mle = MatlabEngine.getMatlabEngine();
        	
			Author testAuthor = dblp.getAuthorByID(2390072);
			
			String fileName = testAuthor.getAuthorID() + ".csv";
			ArrayList<TreeMap<String, Double>> documentTermMatrix = testAuthor.getDocumentTermMatrix(dblp);
			
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/" + fileName);
			
			mle.eval("pca_COEFF",fileName);
			ArrayList<ArrayList<Double>> concepts = IO.readDocumentTermMatrixFromFile("../data/PCA_" + fileName);
			
			if(PRINT) {
				System.out.println("Numero di keyword di " + testAuthor.getAuthorID() + " = " + testAuthor.getKeywordSet().size());
				System.out.println("Scritto su file: " + fileName);
				System.out.println("Letta matrice completa dei concetti da file:");
				Printer.printMatrix(concepts);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSVDOn2390072Top5() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {
			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
			Author testAuthor = dblp.getAuthorByID(2390072);
			String testAuthorID = testAuthor.getAuthorID().toString();
		
			ArrayList<ArrayList<Double>> matrix_v = testAuthor.getSVD(dblp, testAuthorID, 5);			

			if(PRINT) {
				System.out.println("Letta matrice V (top 5) da file:");
				Printer.printMatrix(matrix_v);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSVDOn2390072Top3() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {
			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
			Author testAuthor = dblp.getAuthorByID(2390072);
			String testAuthorID = testAuthor.getAuthorID().toString();
		
			ArrayList<ArrayList<Double>> matrix_v = testAuthor.getSVD(dblp, testAuthorID, 3);			

			if(PRINT) {
				System.out.println("Letta matrice V (top 3) da file:");
				Printer.printMatrix(matrix_v);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetPCAOn2390072Top5() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {
			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
			Author testAuthor = dblp.getAuthorByID(2390072);
			String fileName = testAuthor.getAuthorID().toString();
			ArrayList<ArrayList<Double>> concepts = testAuthor.getPCA(dblp, fileName, 5);			

			if(PRINT) {
				System.out.println("Letta matrice dei (top 5) concetti da file:");
				Printer.printMatrix(concepts);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetPCAOn2390072Top3() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {
			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
			Author testAuthor = dblp.getAuthorByID(2390072);
			String fileName = testAuthor.getAuthorID().toString();
			ArrayList<ArrayList<Double>> concepts = testAuthor.getPCA(dblp, fileName, 3);

			if(PRINT) {
				System.out.println("Letta matrice dei (top 3) concetti da file:");
				Printer.printMatrix(concepts);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void evalTestSVDOn1636579() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
        	MatlabEngine mle = MatlabEngine.getMatlabEngine();
        	
			Author testAuthor = dblp.getAuthorByID(1636579);
			
			String fileName = testAuthor.getAuthorID() + ".csv";
			ArrayList<TreeMap<String, Double>> documentTermMatrix = testAuthor.getDocumentTermMatrix(dblp);
			
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/" + fileName);
			
			mle.eval("svd_V",fileName);
			ArrayList<ArrayList<Double>> matrix_v = IO.readDocumentTermMatrixFromFile("../data/V_" + fileName);

			if(PRINT) {
				System.out.println("Scritto su file: " + fileName);
				System.out.println("Letta matrice completa V da file:");
				Printer.printMatrix(matrix_v);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void evalTestPCAOn1636579() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
        	MatlabEngine mle = MatlabEngine.getMatlabEngine();
        	
			Author testAuthor = dblp.getAuthorByID(1636579);
			
			String fileName = testAuthor.getAuthorID() + ".csv";
			ArrayList<TreeMap<String, Double>> documentTermMatrix = testAuthor.getDocumentTermMatrix(dblp);
			
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/" + fileName);
			
			mle.eval("pca_COEFF",fileName);
			ArrayList<ArrayList<Double>> concepts = IO.readDocumentTermMatrixFromFile("../data/PCA_" + fileName);

			if(PRINT) {
				System.out.println("Numero di keyword di " + testAuthor.getAuthorID() + " = " + testAuthor.getKeywordSet().size());
				System.out.println("Scritto su file: " + fileName);
				System.out.println("Letta matrice completa dei concetti da file:");
				Printer.printMatrix(concepts);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSVDOn1636579Top5() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {
			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
			Author testAuthor = dblp.getAuthorByID(1636579);
			String testAuthorID = testAuthor.getAuthorID().toString();
		
			ArrayList<ArrayList<Double>> matrix_v = testAuthor.getSVD(dblp, testAuthorID, 5);			

			if(PRINT) {
				System.out.println("Letta matrice V (top 5) da file:");
				Printer.printMatrix(matrix_v);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetSVDOn1636579Top3() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		if(DEBUG) {
			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
			Author testAuthor = dblp.getAuthorByID(1636579);
			String testAuthorID = testAuthor.getAuthorID().toString();
		
			ArrayList<ArrayList<Double>> matrix_v = testAuthor.getSVD(dblp, testAuthorID, 3);			

			if(PRINT) {
				System.out.println("Letta matrice V (top 3) da file:");
				Printer.printMatrix(matrix_v);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetPCAOn1636579Top5() throws SQLException, MatlabConnectionException, MatlabInvocationException, NoAuthorsWithSuchIDException, IOException, AuthorWithoutPapersException {
		if(DEBUG) {
			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
			Author testAuthor = dblp.getAuthorByID(1636579);
			String fileName = testAuthor.getAuthorID().toString();
			ArrayList<ArrayList<Double>> concepts = testAuthor.getPCA(dblp, fileName, 5);

			if(PRINT) {
				System.out.println("Letta matrice dei (top 5) concetti da file:");
				Printer.printMatrix(concepts);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetPCAOn1636579Top3() throws SQLException, MatlabConnectionException, MatlabInvocationException, NoAuthorsWithSuchIDException, IOException, AuthorWithoutPapersException {
		if(DEBUG) {
			
			Factory f = new Factory();
        	Corpus dblp = f.getCorpus();
			Author testAuthor = dblp.getAuthorByID(1636579);
			String fileName = testAuthor.getAuthorID().toString();
			ArrayList<ArrayList<Double>> concepts = testAuthor.getPCA(dblp, fileName, 3);

			if(PRINT) {
				System.out.println("Letta matrice dei (top 3) concetti da file:");
				Printer.printMatrix(concepts);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
    @Test
    public void testGetSVDOn2390072() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
            if(DEBUG) {                   
                    Factory f = new Factory();
                    Corpus dblp = f.getCorpus();
                    Author testAuthor = dblp.getAuthorByID(2390072);
        			String fileName = testAuthor.getAuthorID().toString();
        			ArrayList<ArrayList<Double>> matrix_v = testAuthor.getSVD(dblp, fileName, 5);                       

        			if(PRINT) {
        				System.out.println("Letta matrice V completa da file:");
        				Printer.printMatrix(matrix_v);
        				System.out.println("----------------------------------------------------------------------\n");
        			}
            }
    }
   
    @Test
    public void testGetPCAOn2390072() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
            if(DEBUG) {                   
                    Factory f = new Factory();
                    Corpus dblp = f.getCorpus();
                    Author testAuthor = dblp.getAuthorByID(2390072);
           
        			String fileName = testAuthor.getAuthorID().toString();
        			ArrayList<ArrayList<Double>> concepts = testAuthor.getPCA(dblp, fileName, 5);                 

        			if(PRINT) {
        				System.out.println("Letta matrice completa dei concetti da file:");
        				Printer.printMatrix(concepts);
        				System.out.println("----------------------------------------------------------------------\n");
        			}
            }
    }


    @Test
    public void testGetSVDOn1636579() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
            if(DEBUG) {                   
                    Factory f = new Factory();
                    Corpus dblp = f.getCorpus();
                    Author testAuthor = dblp.getAuthorByID(1636579);
           
        			String fileName = testAuthor.getAuthorID().toString();
        			ArrayList<ArrayList<Double>> matrix_v = testAuthor.getSVD(dblp, fileName, 5);                          

        			if(PRINT) {
        				System.out.println("Letta matrice V completa da file:");
        				Printer.printMatrix(matrix_v);
        				System.out.println("----------------------------------------------------------------------\n");
        			}
            }
    }
   
    @Test
    public void testGetPCAOn1636579() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
            if(!DEBUG) {                   
                    Factory f = new Factory();
                    Corpus dblp = f.getCorpus();
                    Author testAuthor = dblp.getAuthorByID(1636579);
           
        			String fileName = testAuthor.getAuthorID().toString();
        			ArrayList<ArrayList<Double>> concepts = testAuthor.getPCA(dblp, fileName, 5);                 

        			if(PRINT) {
        				System.out.println("Letta matrice completa dei concetti da file:");
        				Printer.printMatrix(concepts);
        				System.out.println("----------------------------------------------------------------------\n");
        			}
            }
    }
    
    @Test
    public void testGetPCAOn1677020() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
            if(!DEBUG) {                   
                    Factory f = new Factory();
                    Corpus dblp = f.getCorpus();
                    Author testAuthor = dblp.getAuthorByID(1677020);
           
        			String fileName = testAuthor.getAuthorID().toString();
        			ArrayList<ArrayList<Double>> concepts = testAuthor.getPCA(dblp, fileName, 5);                  

        			if(PRINT) {
        				System.out.println("Letta matrice completa dei concetti da file:");
        				Printer.printMatrix(concepts);
        				System.out.println("----------------------------------------------------------------------\n");
        			}
            }
    }
    	
}
