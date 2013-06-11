package dblp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import utils.IO;
import utils.MatlabEngine;
import utils.Normalization;
import utils.Printer;
import utils.Similarity;
import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoSuchTechniqueException;

public class Author {
	//TODO: spiegare la scelta del 3
	final static int titleWeight = 3;
	private Integer authorID;
	private String name;
	private ArrayList<Paper> papers;
	private ArrayList<String> keywordSet;
	
	private TreeMap<String, Double> weightedTFVector;
	private TreeMap<String, Double> weightedTFIDFVector;
	private TreeMap<String, Integer> combinedKeywordSet;
	private TreeMap<String, Double> tFIDF2Vector;
	private TreeMap<String, Double> pFVector;
	
	private ArrayList<Integer> coAuthorsIDs;
	private ArrayList<Paper> coAuthorsPapers;
	private ArrayList<Paper> coAuthorsAndSelfPapers;
	
	private ArrayList<Integer> otherAuthorsIDs;
	private ArrayList<Author> otherAuthors;
	private ArrayList<Paper> otherAuthorsPapers;
	
	public Author(int personID, String name, ArrayList<Paper> papers) {
		super();
		this.authorID = personID;
		this.name = name;
		this.papers = papers;
	}
	
	
	// Phase 1 - Task 2

	/**
	 * Considera tutti gli articoli scritti da un certo autore per creare un
	 * combined keyword vector dei tf per quell'autore. Nei combined keyword
	 * vector, gli articoli piu recenti devono pesare di piu di quelli piu
	 * vecchi.
	 * 
	 * @return treemap dei tf di un autore, pesando gli articoli per eta'
	 * @throws IOException
	 * @throws AuthorWithoutPapersException 
	 */
	private TreeMap<String, Double> calculateWeightedTFVector() throws IOException, AuthorWithoutPapersException {
		TreeMap<String, Double> wtfv;
		double weight = 0;
		TreeMap<String, Double> weightedTFVector = new TreeMap<String, Double>();

		/*
		 * - vettore di tf per ogni paper dell'autore - età del paper - peso
		 * usando l'età - tfrkey[r] = sum(peso[i]*tfkey[i])/sum(peso[i]);
		 */
		ArrayList<Paper> paperList = this.getPapers();
		
		for (Paper p : paperList) {
			weight = p.getWeightBasedOnAge();
			wtfv = p.getWeightedTFVector(weight);
			for (Map.Entry<String, Double> k : wtfv.entrySet()) {
				if (!weightedTFVector.containsKey(k.getKey())) {
					weightedTFVector.put(k.getKey(), k.getValue());
				} else {
					weightedTFVector.put(k.getKey(),
							weightedTFVector.get(k.getKey()) + k.getValue());
				}
			}
		}

		TreeMap<String, Double> normalizedWeightedTFVector = Normalization.normalize(weightedTFVector);
		return normalizedWeightedTFVector;

	}
	
	public TreeMap<String, Double> getWeightedTFIDFVector(Corpus corpus) {
		if (weightedTFIDFVector == null){
			weightedTFIDFVector = calculateWeightedTFIDFVector(corpus);
		}
		return weightedTFIDFVector;		
	}

	/**
	 * Considera tutti gli articoli scritti da un certo autore per creare un
	 * combined keyword vector dei tfidf per quell'autore. Nei combined
	 * keyword vector, gli articoli piu recenti devono pesare di piu di
	 * quelli piu vecchi.
	 * 
	 * @param corpus il corpus di riferimento
	 * @return treemap dei tfidf di un autore, pesando gli articoli per eta'
	 *
	 */
	private TreeMap<String, Double> calculateWeightedTFIDFVector(Corpus corpus) {
		TreeMap<String, Double> weightedTFIDFVector = new TreeMap<String, Double>();
		TreeMap<String, Double> wtfidfv;
		double weight = 0;
		ArrayList<Paper> paperList = this.getPapers();
		
		/*
		 * - vettore di tfidf per ogni paper dell'autore - età del paper - peso
		 * usando l'età - tfidfrkey[r] = sum(peso[i]*tfidfkey[i])/sum(peso[i]);
		 */

		for (Paper p : paperList) {
			weight = p.getWeightBasedOnAge();
			wtfidfv = p.getWeightedTFIDFVector(weight, corpus);
			for (Map.Entry<String, Double> k : wtfidfv.entrySet()) {
				if (!weightedTFIDFVector.containsKey(k.getKey())) {
					weightedTFIDFVector.put(k.getKey(), k.getValue());
				} else {
					weightedTFIDFVector.put(k.getKey(),
							weightedTFIDFVector.get(k.getKey()) + k.getValue());
				}
			}
		}

		TreeMap<String, Double> normalizedWeightedTFIDFVector = Normalization.normalize(weightedTFIDFVector);

		return normalizedWeightedTFIDFVector;
	}
	
	// Phase 1 - Task 3
	
	public TreeMap<String, Integer> getCombinedKeywordSet() throws AuthorWithoutPapersException {
		if(combinedKeywordSet == null) {
			combinedKeywordSet = calculateCombinedKeywordSet();
		}
		return this.combinedKeywordSet;
	}

	/**
	 * Estrae l'insieme delle keyword presenti in tutti gli articoli
	 * dell'autore, con il rispettivo numero di occorrenze.
	 * 
	 * @return treemap delle keyword in tutti gli articoli dell'autore, con
	 *         occorrenze
	 * @throws AuthorWithoutPapersException 
	 */
	private TreeMap<String, Integer> calculateCombinedKeywordSet() throws AuthorWithoutPapersException {
		TreeMap<String, Integer> combinedKeywordSet = new TreeMap<String, Integer>();
		TreeMap<String, Integer> keywordSet = new TreeMap<String, Integer>();
		ArrayList<Paper> paperList = this.getPapers();

		for (Paper p : paperList) {
			keywordSet = p.getKeywordSetWithOccurrences();
			//System.out.println(keywordSet);
			for (Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				if (!combinedKeywordSet.containsKey(k.getKey())) {
					combinedKeywordSet.put(k.getKey(), k.getValue());
				} else {
					combinedKeywordSet.put(k.getKey(), combinedKeywordSet.get(k.getKey()) + k.getValue());
				}
			}
		}
		return combinedKeywordSet;
	}
	
	/**
	 * Estrae i coautori di un autore dato.
	 * 
	 * @param corpus il corpus di riferimento
	 * @return lista di Author: coautori di un autore dato
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public ArrayList<Author> getCoAuthors(Corpus corpus) throws NoAuthorsWithSuchIDException {
		ArrayList<Author> coAuthors = new ArrayList<Author>();
		ArrayList<Integer> coAuthorsIDs = this.getCoAuthorsIDs();

		for (int coA : coAuthorsIDs) {
			coAuthors.add(corpus.getAuthorByID(coA));
		}

		return coAuthors;
	}
	
	/**
	 * Estrae i coautori di un autore dato, insieme all'autore stesso.
	 * 
	 * @param corpus il corpus di riferimento
	 * @return lista di Author: coautori di un autore dato + autore stesso
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public ArrayList<Author> getCoAuthorsAndSelf(Corpus corpus) throws NoAuthorsWithSuchIDException {
		ArrayList<Author> coAuthorsAndSelf = this.getCoAuthors(corpus);
		coAuthorsAndSelf.add(this);
		return coAuthorsAndSelf;
	}
	
	public TreeMap<String, Double> getWeightedTFVector() throws IOException, AuthorWithoutPapersException {
		if(weightedTFVector == null) {
			weightedTFVector = calculateWeightedTFVector();
		}
		return this.weightedTFVector;
	}
	
	/**
	 * Estrae i nomi dei coautori dell'autore corrente.
	 * 
	 * @return lista di stringhe: nomi dei coautori dell'autore corrente
	 * @throws AuthorWithoutPapersException 
	 */
	public ArrayList<String> getCoAuthorsNames() throws AuthorWithoutPapersException {
		ArrayList<String> coAuthorsNames = new ArrayList<String>();
		ArrayList<Paper> paperList = this.getPapers();
		
		for (Paper p : paperList) {
			for (String coA : p.getAuthorsNames()) {
				if (!coAuthorsNames.contains(coA) && !this.getName().equals(coA)) {
					coAuthorsNames.add(coA);
				}
			}
		}
		return coAuthorsNames;
	}
	
