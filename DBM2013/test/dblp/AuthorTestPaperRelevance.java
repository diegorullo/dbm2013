package dblp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.BeforeClass;
import org.junit.Test;

import utils.Printer;

import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoSuchTechniqueException;

public class AuthorTestPaperRelevance {
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
	public void testGetRankedByKeywordVectorStefania() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {

			LinkedHashMap<String,Double> similStefania = authorStefania.getRelevantPapersRankedByKeywordVector(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Stefania (KV): ");
				Printer.printLinkedHashMap(similStefania);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByTFIDF2Stefania() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similStefania = authorStefania.getRelevantPapersRankedByTFIDF2Vector(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Stefania (TFIDF2): ");
				Printer.printLinkedHashMap(similStefania);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByPFStefania() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similStefania = authorStefania.getRelevantPapersRankedByPFVector(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Stefania (PF): ");
				Printer.printLinkedHashMap(similStefania);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByPCAStefania() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similStefania = authorStefania.getRelevantPapersRankedByPCA(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Stefania (PCA): ");
				Printer.printLinkedHashMap(similStefania);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedBySVDStefania() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similStefania = authorStefania.getRelevantPapersRankedBySVD(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Stefania (SVD): ");
				Printer.printLinkedHashMap(similStefania);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByKeywordVectorLuca() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {

			LinkedHashMap<String,Double> similLuca = authorLuca.getRelevantPapersRankedByKeywordVector(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Luca (KV): ");
				Printer.printLinkedHashMap(similLuca);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByTFIDF2Luca() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similLuca = authorLuca.getRelevantPapersRankedByTFIDF2Vector(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Luca (TFIDF2): ");
				Printer.printLinkedHashMap(similLuca);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByPFLuca() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similLuca = authorLuca.getRelevantPapersRankedByPFVector(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Luca (PF): ");
				Printer.printLinkedHashMap(similLuca);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByPCALuca() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similLuca = authorLuca.getRelevantPapersRankedByPCA(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Luca (PCA): ");
				Printer.printLinkedHashMap(similLuca);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedBySVDLuca() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similLuca = authorLuca.getRelevantPapersRankedBySVD(dummyCorpus);
			if(PRINT) {
				System.out.println("Rilevanti per Luca (SVD): ");
				Printer.printLinkedHashMap(similLuca);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByKeywordVectorCandan() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similCandan = authorCandan.getRelevantPapersRankedByKeywordVector(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Candan (KV): ");
				Printer.printLinkedHashMap(similCandan);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByTFIDF2Candan() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similCandan = authorCandan.getRelevantPapersRankedByTFIDF2Vector(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Candan (TFIDF2): ");
				Printer.printLinkedHashMap(similCandan);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByPFCandan() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similCandan = authorCandan.getRelevantPapersRankedByPFVector(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Candan (PF): ");
				Printer.printLinkedHashMap(similCandan);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByPCACandan() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similCandan = authorCandan.getRelevantPapersRankedByPCA(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Candan (PCA): ");
				Printer.printLinkedHashMap(similCandan);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedBySVDCandan() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similCandan = authorCandan.getRelevantPapersRankedBySVD(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Candan (SVD): ");
				Printer.printLinkedHashMap(similCandan);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByKeywordVectorSapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similSapino = authorSapino.getRelevantPapersRankedByKeywordVector(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Sapino (KV): ");
				Printer.printLinkedHashMap(similSapino);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByTFIDF2Sapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similSapino = authorSapino.getRelevantPapersRankedByTFIDF2Vector(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Sapino (TFIDF2): ");
				Printer.printLinkedHashMap(similSapino);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByPFSapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similSapino = authorSapino.getRelevantPapersRankedByPFVector(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Sapino (PF): ");
				Printer.printLinkedHashMap(similSapino);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedByPCASapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similSapino = authorSapino.getRelevantPapersRankedByPCA(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Sapino (PCA): ");
				Printer.printLinkedHashMap(similSapino);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}
	
	@Test
	public void testGetRankedBySVDSapino() throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException, NoAuthorsWithSuchIDException {
		
		if(DEBUG) {
			
			LinkedHashMap<String,Double> similSapino = authorSapino.getRelevantPapersRankedBySVD(dblp);
			if(PRINT) {
				System.out.println("Rilevanti per Sapino (SVD): ");
				Printer.printLinkedHashMap(similSapino);
				System.out.println("----------------------------------------------------------------------\n");
			}
		}
	}


}
