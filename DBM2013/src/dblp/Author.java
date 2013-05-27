package dblp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.IO;
import utils.MatlabEngine;
import utils.Normalization;

public class Author {
	// FIXME
	final static int titleWeight = 3;
	private int authorID;
	private String name;
	private ArrayList<Paper> papers;

	public Author(int personID, String name, ArrayList<Paper> papers) {
		super();
		this.authorID = personID;
		this.name = name;
		this.papers = papers;
	}

	/**
	 * Estrae i coautori di un autore dato.
	 * 
	 * @param a
	 * @return lista di Author: coautori di un autore dato
	 * @throws Exception
	 */
	// FIXME: sostituire con exception appropriata
	public List<Author> getCoAuthors(Corpus corpus) throws Exception {
		List<Author> coAuthors = new ArrayList<Author>();
		List<Integer> coAuthorsIDs = this.getCoAuthorsIDs();

		for (int coA : coAuthorsIDs) {
			coAuthors.add(corpus.getAuthorByID(coA));
		}

		return coAuthors;
	}

	/**
	 * Estrae i coautori di un autore dato, insieme all'autore stesso.
	 * 
	 * @param a
	 * @return lista di Author: coautori di un autore dato + autore stesso
	 * @throws Exception
	 */
	// FIXME: sostituire con exception appropriata
	public List<Author> getCoAuthorsAndSelf(Corpus corpus) throws Exception {
		List<Author> coAuthorsAndSelf = this.getCoAuthors(corpus);
		coAuthorsAndSelf.add(this);
		return coAuthorsAndSelf;
	}