	public ArrayList<Integer> getCoAuthorsIDs() {
		if(coAuthorsIDs == null) {
			coAuthorsIDs = calculateCoAuthorsIDs();
		}
		return coAuthorsIDs;
	}

	/**
	 * Estrae gli id dei coautori dell'autore corrente.
	 * 
	 * @return lista di interi: id dei coautori dell'autore corrente
	 * @throws AuthorWithoutPapersException 
	 */
	private ArrayList<Integer> calculateCoAuthorsIDs() {
		ArrayList<Integer> coAuthorsIDs = new ArrayList<Integer>();
		ArrayList<Paper> paperList = this.getPapers();
		
		for (Paper p : paperList) {
			for (int coA : p.getAuthors()) {
				if (!coAuthorsIDs.contains(coA) && this.getAuthorID() != (coA)) {
					coAuthorsIDs.add(coA);
				}
			}
		}
//		if (coAuthorsIDs.size()==0) {
//			throw new AuthorWithoutCoAuthorsException("L'autore id " + this.getAuthorID() + " non ha coautori.");
//		}
		return coAuthorsIDs;
	}
	
	/**
	 * Calcola il tf della keyword basandosi sull'insieme di tutti gli articoli
	 * scritti dall' autore dato.
	 * 
	 * @param keyword
	 * @return tf della keyword basandosi sugli articoli dell'autore
	 * @throws AuthorWithoutPapersException 
	 */
	public double getRestrictedTF(String keyword) throws AuthorWithoutPapersException {
		double tf = 0.0;
		ArrayList<Paper> rc = this.getPapers();
		// conta il numero di occorrenze della keyword nei papers di author
		int n = 0;
		int K = 0;
		for (Paper p : rc) {
			TreeMap<String, Integer> keywordSet = p.getKeywordSetWithOccurrences();

			if (keywordSet.get(keyword) != null) {
				n += keywordSet.get(keyword);
			}
			if (keywordSet != null) {
				K += p.getKeywords().size() + p.getTitlesKeywords().size() * titleWeight;
			}
		}
		tf = (double) n / K;
		return tf;
	}

	/**
	 * Calcola il numero di occorrenze di una keyword nel corpus ristretto,
	 * ovvero il corpus formato dai paper di un autore e dei suoi coautori.
	 * 
	 * @param keyword
	 * @param corpus il corpus di riferimento
	 * @return IDF calcolato in base al corpus ristretto
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public double getRestrictedIDF(String keyword, Corpus corpus) throws NoAuthorsWithSuchIDException {
		double idf = 0;
		int m = 0; // conta il numero di articoli in cui la keyword compare

		ArrayList<Author> coAuthorsAndSelf = this.getCoAuthorsAndSelf(corpus);
		ArrayList<Paper> restrictedCorpus = new ArrayList<Paper>();

		//estrae il corpus ristretto papers di autore+relativi coautori)
		for (Author a : coAuthorsAndSelf) {
			ArrayList<Paper> papersList = a.getPapers();
			for (Paper p : papersList) {
				if (!restrictedCorpus.contains(p)) {
					restrictedCorpus.add(p);
				}
			}
		}
		int N = restrictedCorpus.size();

		for (Paper p : restrictedCorpus) {
			TreeMap<String, Integer> keywordSet = p.getKeywordSetWithOccurrences();
			for (Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				if (k.getKey().equals(keyword)) {
					m++;
				}
			}
		}
		if (N > 0 && m > 0) {
			idf = Math.log((double) N / m);
		}

		return idf;
	}

	/**
	 * Calcola il tfidf2 dato un autore: tf2 - tutti gli articoli dell'autore
	 * idf2 - tutti gli articoli del corpus ristretto (ovvvero dell'autore e i
	 * suoi coautori)
	 * 
	 * @param keyword
	 * @param corpus il corpus di riferimento
	 * @return double tfidf2
	 * @throws AuthorWithoutPapersException 
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public double getTFIDF2(String keyword, Corpus corpus) throws AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		double tfidf2 = 0.0;
		double tf2 = this.getRestrictedTF(keyword);
		double idf2 = this.getRestrictedIDF(keyword, corpus);

		tfidf2 = tf2 * idf2;

		return tfidf2;
	}
	
	public TreeMap<String, Double> getTFIDF2Vector(Corpus corpus) throws AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		if (tFIDF2Vector == null){
			tFIDF2Vector = calculateTFIDF2Vector(corpus);
		}
		return tFIDF2Vector;		
	}

	/**
	 * Restituisce il keyword vector sotto forma di sequenza di coppie
	 * <keyword,weight> rispetto al modello di pesi TFIDF2, che per il calcolo
	 * del tf considera l'insieme di tutti gli articoli scritti dall'autore dato
	 * e per il calcolo dell'idf considera l'insieme degli articoli scritti
	 * dall'autore e dai suoi coautori.
	 * 
	 * @param corpus il corpus di riferimento
	 * @return keywordVector pesato in base al modello TFIDF2
	 * @throws NoAuthorsWithSuchIDException 
	 * @throws AuthorWithoutPapersException 
	 *
	 */
	private TreeMap<String, Double> calculateTFIDF2Vector(Corpus corpus) throws AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		TreeMap<String, Double> TFIDF2Vector = new TreeMap<String, Double>();
		ArrayList<Paper> paperList = this.getPapers();
		double tfidf2;
		String key;
		for (Paper p : paperList) {
			TreeMap<String, Integer> keywordSet = p.getKeywordSetWithOccurrences();
			for (Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				key = k.getKey();
				tfidf2 = this.getTFIDF2(key, corpus);
				TFIDF2Vector.put(key, tfidf2);
			}
		}

		//System.out.println("TFIDF2Vector: " + TFIDF2Vector);
		TreeMap<String, Double> normalizedTFIDF2Vector = Normalization.normalize(TFIDF2Vector);

