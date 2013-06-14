package dblp;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NameNotFoundException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import utils.IO;
import utils.MatlabEngine;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoAuthorsWithSuchNameException;
import exceptions.NoPaperWithSuchIDException;

public class Corpus {

	private ArrayList<Author> authors;
	private ArrayList<Paper> papers;
	private int cardinality;
	
	public Corpus(ArrayList<Author> authors, ArrayList<Paper> papers, int cardinality) {
		super();
		this.authors = authors;
		this.papers = papers;
		this.cardinality = cardinality;
	}
	
	/**
	 * Restituisce l'oggetto paper associato all'id dato.
	 * 
	 * @param id
	 * @return oggetto Paper associato all'id dato
	 * @throws NoPaperWithSuchIDException 
	 *
	 */
	public Paper getPaperByID(int id) throws NoPaperWithSuchIDException {
		ArrayList<Paper> papersList = this.getPapers();
		for (Paper p : papersList) {
			if (id==p.getPaperID()) {
				return p;
			}
		}
		throw new NoPaperWithSuchIDException("ID: " + id);
	}
	
	/**
	 * Restituisce l'oggetto Author associato all'id dato.
	 * 
	 * @param id
	 * @return oggetto Author associato all'id dato
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public Author getAuthorByID(int id) throws NoAuthorsWithSuchIDException {
		ArrayList<Author> authorsList = this.getAuthors();
		for (Author a : authorsList) {
			if (id == a.getAuthorID()) {
				return a;
			}
		}
		throw new NoAuthorsWithSuchIDException("ID: " + id);
	}
	
	/**
	 * Restituisce l'oggetto Author associato al nome dato.
	 * 
	 * @param name
	 * @return oggetto Author associato al nome dato
	 * @throws NameNotFoundException
	 * @throws NoAuthorsWithSuchNameException 
	 */
	public Author getAuthorByName(String name) throws NoAuthorsWithSuchNameException {
		ArrayList<Author> authorsList = this.getAuthors();
		for (Author a : authorsList) {
			if (name.equals(a.getName())) {
				return a;
			}
		}
		throw new NoAuthorsWithSuchNameException("There is no author named '" + name + "' in the Corpus.");
	}
	
	/**
	 * Restituisce l'idf di una keyword.
	 * 
	 * @param keyword
	 * @return idf di una keyword
	 *
	 */
	public double getIDF(String keyword) {
		double idf = 0;
		int m = 0;
		int N = this.getCardinality();
		ArrayList<Paper> papersList = this.getPapers();

		// conta il numero di occorrenze della keyword s nel corpus
		for(Paper p : papersList) {
			TreeMap<String, Integer> keywordSet = p.getKeywordSetWithOccurrences();
			for(Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				if (k.getKey().equals(keyword)) {
					m++;
				}
			}				
		}
		if (N > 0 && m > 0) {
			idf = Math.log((double)N/m);
		}
		return idf;
	}
	
	/**
	 * Estrae la matrice di similarita' autore-autore su tutti gli autori del corpus
	 * 
	 * @return Table<Integer, Integer, Double> authorAuthorSimilarityMatrixOnKeywordVector
	 */
	public Table<Integer, Integer, Double> getAuthorAuthorSimilarityMatrixOnKeywordVector() {
		Table<Integer, Integer, Double> authorAuthorSimilarityMatrixOnKeywordVector = TreeBasedTable.create();
		
		ArrayList<Author> authors1 = this.getAuthors();
		ArrayList<Author> authors2 = this.getAuthors();
		int a1ID, a2ID;
		for(Author a1 : authors1) {
			a1ID = a1.getAuthorID();
			for(Author a2 : authors2) {
				a2ID = a2.getAuthorID();
				authorAuthorSimilarityMatrixOnKeywordVector.put(a1ID, a2ID, a1.getSimilarityOnKeywordVector(a2, this));
			}
		}
		return authorAuthorSimilarityMatrixOnKeywordVector;		
	}
	