	/**
	 * Considera tutti gli articoli scritti da un certo autore per creare un
	 * combined keyword vector dei tf per quellautore. Nei combined keyword
	 * vector, gli articoli piu recenti devono pesare di piu di quelli piu
	 * vecchi.
	 * 
	 * @return treemap dei tf di un autore, pesando gli articoli per eta'
	 * @throws IOException
	 */
	public Map<String, Double> getWeightedTFVector() throws IOException {
		Map<String, Double> wtfv;
		double weight = 0;
		// double weightNormalizationFactor = 0;
		TreeMap<String, Double> weightedTFVector = new TreeMap<String, Double>();
		// FIXME controllare - aggiustare il metodo

		/*
		 * - vettore di tf per ogni paper dell'autore - età del paper - peso
		 * usando l'età - tfrkey[r] = sum(peso[i]*tfkey[i])/sum(peso[i]);
		 */

		for (Paper p : papers) {
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

		Map<String, Double> normalizedWeightedTFVector = Normalization.normalizeTreeMap(weightedTFVector);
		return normalizedWeightedTFVector;

	}

	/**
	 * Considera tutti gli articoli scritti da un certo autore per creare un
	 * combined keyword vector dei tfidf per quellautore. Nei combined
	 * keyword vector, gli articoli piu recenti devono pesare di piu di
	 * quelli piu vecchi.
	 * 
	 * @return treemap dei tfidf di un autore, pesando gli articoli per eta'
	 * @throws Exception
	 */
	// FIXME: sistemare l'eccezione
	public Map<String, Double> getWeightedTFIDFVector(Corpus corpus) throws Exception {
		TreeMap<String, Double> weightedTFIDFVector = new TreeMap<String, Double>();

		Map<String, Double> wtfidfv;
		double weight = 0;

		/*
		 * - vettore di tfidf per ogni paper dell'autore - età del paper - peso
		 * usando l'età - tfidfrkey[r] = sum(peso[i]*tfidfkey[i])/sum(peso[i]);
		 */

		for (Paper p : papers) {
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

		TreeMap<String, Double> normalizedWeightedTFIDFVector = Normalization.normalizeTreeMap(weightedTFIDFVector);

		return normalizedWeightedTFIDFVector;
	}

	/**
	 * Estrae l'insieme delle keyword presenti in tutti gli articoli
	 * dell'autore, con il rispettivo numero di occorrenze.
	 * 
	 * @return hashmap delle keyword in tutti gli articoli dell'autore, con
	 *         occorrenze
	 */
	public HashMap<String, Integer> getCombinedKeywordSet() {
		HashMap<String, Integer> combinedKeywordSet = new HashMap<String, Integer>();
		HashMap<String, Integer> keywordSet = new HashMap<String, Integer>();

		for (Paper p : papers) {
			keywordSet = p.getKeywordSetWithOccurrences();
			// System.out.println(keywordSet);
			for (Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				if (!combinedKeywordSet.containsKey(k)) {
					combinedKeywordSet.put(k.getKey(), k.getValue());
				} else {
					combinedKeywordSet.put(k.getKey(), combinedKeywordSet.get(k) + k.getValue());
				}
			}
		}
		return combinedKeywordSet;
	}

	/**
	 * Estrae i nomi dei coautori dell'autore corrente.
	 * 
	 * @return lista di stringhe: nomi dei coautori dell'autore corrente
	 */
	public List<String> getCoAuthorsNames() {
		List<String> coAuthorsNames = new ArrayList<String>();

		for (Paper p : this.getPapers()) {
			for (String coA : p.getAuthorsNames()) {
				if (!coAuthorsNames.contains(coA) && !this.getName().equals(coA)) {
					coAuthorsNames.add(coA);
				}
			}
		}
		return coAuthorsNames;
	}

	/**
	 * Estrae gli id dei coautori dell'autore corrente.
	 * 
	 * @return lista di interi: id dei coautori dell'autore corrente
	 */
	public List<Integer> getCoAuthorsIDs() {
		List<Integer> coAuthorsIDs = new ArrayList<Integer>();

		for (Paper p : this.getPapers()) {
			for (int coA : p.getAuthors()) {
				if (!coAuthorsIDs.contains(coA) && this.getAuthorID() != (coA)) {
					coAuthorsIDs.add(coA);
				}
			}
		}
		return coAuthorsIDs;
	}

	/**
	 * Calcola il tf della keyword basandosi sull'insieme di tutti gli articoli
	 * scritti dall' autore dato.
	 * 
	 * @param keyword
	 * @return tf della keyword basandosi sugli articoli dell'autore
	 */
	public double getRestrictedTF(String keyword) {
		double tf = 0.0;
		List<Paper> rc = this.getPapers();
		// conta il numero di occorrenze della keyword nei papers di author
		int n = 0;
		int K = 0;
		for (Paper p : rc) {
			HashMap<String, Integer> keywordSet = p
					.getKeywordSetWithOccurrences();

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
	 * @param author
	 * @param authors
	 * @return IDF calcolato in base al corpus ristretto
	 * @throws Exception
	 */
	public double getRestrictedIDF(String keyword, Corpus corpus)
			throws Exception {
		double idf = 0;
		int m = 0; // conta il numero di articoli in cui la keyword compare

		List<Author> coAuthorsAndSelf = this.getCoAuthorsAndSelf(corpus);
		List<Paper> restrictedCorpus = new ArrayList<Paper>();

		/*
		 * estrae il corpus ristretto papers di autore+relativi coautori)
		 */
		for (Author a : coAuthorsAndSelf) {
			for (Paper p : a.getPapers()) {
				if (!restrictedCorpus.contains(p)) {
					restrictedCorpus.add(p);
				}
			}
		}
		int N = restrictedCorpus.size();

		for (Paper p : restrictedCorpus) {
			HashMap<String, Integer> keywordSet = p
					.getKeywordSetWithOccurrences();
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
	 * @param author
	 * @return double tfidf2
	 * @throws Exception
	 */
	public double getTFIDF2(String keyword, Corpus corpus) throws Exception {
		double tfidf2 = 0.0;
		double tf2 = this.getRestrictedTF(keyword);
		double idf2 = this.getRestrictedIDF(keyword, corpus);

		tfidf2 = tf2 * idf2;

		return tfidf2;
	}

	/**
	 * Restituisce il keyword vector sotto forma di sequenza di coppie
	 * <keyword,weight> rispetto al modello di pesi TFIDF2, che per il calcolo
	 * del tf considera l'insieme di tutti gli articoli scritti dall'autore dato
	 * e per il calcolo dell'idf considera l'insieme degli articoli scritti
	 * dall'autore e dai suoi coautori.
	 * 
	 * @param c
	 * @return keywordVector pesato in base al modello TFIDF2
	 * @throws Exception
	 */
	// FIXME
	public TreeMap<String, Double> getTFIDF2Vector(Corpus corpus) throws Exception {
		TreeMap<String, Double> TFIDF2Vector = new TreeMap<String, Double>();

		List<Paper> papers = this.getPapers();
		for (Paper p : papers) {
			HashMap<String, Integer> keywordSet = p
					.getKeywordSetWithOccurrences();
			double tfidf2;
			String key;
			for (Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				key = k.getKey();
				tfidf2 = this.getTFIDF2(key, corpus);
				TFIDF2Vector.put(key, tfidf2);
			}
		}

		TreeMap<String, Double> normalizedTFIDF2Vector = Normalization
				.normalizeTreeMap(TFIDF2Vector);

		return normalizedTFIDF2Vector;

	}

	/**
	 * Estrae gli articoli dei coautori di un autore dato.
	 * 
	 * @param a
	 * @return lista di Paper: elenco dei paper dei coautori di un autore dato
	 * @throws Exception
	 */
	// FIXME: sostituire con exception appropriata
	public List<Paper> getCoAuthorsPapers(Corpus corpus) throws Exception {
		List<Author> coAuthors = this.getCoAuthors(corpus);
		List<Paper> coAuthorsPapers = new ArrayList<Paper>();

		List<Paper> authorsPapers = new ArrayList<Paper>();
		for (Author coA : coAuthors) {
			authorsPapers = coA.getPapers();
			for (Paper p : authorsPapers) {
				if (!coAuthorsPapers.contains(p)) {
					coAuthorsPapers.add(p);
				}
			}
		}

		return coAuthorsPapers;
	}

	/**
	 * Estrae gli articoli dei coautori di un autore dato, insieme a quelli
	 * dell'autore stesso.
	 * 
	 * @param a
	 * @return lista di Paper: elenco degli articoli di un autore dato insieme a
	 *         quelli dell'autore stesso
	 * @throws Exception
	 */
	// FIXME: sostituire con exception appropriata
	public List<Paper> getCoAuthorsAndSelfPapers(Corpus corpus)
			throws Exception {

		List<Paper> coAuthorsAndSelfPapers = this.getCoAuthorsPapers(corpus);
		List<Paper> selfPapers = this.getPapers();

		for (Paper p : selfPapers) {
			if (!coAuthorsAndSelfPapers.contains(p)) {
				coAuthorsAndSelfPapers.add(p);
			}
		}

		return coAuthorsAndSelfPapers;
	}

	/**
	 * Restituisce l'insieme delle keyword relativo a tutti i paper dell'autore,
	 * ordinato lessicograficamente.
	 * 
	 * @return arraylist delle keyword dell'autore, ordinato
	 */
	public ArrayList<String> getKeywordSet() {
		ArrayList<String> keywordSet = new ArrayList<String>();

		ArrayList<String> ks = new ArrayList<String>();
		for (Paper p : papers) {
			ks = p.getKeywordSet();
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
	 * @param a_i
	 * @param k_j
	 * @return numero di articoli dei soli coautori dell'autore a_i che non
	 *         contengono la chiave k_j
	 * @throws Exception
	 */
	// FIXME: sostituire con exception appropriata
	public int r_withoutKey(String k_j, Corpus corpus) throws Exception {
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
	 * @param a_i
	 * @param k_j
	 * @return numero intero: gli articoli dell'autore a_i e dei suoi coautori
	 *         che non contengono la chiave k_j
	 * @throws Exception
	 */
	public int n_withoutKey(String k_j, Corpus corpus) throws Exception {
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
	 * @param a_i
	 * @param k_j
	 * @return peso u_ij della keyword k_j per l'autore a_i
	 * @throws Exception
	 */
	// FIXME: sostituire con eccezione appropriata...
	public double getU_ij(String k_j, Corpus corpus) throws Exception {
		double u_ij = 0.0;
		double epsilon = 0.1; // costante che non fa andare a zero
		double numLog = 0.0;
		double denLog = 0.0;
		double resLog = 0.0;
		double resAbs = 0.0;

		int r_ij = this.r_withoutKey(k_j, corpus);
		int n_ij = this.n_withoutKey(k_j, corpus);
		int R_i = this.getCoAuthorsPapers(corpus).size();
		int N_i = this.getCoAuthorsAndSelfPapers(corpus).size();

		numLog = (double) (r_ij) / (R_i - r_ij + epsilon);
		denLog = (double) (n_ij - r_ij + epsilon)
				/ (N_i - n_ij - R_i + r_ij + epsilon);
		// FIXME: aggiunto 1 + ... all'argomento del logaritmo (come visto il 2
		// maggio a lezione)
		resLog = Math.log(1 + (numLog / denLog));

		resAbs = Math.abs((double) (r_ij + epsilon / R_i + epsilon)
				- (double) ((n_ij - r_ij + epsilon) / (N_i - R_i + epsilon)));

		u_ij = resLog * resAbs;

		return u_ij;
	}

	/**
	 * Restituisce il keyword vector sotto forma di sequenza di coppie
	 * <keyword,weight> rispetto al modello di pesi PF, che per il calcolo dei
	 * pesi considera l'insieme di tutti gli articoli scritti dall'autore dato e
	 * l'insieme degli articoli scritti dall'autore e dai suoi coautori.
	 * Utilizza il meccanismo di feedback probabilistico (PF).
	 * 
	 * @param c
	 * @return keywordVector pesato in base al modello PF
	 * @throws Exception
	 */
	// FIXME
	public TreeMap<String, Double> getPFVector(Corpus corpus) throws Exception {
		TreeMap<String, Double> PFVector = new TreeMap<String, Double>();

		List<Paper> papers = this.getPapers();
		for (Paper p : papers) {
			HashMap<String, Integer> keywordSet = p
					.getKeywordSetWithOccurrences();
			double pf;
			String key;
			for (Map.Entry<String, Integer> k : keywordSet.entrySet()) {
				key = k.getKey();
				pf = this.getU_ij(key, corpus);
				PFVector.put(key, pf);
			}
		}

		TreeMap<String, Double> normalizedPFVector = Normalization
				.normalizeTreeMap(PFVector);

		return normalizedPFVector;
	}

	/**
	 * Restituisce la matrice document-term relativa all'autore selezionato
	 * 
	 * @param a autore
	 * @return ArrayList<TreeMap<String, Double>> matrice document-term
	 * @throws Exception
	 */
	public ArrayList<TreeMap<String, Double>> getDocumentTermMatrix(Corpus corpus) throws Exception {
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
		Map<String, Double> currentWeightedTFIDFVector = new HashMap<String, Double>();
		for (int doc = 0; doc < m; doc++) {
			// recupera il paper corrente...
			Paper currentPaper = documents.get(doc);
			// System.out.println("Paper: \'" + currentPaper.getTitle() +
			// "\', peso: " + currentPaper.getWeightBasedOnAge());
			// ... e ne calcola il vettore di tfidf pesato
			currentWeightedTFIDFVector = currentPaper.getWeightedTFIDFVector(
					currentPaper.getWeightBasedOnAge(), corpus);
			// System.out.println("WeightedTFIDFVector: \'" +
			// currentWeightedTFIDFVector);
			// modifica la riga relativa al documento corrente, sostituendo gli
			// zeri con i valori
			TreeMap<String, Double> row = documentTermMatrix.get(doc);
			for (Map.Entry<String, Double> entry : currentWeightedTFIDFVector
					.entrySet()) {
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
	 * Calcola la matrice SVD per l'autore corrente
	 * @param documentTermMatrix
	 * @return matrice SVD per l'autore corrente
	 * @throws Exception
	 */
	public ArrayList<ArrayList<Double>> getSVD(Corpus corpus) throws Exception {
		String startingDirectory = System.getProperty("user.dir");
		String ioDirectory = startingDirectory + "/../data/";
		String fileName = this.getAuthorID() + ".csv";
		File csvFile = new File(ioDirectory + fileName);
		MatlabEngine me = new MatlabEngine();
		me.init();		
		if (!csvFile.isFile()) {
			ArrayList<TreeMap<String, Double>> documentTermMatrix = this.getDocumentTermMatrix(corpus);
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, ioDirectory + fileName);
		}
		me.eval("svd_IR", fileName, startingDirectory);
		ArrayList<ArrayList<Double>> svd = IO.readDocumentTermMatrixFromFile(ioDirectory + "/V_" + fileName);
		return svd;
	}

	/**
	 * Calcola la matrice PCA per l'autore corrente
	 * @param documentTermMatrix
	 * @return matrice PCA per l'autore corrente
	 * @throws Exception
	 */
	public ArrayList<ArrayList<Double>> getPCA(Corpus corpus) throws Exception {
		String startingDirectory = System.getProperty("user.dir") + "/ext-matlab/";
		String ioDirectory = startingDirectory + "/../data/";
		String fileName = this.getAuthorID() + ".csv";
		File csvFile = new File("../data/" + fileName);
		MatlabEngine me = new MatlabEngine();
		me.init();
		if (!csvFile.isFile()) {
			ArrayList<TreeMap<String, Double>> documentTermMatrix = this.getDocumentTermMatrix(corpus);
			IO.printDocumentTermMatrixOnFile(documentTermMatrix, "../data/"	+ fileName);
		}
		me.eval("pca_IR", ioDirectory, fileName);
		//FIXME: controllare che la matrice corretta sia "score_..."
		ArrayList<ArrayList<Double>> pca = IO.readDocumentTermMatrixFromFile("../data/score_" + fileName);
		return pca;
	}
	public int getAuthorID() {
		return authorID;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}

	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPapers(ArrayList<Paper> papers) {
		this.papers = papers;
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