		return normalizedTFIDF2Vector;

	}
	
	public ArrayList<Paper> getCoAuthorsPapers(Corpus corpus) throws NoAuthorsWithSuchIDException {
		if (coAuthorsPapers == null) {
			coAuthorsPapers = calculateCoAuthorsPapers(corpus);
		}
		return coAuthorsPapers;
	}

	/**
	 * Estrae gli articoli dei coautori di un autore dato.
	 * 
	 * @param corpus il corpus di riferimento
	 * @return lista di Paper: elenco dei paper dei coautori di un autore dato
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	private ArrayList<Paper> calculateCoAuthorsPapers(Corpus corpus) throws NoAuthorsWithSuchIDException {
		ArrayList<Author> coAuthors = this.getCoAuthors(corpus);
		ArrayList<Paper> coAuthorsPapers = new ArrayList<Paper>();

		for (Author coA : coAuthors) {
			List<Paper> authorsPapers = coA.getPapers();
			for (Paper p : authorsPapers) {
				if (!coAuthorsPapers.contains(p)) {
					coAuthorsPapers.add(p);
				}
			}
		}

		return coAuthorsPapers;
	}
	
	public ArrayList<Paper> getCoAuthorsAndSelfPapers(Corpus corpus) throws NoAuthorsWithSuchIDException	{
		if (coAuthorsAndSelfPapers == null) {
			coAuthorsAndSelfPapers = calculateCoAuthorsAndSelfPapers(corpus);
		}
		return coAuthorsAndSelfPapers;
	}
	
	/**
	 * Estrae gli articoli dei coautori di un autore dato, insieme a quelli
	 * dell'autore stesso.
	 * 
	 * @param a
	 * @return lista di Paper: elenco degli articoli di un autore dato insieme a
	 *         quelli dell'autore stesso
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	private ArrayList<Paper> calculateCoAuthorsAndSelfPapers(Corpus corpus) throws NoAuthorsWithSuchIDException {
		ArrayList<Paper> coAuthorsAndSelfPapers = this.getCoAuthorsPapers(corpus);
		ArrayList<Paper> paperList = this.getPapers();
		
		for (Paper p : paperList) {
			if (!coAuthorsAndSelfPapers.contains(p)) {
				coAuthorsAndSelfPapers.add(p);
			}
		}

		return coAuthorsAndSelfPapers;
	}
	
	public ArrayList<String> getKeywordSet() throws AuthorWithoutPapersException {
		if(keywordSet == null) {
			keywordSet = calculateKeywordSet();
		}
		return this.keywordSet;
	}
	
	/**
	 * Restituisce l'insieme delle keyword relativo a tutti i paper dell'autore,
	 * ordinato lessicograficamente.
	 * 
	 * @return arraylist delle keyword dell'autore, ordinato
	 * @throws AuthorWithoutPapersException 
	 */
	private ArrayList<String> calculateKeywordSet() throws AuthorWithoutPapersException {
		ArrayList<String> keywordSet = new ArrayList<String>();
		ArrayList<Paper> paperList = this.getPapers();
		
		for (Paper p : paperList) {
			ArrayList<String> ks = p.getKeywordSet();
			for (String k : ks) {
				if (!keywordSet.contains(k)) {
					keywordSet.add(k);
				}
			}
		}

		Collections.sort(keywordSet);

		return keywordSet;
	}

	/**
	 * Calcola il numero di articoli dei soli coautori dell'autore a_i che non
	 * contengono la chiave k_j.
	 * 
	 * @param k_i
	 * @param corpus il corpus di riferimento
	 * @return numero di articoli dei soli coautori dell'autore a_i che non
	 *         contengono la chiave k_j
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public int rWithoutKey(String k_j, Corpus corpus) throws NoAuthorsWithSuchIDException {
		List<Paper> coAuthorsPapers = this.getCoAuthorsPapers(corpus);
		int r_ij = 0;

		for (Paper p : coAuthorsPapers) {
			if (!p.containsKeyword(k_j)) {
				r_ij++;
			}
		}

		return r_ij;
	}

	/**
	 * Calcola il numero di articoli dell'autore a_i e dei suoi coautori che non
	 * contengono la chiave k_j.
	 * 
	 * @param k_i
	 * @param corpus il corpus di riferimento
	 * @return numero intero: gli articoli dell'autore a_i e dei suoi coautori
	 *         che non contengono la chiave k_j
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public int nWithoutKey(String k_j, Corpus corpus) throws NoAuthorsWithSuchIDException {
		List<Paper> coAuthorsPapers = this.getCoAuthorsAndSelfPapers(corpus);
		int n_ij = 0;

		for (Paper p : coAuthorsPapers) {
			if (!p.containsKeyword(k_j)) {
				n_ij++;
			}
		}

		return n_ij;
	}

	/**
	 * Calcola il peso u_ij della keyword k_j per l'autore a_i.
	 * 
	 * @param k_i
	 * @param corpus il corpus di riferimento
	 * @return peso u_ij della keyword k_j per l'autore a_i
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public double getU_ij(String k_j, Corpus corpus) throws NoAuthorsWithSuchIDException {
		double u_ij = 0.0;
		double epsilon = 0.1; // costante che non fa andare a zero
		double numLog = 0.0;
		double denLog = 0.0;
		double resLog = 0.0;
		double resAbs = 0.0;

		double r_ij = (double) this.rWithoutKey(k_j, corpus);
		double n_ij = (double) this.nWithoutKey(k_j, corpus);		
		double R_i = (double) this.getCoAuthorsPapers(corpus).size();
		double N_i = (double) this.getCoAuthorsAndSelfPapers(corpus).size();
		
		numLog = (r_ij) / (R_i - r_ij + epsilon);
		denLog = (n_ij - r_ij + epsilon) / (N_i - n_ij - R_i + r_ij + epsilon);
		// Aggiunto 1 + ... all'argomento del logaritmo (come visto il 2
		// maggio a lezione)
		
		// Modificato inserendo la frazione all'interno di un valore assoluto
		// per evitare casi in cui l'argomento del logaritmo usciva dal dominio
		resLog = Math.log(1 + Math.abs((numLog / denLog)));

		resAbs = Math.abs(((r_ij + epsilon) / (R_i + epsilon)) - ((n_ij - r_ij + epsilon) / (N_i - R_i + epsilon)));

		u_ij = resLog * resAbs;
//		System.out.println(">>>>r_ij: " + r_ij + ", n_ij: " + n_ij + ", R_i: " + R_i + ", N_i: " + N_i);
//		System.out.println(">>>>den1: " + (R_i + epsilon) + ", den2: "  + (N_i - R_i + epsilon));
//		System.out.println(">>>>numLog: " + numLog + ", denLog: " + denLog + ", resLog: " + resLog + ", resAbs: " + resAbs);
		return u_ij;
	}
	
	public TreeMap<String, Double> getPFVector(Corpus corpus) throws NoAuthorsWithSuchIDException {
		if (pFVector == null){
			pFVector = calculatePFVector(corpus);
		}
		return pFVector;		
	}

	/**
	 * Restituisce il keyword vector sotto forma di sequenza di coppie
	 * <keyword,weight> rispetto al modello di pesi PF, che per il calcolo dei
	 * pesi considera l'insieme di tutti gli articoli scritti dall'autore dato e
	 * l'insieme degli articoli scritti dall'autore e dai suoi coautori.
	 * Utilizza il meccanismo di feedback probabilistico (PF).
	 * 
	 * @param corpus il corpus di riferimento
	 * @return keywordVector pesato in base al modello PF
	 * @throws NoAuthorsWithSuchIDException 
	 */
	private TreeMap<String, Double> calculatePFVector(Corpus corpus) throws NoAuthorsWithSuchIDException {
		TreeMap<String, Double> PFVector = new TreeMap<String, Double>();
		ArrayList<Paper> papersList = this.getPapers();
		double pf;
		String key;
		
		for (Paper p : papersList) {
			TreeMap<String, Integer> keywordSet = p.getKeywordSetWithOccurrences();
//			System.out.println(">>>>>>>>>>>>>Paper=" + p.getPaperID() + ": " + keywordSet);
			for (Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				key = k.getKey();
				pf = this.getU_ij(key, corpus);
//				System.out.println(">>>>>>>k="+key + ", pf=" + pf);
				PFVector.put(key, pf);
			}
		}
		
		//System.out.println("PFVector: " + PFVector);
		TreeMap<String, Double> normalizedPFVector = Normalization.normalize(PFVector);
		//System.out.println("normalizedPFVector: " + normalizedPFVector);

		return normalizedPFVector;
	}
	
	// Phase 2 - Task 1a

	/**
	 * Restituisce la matrice document-term relativa all'autore selezionato
	 * 
	 * @param a autore
	 * @return ArrayList<TreeMap<String, Double>> matrice document-term
	 * @throws AuthorWithoutPapersException 
	 *
	 */
	public ArrayList<TreeMap<String, Double>> getDocumentTermMatrix(Corpus corpus) throws AuthorWithoutPapersException {
		ArrayList<TreeMap<String, Double>> documentTermMatrix = new ArrayList<TreeMap<String, Double>>();

		ArrayList<String> keywordSet = this.getKeywordSet();
		ArrayList<Paper> documents = this.getPapers();

		int m = documents.size();
		// int n = keywordSet.size();

		// inizializziamo la matrice con tutti valori a 0
		for (int doc = 0; doc < m; doc++) {
			TreeMap<String, Double> row = new TreeMap<String, Double>();
			for (String s : keywordSet) {
				row.put(s, 0.0);
			}
			documentTermMatrix.add(row);
		}

		// inseriamo i valori di tfidf relativi al vettore dei vari documenti
		TreeMap<String, Double> currentWeightedTFIDFVector = new TreeMap<String, Double>();
		for (int doc = 0; doc < m; doc++) {
			// recupera il paper corrente...
			Paper currentPaper = documents.get(doc);
			// System.out.println("Paper: \'" + currentPaper.getTitle() +
			// "\', peso: " + currentPaper.getWeightBasedOnAge());
			// ... e ne calcola il vettore di tfidf pesato
			currentWeightedTFIDFVector = currentPaper.getWeightedTFIDFVector(currentPaper.getWeightBasedOnAge(), corpus);
			// System.out.println("WeightedTFIDFVector: \'" +
			// currentWeightedTFIDFVector);
			// modifica la riga relativa al documento corrente, sostituendo gli
			// zeri con i valori
			TreeMap<String, Double> row = documentTermMatrix.get(doc);
			for (Map.Entry<String, Double> entry : currentWeightedTFIDFVector.entrySet()) {
				row.put(entry.getKey(), entry.getValue());
				// documentTermMatrix.set(doc,
				// documentTermMatrix.get(doc).put(entry.getKey(),
				// entry.getValue()))
			}
			documentTermMatrix.set(doc, row);
		}

		return documentTermMatrix;
	}

	/**
	 * Ricostruisce l'associazione Key-value delle top PCA/SVD
	 * @param nMatrix matrice di soli valori double
	 * @param topN numero di top desiderati
	 * @return matrice delle n top con associazione delle keyword
	 * @throws AuthorWithoutPapersException 
	 *
	 */
	public ArrayList<TreeMap<String, Double>> getTopN(ArrayList<ArrayList<Double>> nMatrix,int topN) throws AuthorWithoutPapersException {
		ArrayList<TreeMap<String, Double>> topNMatrix = new ArrayList<TreeMap<String, Double>>();
		ArrayList<String> keywordSet = this.getKeywordSet();
		if(topN > nMatrix.size())
		{
			topN = nMatrix.size();
		}
		for(int i=0;i<topN;i++)
		{
			ArrayList<Double> currRow = nMatrix.get(i);
			TreeMap<String, Double> newRow = new TreeMap<String, Double>();
			int j = 0;
			for (String s : keywordSet) 
			{
				newRow.put(s, currRow.get(j));
				j++;
			}
			topNMatrix.add(newRow);
		}
		
		return topNMatrix;
	}
	   
	/**
	 * Calcola la matrice PCA per l'autore corrente	 
	 * @param topN numero degli autovalori della latent
	 * @return matrice PCA per l'autore corrente
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException 
	 * @throws AuthorWithoutPapersException 
	 *
	 */
	public ArrayList<ArrayList<Double>> getPCA(Corpus corpus, String inputFileName, int topN) throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		String startingDirectory = System.getProperty("user.dir");
		String ioDirectory = startingDirectory + "/../data/";
		String fileName = inputFileName + ".csv";
		File csvFile = new File(ioDirectory + fileName);
		MatlabEngine me = MatlabEngine.getMatlabEngine();
		me.init();
		if (!csvFile.isFile()) {
			ArrayList<TreeMap<String, Double>> documentTermMatrix = this.getDocumentTermMatrix(corpus);
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/"	+ fileName);
		}
		me.eval("pca_COEFF", fileName);

		ArrayList<ArrayList<Double>> pcaScore = IO.readTopNDocumentTermMatrixFromFile("../data/PCA_" + fileName,topN);

		return pcaScore;
	}

	
	/**
	 * Calcola l'SVD a partire dalla matrice X,
	 * producendo i file relativi alle tre matrici S, U e V. 
	 * Restituisce al chiamante le prime n righe
	 * della matrice V', leggendola da file.
	 * 
	 * @param corpus il corpus di documenti a cui si fa riferimento
	 * @param inputFileName nome del file .csv da cui leggere la matrice X
	 * @param topN numero delle prime n righe della matrice V'
	 * 
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException 
	 * @throws AuthorWithoutPapersException 
	 * 
	 * @return le prime n righe della matrice V'
	 */
	public ArrayList<ArrayList<Double>> getSVD(Corpus corpus, String inputFileName, int topN) throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException {
		String startingDirectory = System.getProperty("user.dir");
		String ioDirectory = startingDirectory + "/../data/";
		String fileName = inputFileName + ".csv";
		File csvFile = new File(ioDirectory + fileName);
		MatlabEngine me = MatlabEngine.getMatlabEngine();
		me.init();		
		if (!csvFile.isFile()) {
			ArrayList<TreeMap<String, Double>> documentTermMatrix = this.getDocumentTermMatrix(corpus);
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, ioDirectory + fileName);
		}
		me.eval("svd_V", fileName);
		ArrayList<ArrayList<Double>> svd = IO.readTopNDocumentTermMatrixFromFile(ioDirectory + "/V_" + fileName, topN);
		return svd;
	}
	
	/**
	 * Restituisce tutti i fattori di latent (PCA)
	 * @return valori degli autovalori di latent (PCA)
	 *
	 */
	public ArrayList<Double> getLatentPCA() {
		String fileName = this.getAuthorID() + ".csv";
		
		ArrayList<ArrayList<Double>> latentPCA = IO.readDocumentTermMatrixFromFile("../data/latent_" + fileName);
		ArrayList<Double> latentPCAVector = new ArrayList<Double>();

		for(int i = 0; i < latentPCA.size(); i++)
		{
			ArrayList<Double> latentCurr = latentPCA.get(i);
			
			latentPCAVector.add(latentCurr.get(0));
		}		
		return latentPCAVector;
	}
	
	/**
	 * Restituisce i primi 5 fattori di S (SVD)
	 * @return valori degli autovalori di S (SVD)
	 *
	 */
	public ArrayList<Double> getLatentSVD() {
		String fileName = this.getAuthorID() + ".csv";
		
		ArrayList<ArrayList<Double>> latentSVD = IO.readDocumentTermMatrixFromFile("../data/S_" + fileName);
		ArrayList<Double> latentSVDVector = new ArrayList<Double>();
		int rows = 0;
		for(int i = 0; i < latentSVD.size(); i++) {
			ArrayList<Double> latentCurr = latentSVD.get(i);
			latentSVDVector.add(latentCurr.get(i));
			rows++;
		}
		
		while(rows < 5) {
			latentSVDVector.add(0.0);
			rows++;
		}
		
		return latentSVDVector;
	}
	
	/**
	 * Restituisce n_top fattori di latent 
	 * @param topN numero degli autovalori della latent
	 * @return valori degli n_top autovalori di latent
	 *
	 */
	public ArrayList<Double> getLatentPCATopN(int topN) {
		String fileName = this.getAuthorID() + ".csv";
		
		ArrayList<ArrayList<Double>> latentPCA = IO.readTopNDocumentTermMatrixFromFile("../data/latent_" + fileName,topN);
		ArrayList<Double> latentPCAVector = new ArrayList<Double>();
		
		if(topN > latentPCA.size())
		{
			topN = latentPCA.size();
		}
		
		for(int i=0;i<topN;i++)
		{
			ArrayList<Double> latentCurr = latentPCA.get(i);
			
			latentPCAVector.add(latentCurr.get(0));
		}
		
		return latentPCAVector;
	}
	
	/**
	 * Restituisce n_top fattori di latent 
	 * @param topN numero degli autovalori della latent
	 * @return valori degli n_top autovalori di latent
	 *
	 */
	public ArrayList<Double> getLatentSVDTopN(int topN) {
		String fileName = this.getAuthorID() + ".csv";
		
		ArrayList<ArrayList<Double>> latentSVD = IO.readTopNDocumentTermMatrixFromFile("../data/S_" + fileName,topN);
		ArrayList<Double> latentSVDVector = new ArrayList<Double>();
		
		if(topN > latentSVD.size())
		{
			topN = latentSVD.size();
		}
		
		for(int i=0;i<topN;i++)
		{
			ArrayList<Double> latentCurr = latentSVD.get(i);
			
			latentSVDVector.add(latentCurr.get(i));
		}
		
		return latentSVDVector;
	}
	
	// Phase 2 - Task 1b	
	
	/**
	 * Calcola la similarita' (coseno) tra l'autore corrente e un altro autore
	 * 
	 * @param otherAuthor
	 * @param corpus
	 * @return double similarita'
	 *
	 */
	public double getSimilarityOnKeywordVector(Author otherAuthor, Corpus corpus) {
		Double similarity = 0.0;
		
		TreeMap<String, Double> myKeywordVector = this.getWeightedTFIDFVector(corpus);
		TreeMap<String, Double> otherKeywordVector = otherAuthor.getWeightedTFIDFVector(corpus);
//		System.out.println("myKeywordVector: " + myKeywordVector);
//		System.out.println("otherKeywordVector: " + otherKeywordVector);
		
		similarity = Similarity.getCosineSimilarity(myKeywordVector, otherKeywordVector);
		
		return similarity;
	}
		
	/**
	 *  Calcola la similarita' (coseno) tra l'autore corrente e un altro autore
	 * 
	 * @param otherAuthor
	 * @param corpus
	 * @return
	 * @throws NoAuthorsWithSuchIDException 
	 * @throws AuthorWithoutPapersException 
	 *
	 */
	public double getSimilarityOnTFIDF2Vector(Author otherAuthor, Corpus corpus) throws AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		Double similarity = 0.0;
		
		TreeMap<String, Double> myTFIDF2Vector = this.getTFIDF2Vector(corpus);
		TreeMap<String, Double> otherTFIDF2Vector = otherAuthor.getTFIDF2Vector(corpus);