	/**
	 * Calcola l'SVD a partire dalla matrice X,
	 * producendo i file relativi alle tre matrici S, U e V. 
	 * Restituisce al chiamante le prime 3 righe
	 * della matrice U, leggendola da file.
	 * 
	 * @param corpus il corpus di documenti a cui si fa riferimento
	 * @param inputFileName nome del file .csv da cui leggere la matrice X
	 * 
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException
	 * 
	 * @return le prime n righe della matrice U
	 */
	public ArrayList<ArrayList<Double>> getTop3SVDAuthor(String path, String inputFileName) throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		File csvFile = new File(path + inputFileName);
		MatlabEngine me = MatlabEngine.getMatlabEngine();
		me.init();		
		if (!csvFile.isFile()) {
			Table<Integer, Integer, Double> authorAuthorSimilarityMatrixOnKeywordVector = this.getAuthorAuthorSimilarityMatrixOnKeywordVector();
			IO.printTableOnFile(authorAuthorSimilarityMatrixOnKeywordVector, path, inputFileName);
		}
		me.eval("svd_U", inputFileName);
		ArrayList<ArrayList<Double>> svd = IO.readTopNDocumentTermMatrixFromFile(path + "/U_" + inputFileName, 3);
		return svd;
	}
	
	// Phase 2 - Task 2b
	
	/**
	 * Estrae la matrice di similarita' coautore-coautore su tutti gli autori del corpus
	 * 
	 * @return Table<Integer, Integer, Double> authorAuthorSimilarityMatrixOnKeywordVector
	 */
	public Table<Integer, Integer, Double> getCoAuthorCoAuthorSimilarityMatrixOnKeywordVector() {
				
		Table<Integer, Integer, Double> coAuthorCoAuthorSimilarityMatrixOnKeywordVector = TreeBasedTable.create();

		// Estraggo due copie della lista degli autori
		ArrayList<Author> authors1 = this.getAuthors();
		ArrayList<Author> authors2 = authors1;
		int a1ID, a2ID;
		
		// Le scorro e riempo la matrice
		for(Author a1 : authors1) {
			a1ID = a1.getAuthorID();
			for(Author a2 : authors2) {
				a2ID = a2.getAuthorID();
				// Se entrambi gli autori hanno coautori, aggiungo la loro similarita' coseno
				if (a1.hasCoAuthors() && a2.hasCoAuthors()) {
					coAuthorCoAuthorSimilarityMatrixOnKeywordVector.put(a1ID, a2ID, a1.getSimilarityOnKeywordVector(a2, this));
				}
				else {
					// Se siamo sull'intersezione di un autore con se' stesso, aggiungo 1.0
					if(a1ID == a2ID) {
						coAuthorCoAuthorSimilarityMatrixOnKeywordVector.put(a1ID, a2ID, 1.0);
					}
					else {
						// Se due autori non hanno coautori, aggiungo 0.0
						coAuthorCoAuthorSimilarityMatrixOnKeywordVector.put(a1ID, a2ID, 0.0);
					}
				}
			}
		}
		return coAuthorCoAuthorSimilarityMatrixOnKeywordVector;		
	}
	
	/**
	 * Calcola l'SVD a partire dalla matrice X,
	 * producendo i file relativi alle tre matrici S, U e V. 
	 * Restituisce al chiamante le prime 3 righe
	 * della matrice U, leggendola da file.
	 * 
	 * @param corpus il corpus di documenti a cui si fa riferimento
	 * @param inputFileName nome del file .csv da cui leggere la matrice X
	 * 
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException
	 * 
	 * @return le prime n righe della matrice U
	 * @throws NoAuthorsWithSuchIDException 
	 */
	public ArrayList<ArrayList<Double>> getTop3SVDCoAuthor(String path, String inputFileName) throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		File csvFile = new File(path + inputFileName);
		MatlabEngine me = MatlabEngine.getMatlabEngine();
		me.init();		
		if (!csvFile.isFile()) {
			Table<Integer, Integer, Double> coAuthorcoAuthorSimilarityMatrixOnKeywordVector = this.getCoAuthorCoAuthorSimilarityMatrixOnKeywordVector();
			IO.printTableOnFile(coAuthorcoAuthorSimilarityMatrixOnKeywordVector, path, inputFileName);
		}
		me.eval("svd_U", inputFileName);
		ArrayList<ArrayList<Double>> svd = IO.readTopNDocumentTermMatrixFromFile(path + "/U_" + inputFileName, 3);
		return svd;
	}
	
	// Phase 2 - Task 3a
	
		
	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}

	public int getCardinality() {
		return cardinality;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Corpus [authors=" + authors + ", papers=" + papers
				+ ", cardinality=" + cardinality + "]";
	}
}