//		System.out.println("myTFIDF2Vector: " + myTFIDF2Vector);
//		System.out.println("otherTFIDF2Vector: " + otherTFIDF2Vector);
		
		similarity = Similarity.getCosineSimilarity(myTFIDF2Vector, otherTFIDF2Vector);	
		
		return similarity;
	}
	
	/**
	 * Calcola la similarita' (coseno) tra l'autore corrente e un altro autore
	 * 
	 * @param otherAuthor
	 * @param corpus
	 * @return
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public double getSimilarityOnPFVector(Author otherAuthor, Corpus corpus) throws NoAuthorsWithSuchIDException {
		Double similarity = 0.0;
		
		TreeMap<String, Double> myPFVector = this.getPFVector(corpus);
		TreeMap<String, Double> otherPFVector = otherAuthor.getPFVector(corpus);
//		System.out.println("myPFVector: " + myPFVector);
//		System.out.println("otherPFVector: " + otherPFVector);
		
		similarity = Similarity.getCosineSimilarity(myPFVector, otherPFVector);	
		
		return similarity;
	}
	
	
	/**
	 * Calcola la similarita' (coseno) tra l'autore corrente e un altro autore
	 * basandosi sul confronto delle matrici dei concetti
	 * (calcolate tramite SVD o PCA - a scelta tramite il parametro technique di tipo String).
	 * 
	 * @param otherAuthor
	 * @param corpus
	 * @param technique Tecnica a scelta tra "PCA" e "SVD"
	 * @return double similarita'
	 * @throws AuthorWithoutPapersException 
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException 
	 * @throws NoSuchTechniqueException 
	 *
	 */
	public double getSimilarityOnConceptsMatrix(Author otherAuthor, Corpus corpus, String technique) throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		Double similarity = 0.0;
		
		ArrayList<ArrayList<Double>> myMatrix;
		ArrayList<ArrayList<Double>> otherMatrix;
		ArrayList<TreeMap<String,Double>> myMatrixWithKeywords;
		ArrayList<TreeMap<String,Double>> otherMatrixWithKeywords;
		
		ArrayList<Double> myConceptsEigenValues;
		ArrayList<Double> otherConceptsEigenValues;
		String myFileName = this.getAuthorID().toString();
		String otherFileName = otherAuthor.getAuthorID().toString();
	
		switch(technique.toUpperCase()) {
			case "PCA":

				myMatrix = this.getPCA(corpus, myFileName, 5);
				otherMatrix = otherAuthor.getPCA(corpus, otherFileName, 5);
				
				myConceptsEigenValues = this.getLatentPCA();
				otherConceptsEigenValues = otherAuthor.getLatentPCA();
//				System.out.println("myConceptsEigenValues: " + myConceptsEigenValues);
//				System.out.println("otherConceptsEigenValues: " + otherConceptsEigenValues);
				break;
				
			case "SVD":
				myMatrix = this.getSVD(corpus, myFileName, 5);
				otherMatrix = otherAuthor.getSVD(corpus, otherFileName, 5);
				
				myConceptsEigenValues = this.getLatentSVD();
				otherConceptsEigenValues = otherAuthor.getLatentSVD();
//				System.out.println("myConceptsEigenValues: " + myConceptsEigenValues);
//				System.out.println("otherConceptsEigenValues: " + otherConceptsEigenValues);
				break;
				
			default:
				throw new NoSuchTechniqueException(technique + " is not a valid technique: try with PCA or SVD.");
		}
		
		myMatrixWithKeywords = this.getTopN(myMatrix, myMatrix.size());
		otherMatrixWithKeywords = otherAuthor.getTopN(otherMatrix, otherMatrix.size());
		myMatrixWithKeywords = this.getTopN(myMatrix, myMatrix.size());
		otherMatrixWithKeywords = otherAuthor.getTopN(otherMatrix, otherMatrix.size());
		
		// Normalizziamo i vettori di concetti per pesarne la varianza
		myConceptsEigenValues = Normalization.normalize(myConceptsEigenValues);
		otherConceptsEigenValues = Normalization.normalize(otherConceptsEigenValues);
//		System.out.println("myConceptsEigenValues normalizzato: " + myConceptsEigenValues);
//		System.out.println("otherConceptsEigenValues normalizzato: " + otherConceptsEigenValues);
		
		// valori relativi a massima e minima similarita' per righe
		double max_similarity = -1.0, min_similarity = 1.0;

		// Calcolo della media pesata (tramite gli autovalori) delle similarita'
		double denominatore = 0.0;
		for(int i = 0; i < myMatrixWithKeywords.size(); i++) {			
			TreeMap<String,Double> myCurrentConceptEigenVector = myMatrixWithKeywords.get(i);
			TreeMap<String,Double> otherCurrentConceptEigenVector = otherMatrixWithKeywords.get(i);
			
			TreeMap<String,Double> myCurrentConceptEigenVectorWeigthed = new TreeMap<String,Double>();
			TreeMap<String,Double> otherCurrentConceptEigenVectorWeigthed = new TreeMap<String,Double>();
			
//			System.out.println("Ciclo " + i);
//			System.out.println("myCurrentConceptEigenVector: " + myCurrentConceptEigenVector);
//			System.out.println("otherCurrentConceptEigenVector: " + otherCurrentConceptEigenVector);
			
			// Normalizziamo gli autovettori dei 2 autori
			myCurrentConceptEigenVector = Normalization.normalize(myCurrentConceptEigenVector);
			otherCurrentConceptEigenVector = Normalization.normalize(otherCurrentConceptEigenVector);			
			
//			System.out.println("myCurrentConceptEigenVector normalizzato: " + myCurrentConceptEigenVector);
//			System.out.println("otherCurrentConceptEigenVector normalizzato: " + otherCurrentConceptEigenVector);
					
			// Se ALMENO 1 degli autovalori e' 0 NON pesa i vettori e la similarity somma 0
			if(!(myConceptsEigenValues.get(i) == 0.0 || otherConceptsEigenValues.get(i) == 0.0)) {
				// Se almeno uno degli autovettori e' costituito da soli 0,
				// si salta il calcolo e si somma 0 per quella riga
				if(!(Normalization.isAllZeros(myCurrentConceptEigenVector) || Normalization.isAllZeros(otherCurrentConceptEigenVector))) {
					
					for(Map.Entry<String, Double> coeff : myCurrentConceptEigenVector.entrySet()) {
						myCurrentConceptEigenVectorWeigthed.put(coeff.getKey(),coeff.getValue() * myConceptsEigenValues.get(i));
					}										
					
					for(Map.Entry<String, Double> coeff : otherCurrentConceptEigenVector.entrySet()) {
						otherCurrentConceptEigenVectorWeigthed.put(coeff.getKey(),coeff.getValue() * otherConceptsEigenValues.get(i));
					}
					
//					System.out.println("myCurrentConceptEigenVectorWeigthed: " + myCurrentConceptEigenVectorWeigthed);
//					System.out.println("otherCurrentConceptEigenVectorWeigthed: " + otherCurrentConceptEigenVectorWeigthed);
					
					double currentSimilarity = Similarity.getCosineSimilarity(myCurrentConceptEigenVectorWeigthed, otherCurrentConceptEigenVectorWeigthed);
					similarity += currentSimilarity;
					
					//FIXME:
					//Calcolo dei max e min correnti
					if(currentSimilarity > max_similarity) {
						max_similarity = currentSimilarity;
					}
					if (currentSimilarity < min_similarity) {
						min_similarity = currentSimilarity;
					}
//					System.out.println("Massima similarita' = " + max_similarity + "; Minima similarita' = " + min_similarity);
					
					denominatore++;
//					System.out.println("Similarity = " + similarity + "\n");
				}
			}
		}
		
		// In questo modo si calcola la similarità basandosi sul confronto delle matrici dei concetti
		return similarity / denominatore;
		
		//FIXME:
		// In questo modo si calcola la similarita' complessiva usando il punto medio tra massima e minima
		//return (max_similarity + min_similarity) / 2;
	}
		
	/**
	 * Resituisce la classifica dei 10 autori per similarita' (coseno) rispetto all'autore corrente
	 * misurando le similarita' basandosi sulla PCA.
	 * 
	 * @param otherAuthor
	 * @param corpus
	 * @return La classifica dei 10 autori piu' simili
	 * @throws NoAuthorsWithSuchIDException 
	 * @throws AuthorWithoutPapersException 
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException 
	 * @throws NoSuchTechniqueException 
	 *
	 */
	public LinkedHashMap<String,Double> getSimilarAuthorsRankedByPCA(Corpus corpus) throws NoAuthorsWithSuchIDException, MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		LinkedHashMap<String,Double> top10 = new LinkedHashMap<String,Double>();
		TreeMap<String,Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		ArrayList<Author> authors = corpus.getAuthors();
		
		for(Author author : authors) {
			if(!author.equals(this)) {
				similarity = this.getSimilarityOnConceptsMatrix(author, corpus, "PCA");
				similarityVector.put(author.getName(), similarity);
			}
		}
		
		ArrayList<Map.Entry<String, Double>> authorsOrderedBySimilarity = Printer.orderVectorTreeMap(similarityVector);

		for(int i = 0; i < 10; i++) {
			top10.put(authorsOrderedBySimilarity.get(i).getKey(), authorsOrderedBySimilarity.get(i).getValue());
		}
		
		return top10;
	}
	
	/**
	 * Resituisce la classifica dei 10 autori per similarita' (coseno) rispetto all'autore corrente
	 * misurando le similarita' basandosi sulla SVD.
	 * 
	 * @param otherAuthor
	 * @param corpus
	 * @return La classifica dei 10 autori piu' simili
	 * @throws NoAuthorsWithSuchIDException 
	 * @throws AuthorWithoutPapersException 
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException 
	 * @throws NoSuchTechniqueException 
	 *
	 */
	public LinkedHashMap<String,Double> getSimilarAuthorsRankedBySVD(Corpus corpus) throws NoAuthorsWithSuchIDException, MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoSuchTechniqueException {
		
		LinkedHashMap<String,Double> top10 = new LinkedHashMap<String,Double>();
		TreeMap<String,Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		ArrayList<Author> authors = corpus.getAuthors();
		
		for(Author author : authors) {
			if(!author.equals(this)) {
				similarity = this.getSimilarityOnConceptsMatrix(author, corpus, "SVD");
				similarityVector.put(author.getName(), similarity);
			}
		}
		
		ArrayList<Map.Entry<String, Double>> authorsOrderedBySimilarity = Printer.orderVectorTreeMap(similarityVector);

		for(int i = 0; i < 10; i++) {
			top10.put(authorsOrderedBySimilarity.get(i).getKey(), authorsOrderedBySimilarity.get(i).getValue());
		}
		
		return top10;
	}
	
	/**
	 * Resituisce la classifica dei 10 autori per similarita' (coseno) rispetto all'autore corrente
	 * misurando le similarita' basandosi sul keyword vector.
	 * 
	 * @param otherAuthor
	 * @param corpus
	 * @return La classifica dei 10 autori piu' simili
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public LinkedHashMap<String,Double> getSimilarAuthorsRankedByKeywordVector(Corpus corpus) throws NoAuthorsWithSuchIDException {
		
		LinkedHashMap<String,Double> top10 = new LinkedHashMap<String,Double>();
		TreeMap<String,Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		ArrayList<Author> authors = corpus.getAuthors();
		
		for(Author author : authors) {
			if(!author.equals(this)) {
				similarity = this.getSimilarityOnKeywordVector(author, corpus);
				similarityVector.put(author.getName(), similarity);
			}
		}
		
		ArrayList<Map.Entry<String, Double>> authorsOrderedBySimilarity = Printer.orderVectorTreeMap(similarityVector);

		for(int i = 0; i < 10; i++) {
			top10.put(authorsOrderedBySimilarity.get(i).getKey(), authorsOrderedBySimilarity.get(i).getValue());
		}
		
		return top10;
	}
	
	/**
	 * Resituisce la classifica dei 10 autori per similarita' (coseno) rispetto all'autore corrente 
	 * misurando le similarita' basandosi sul PF vector.
	 *  
	 * @param otherAuthor
	 * @param corpus
	 * @return La classifica dei 10 autori piu' simili
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public LinkedHashMap<String,Double> getSimilarAuthorsRankedByPFVector(Corpus corpus) throws NoAuthorsWithSuchIDException {
		
		LinkedHashMap<String,Double> top10 = new LinkedHashMap<String,Double>();
		TreeMap<String,Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		ArrayList<Author> authors = corpus.getAuthors();
		
		for(Author author : authors) {
			if(!author.equals(this)) {
				similarity = this.getSimilarityOnPFVector(author, corpus);
				similarityVector.put(author.getName(), similarity);
			}
		}
		
		ArrayList<Map.Entry<String, Double>> authorsOrderedBySimilarity = Printer.orderVectorTreeMap(similarityVector);

		for(int i = 0; i < 10; i++) {
			top10.put(authorsOrderedBySimilarity.get(i).getKey(), authorsOrderedBySimilarity.get(i).getValue());
		}
		
		return top10;
	}
	
	/**
	 * Resituisce la classifica dei 10 autori per similarita' (coseno) rispetto all'autore corrente 
	 * misurando le similarita' basandosi sul TFIDF2 vector.
	 *  
	 * @param otherAuthor
	 * @param corpus
	 * @return La classifica dei 10 autori piu' simili
	 * @throws NoAuthorsWithSuchIDException 
	 * @throws AuthorWithoutPapersException 
	 *
	 */
	public LinkedHashMap<String,Double> getSimilarAuthorsRankedByTFIDF2Vector(Corpus corpus) throws NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		
		LinkedHashMap<String,Double> top10 = new LinkedHashMap<String,Double>();
		TreeMap<String,Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		ArrayList<Author> authors = corpus.getAuthors();
		
		for(Author author : authors) {
			if(!author.equals(this)) {
				similarity = this.getSimilarityOnTFIDF2Vector(author, corpus);
				similarityVector.put(author.getName(), similarity);
			}
		}
		
		ArrayList<Map.Entry<String, Double>> authorsOrderedBySimilarity = Printer.orderVectorTreeMap(similarityVector);

		for(int i = 0; i < 10; i++) {
			top10.put(authorsOrderedBySimilarity.get(i).getKey(), authorsOrderedBySimilarity.get(i).getValue());
		}
		
		return top10;
	}
	
	// Phase 2 - Task 1c
	
	public ArrayList<Author> getOtherAuthors(Corpus corpus) throws NoAuthorsWithSuchIDException {
		if(otherAuthors == null) {
			otherAuthors = calculateOtherAuthors(corpus);
		}
		return otherAuthors;
	}

	/**
	 * Estrae gli autori presenti nel corpus, diversi da se' stesso.
	 * 
	 * @param corpus il corpus di riferimento
	 * @return List<Author> autori presenti nel corpus, diversi da se' stesso
	 * 
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	public ArrayList<Author> calculateOtherAuthors(Corpus corpus) throws NoAuthorsWithSuchIDException {		
		// creo una nuova lista
		ArrayList<Author> otherAuthors = new ArrayList<Author>(corpus.getAuthors());

		otherAuthors.remove(this);

		return otherAuthors;
	}
		
	public ArrayList<Integer> getOtherAuthorsIDs(Corpus corpus) throws NoAuthorsWithSuchIDException {
		if(otherAuthorsIDs == null) {
			otherAuthorsIDs = calculateOtherAuthorsIDs(corpus);
		}
		return otherAuthorsIDs;
	}
	
	/**
	 * Estrae gli ID degli autori presenti nel corpus, diversi da se' stesso.
	 * 
	 * @param corpus il corpus di riferimento
	 * @throws NoAuthorsWithSuchIDException 
	 * 
	 * @return List<Integer> ID degli autori presenti nel corpus, diversi da se' stesso
	 *
	 */
	private ArrayList<Integer> calculateOtherAuthorsIDs(Corpus corpus) throws NoAuthorsWithSuchIDException {		
		ArrayList<Integer> otherAuthorsIDs = new ArrayList<Integer>();
		
		ArrayList<Author> otherAuthors = this.getOtherAuthors(corpus);
		
		for (Author a : otherAuthors) {
			otherAuthorsIDs.add(a.getAuthorID());
		}
		return otherAuthorsIDs;
	}
	
	public ArrayList<Paper> getOtherAuthorsPapers(Corpus corpus) throws NoAuthorsWithSuchIDException {
		if (otherAuthorsPapers == null) {
			otherAuthorsPapers = calculateOtherAuthorsPapers(corpus);
		}
		return otherAuthorsPapers;
	}
		
	/**
	 * Estrae i paper degli autori diversi dall' autore dato.
	 * 
	 * @param corpus il corpus di riferimento
	 * @return lista di Paper: elenco dei paper degli autori diversi da un autore dato
	 * @throws NoAuthorsWithSuchIDException 
	 *
	 */
	private ArrayList<Paper> calculateOtherAuthorsPapers(Corpus corpus) throws NoAuthorsWithSuchIDException {
		ArrayList<Paper> otherAuthorsPapers = new ArrayList<Paper>();
		ArrayList<Paper> myPapers = this.getPapers();
		
		ArrayList<Author> otherAuthors = this.getOtherAuthors(corpus);

		for (Author a : otherAuthors) {
			List<Paper> authorsPapers = a.getPapers();
			for (Paper p : authorsPapers) {
				// Aggiungo un paper solo se non c'è già, e se non è tra i paper dell'autore dato
				if (!(otherAuthorsPapers.contains(p) || myPapers.contains(p))) {
					otherAuthorsPapers.add(p);
				}
			}
		}

		return otherAuthorsPapers;
	}
	
	//Task 2a
	
	/**
	 * Restituisce in ordine di rilevanza gli articoli piu’ rilevanti (di cui lui non e’ autore), basandosi 
	 * sulla similarita’ tra i seguenti vettori relativi all’autore dato basandosi sul keyword vector.
	 * @param corpus
	 * @return La classifica degli articoli piu’rilevanti
	 *
	 */
	public LinkedHashMap<String,Double> getRelevantPapersRankedByKeywordVector(Corpus corpus) throws NoAuthorsWithSuchIDException {
		
		LinkedHashMap<String,Double> topRelevantPapers = new LinkedHashMap<String,Double>();
		TreeMap<String, Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		
		TreeMap<String, Double> myKeywordVector = this.getWeightedTFIDFVector(corpus);
		List<Paper> otherAuthorsPapers = this.getOtherAuthorsPapers(corpus);
		
		for(Paper paper : otherAuthorsPapers)
		{
//			System.out.println("keyword vector di " + this.getName() + ": " + myKeywordVector);
//			System.out.println("TFIDF vector del paper " + paper.getPaperID() + ": " + paper.getTFIDFVector(corpus));
			similarity = Similarity.getCosineSimilarity(myKeywordVector, paper.getTFIDFVector(corpus));
			similarityVector.put(paper.getTitle(), similarity);
		}
		
		ArrayList<Map.Entry<String, Double>> papersOrderedByRelevance = Printer.orderVectorTreeMap(similarityVector);

		int topRank = 10;
		if(papersOrderedByRelevance.size() < topRank)
		{
			topRank = papersOrderedByRelevance.size();
		}
		
		for(int i = 0; i < topRank; i++) {
			topRelevantPapers.put(papersOrderedByRelevance.get(i).getKey(), papersOrderedByRelevance.get(i).getValue());
		}
		
		return topRelevantPapers;
	}
	
	/**
	 * Restituisce in ordine di rilevanza gli articoli piu’rilevanti (di cui lui non e’ autore), basandosi 
	 * sulla similarita’ tra i seguenti vettori relativi all’autore dato basandosi sul TFIDF2.
	 * @param corpus
	 * @return La classifica degli articoli piu’rilevanti
	 * @throws AuthorWithoutPapersException 
	 *
	 */
	public LinkedHashMap<String,Double> getRelevantPapersRankedByTFIDF2Vector(Corpus corpus) throws NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		
		LinkedHashMap<String,Double> topRelevantPapers = new LinkedHashMap<String,Double>();
		TreeMap<String, Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		
		TreeMap<String, Double> myTFIDF2Vector = this.getTFIDF2Vector(corpus);	
		List<Paper> otherAuthorsPapers = this.getOtherAuthorsPapers(corpus);
		
		for(Paper paper : otherAuthorsPapers)
		{
			similarity = Similarity.getCosineSimilarity(myTFIDF2Vector, paper.getTFIDFVector(corpus));
			similarityVector.put(paper.getTitle(), similarity);
		}
		
		ArrayList<Map.Entry<String, Double>> papersOrderedByRelevance = Printer.orderVectorTreeMap(similarityVector);

		int topRank = 10;
		if(papersOrderedByRelevance.size() < topRank)
		{
			topRank = papersOrderedByRelevance.size();
		}
		
		for(int i = 0; i < topRank; i++) {
			topRelevantPapers.put(papersOrderedByRelevance.get(i).getKey(), papersOrderedByRelevance.get(i).getValue());
		}
		
		return topRelevantPapers;
	}
	
	/**
	 * Restituisce in ordine di rilevanza gli articoli piu’rilevanti (di cui lui non e’ autore), basandosi 
	 * sulla similarita’ tra i seguenti vettori relativi all’autore dato basandosi sul PF.
	 * @param corpus
	 * @return La classifica degli articoli piu’rilevanti
	 * @throws AuthorWithoutPapersException 
	 *
	 */
	public LinkedHashMap<String,Double> getRelevantPapersRankedByPFVector(Corpus corpus) throws NoAuthorsWithSuchIDException, AuthorWithoutPapersException {
		
		LinkedHashMap<String,Double> topRelevantPapers = new LinkedHashMap<String,Double>();
		TreeMap<String, Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		
		TreeMap<String, Double> myPFVector = this.getPFVector(corpus);
		
		List<Paper> otherAuthorsPapers = this.getOtherAuthorsPapers(corpus);
		
		for(Paper paper : otherAuthorsPapers)
		{
			similarity = Similarity.getCosineSimilarity(myPFVector, paper.getTFIDFVector(corpus));
			similarityVector.put(paper.getTitle(), similarity);
		}
		
		ArrayList<Map.Entry<String, Double>> papersOrderedByRelevance = Printer.orderVectorTreeMap(similarityVector);

		int topRank = 10;
		if(papersOrderedByRelevance.size() < topRank)
		{
			topRank = papersOrderedByRelevance.size();
		}
		
		for(int i = 0; i < topRank; i++) {
			topRelevantPapers.put(papersOrderedByRelevance.get(i).getKey(), papersOrderedByRelevance.get(i).getValue());
		}
		
		return topRelevantPapers;
	}
	
	/**
	 * Restituisce in ordine di rilevanza gli articoli piu’rilevanti (di cui lui non e’ autore), basandosi 
	 * sulla similarita’ tra i seguenti vettori relativi all’autore dato basandosi sulla PCA.
	 * @param corpus
	 * @return La classifica degli articoli piu’rilevanti
	 * @throws AuthorWithoutPapersException 
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException 
	 *
	 */
	public LinkedHashMap<String,Double> getRelevantPapersRankedByPCA(Corpus corpus) throws NoAuthorsWithSuchIDException, AuthorWithoutPapersException, MatlabConnectionException, MatlabInvocationException {
		
		LinkedHashMap<String,Double> topRelevantPapers = new LinkedHashMap<String,Double>();
		TreeMap<String, Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		
		String fileName = this.getAuthorID().toString();
		ArrayList<ArrayList<Double>> myPCAMatrix = this.getPCA(corpus, fileName, 5);
		ArrayList<TreeMap<String, Double>> myPCAMatrixwithKey = this.getTopN(myPCAMatrix, myPCAMatrix.size());
		List<Paper> otherAuthorsPapers = this.getOtherAuthorsPapers(corpus);
		
		for(Paper paper : otherAuthorsPapers)
		{
			for(int i=0; i < myPCAMatrixwithKey.size(); i++)
			{			
				similarity += Similarity.getCosineSimilarity(myPCAMatrixwithKey.get(i), paper.getTFIDFVector(corpus));
			}
			similarityVector.put(paper.getTitle(), similarity);
		}
		
		ArrayList<Map.Entry<String, Double>> papersOrderedByRelevance = Printer.orderVectorTreeMap(similarityVector);

		int topRank = 10;
		if(papersOrderedByRelevance.size() < topRank)
		{
			topRank = papersOrderedByRelevance.size();
		}
		
		for(int i = 0; i < topRank; i++) {
			topRelevantPapers.put(papersOrderedByRelevance.get(i).getKey(), papersOrderedByRelevance.get(i).getValue());
		}
		
		return topRelevantPapers;
	}
	
	/**
	 * Restituisce in ordine di rilevanza gli articoli piu’rilevanti (di cui lui non e’ autore), basandosi 
	 * sulla similarita’ tra i seguenti vettori relativi all’autore dato basandosi sulla PCA.
	 * @param corpus
	 * @return La classifica degli articoli piu’rilevanti
	 * @throws AuthorWithoutPapersException 
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException 
	 *
	 */
	public LinkedHashMap<String,Double> getRelevantPapersRankedBySVD(Corpus corpus) throws NoAuthorsWithSuchIDException, AuthorWithoutPapersException, MatlabConnectionException, MatlabInvocationException {
		
		LinkedHashMap<String,Double> topRelevantPapers = new LinkedHashMap<String,Double>();
		TreeMap<String, Double> similarityVector = new TreeMap<String,Double>();
		Double similarity = 0.0;
		String fileName = this.getAuthorID().toString();
		
		ArrayList<ArrayList<Double>> mySVDMatrix = this.getSVD(corpus, fileName, 5);
		ArrayList<TreeMap<String, Double>> mySVDMatrixwithKey = this.getTopN(mySVDMatrix, mySVDMatrix.size());
		List<Paper> otherAuthorsPapers = this.getOtherAuthorsPapers(corpus);
		
		for(Paper paper : otherAuthorsPapers) 
		{
			for(int i=0; i < mySVDMatrixwithKey.size(); i++)
			{			
				similarity += Similarity.getCosineSimilarity(mySVDMatrixwithKey.get(i), paper.getTFIDFVector(corpus));
			}
			similarityVector.put(paper.getTitle(), similarity);
		}
		
		ArrayList<Map.Entry<String, Double>> papersOrderedByRelevance = Printer.orderVectorTreeMap(similarityVector);

		int topRank = 10;
		if(papersOrderedByRelevance.size() < topRank)
		{
			topRank = papersOrderedByRelevance.size();
		}
		
		for(int i = 0; i < topRank; i++) {
			topRelevantPapers.put(papersOrderedByRelevance.get(i).getKey(), papersOrderedByRelevance.get(i).getValue());
		}
		
		return topRelevantPapers;
	}
		
	// Phase 2 - Task 2b
	
	/**
	 * Verifica se l'autore ha coautori.
	 * 
	 * @return true se l'autore ha coautori, false altrimenti
	 */
	public boolean hasCoAuthors() {
		return this.getCoAuthorsIDs().size() == 0;
	}
	
	public Integer getAuthorID() {
		return authorID;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Author [personID=" + authorID + ", name=" + name + ", papers="
				+ papers + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + authorID;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		if (authorID != other.authorID)
			return false;
		return true;
	}

}
