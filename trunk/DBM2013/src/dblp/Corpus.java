package dblp;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.naming.NameNotFoundException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.EdgeIterable;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.PageRank;
import org.openide.util.Lookup;

import utils.GraphEngine;
import utils.IO;
import utils.MapUtils;
import utils.MatlabEngine;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;
import exceptions.NoAuthorsWithSuchNameException;
import exceptions.NoPaperWithSuchIDException;
import exceptions.WrongClusteringException;
import exceptions.WrongFileTypeException;

public class Corpus {

	private ArrayList<Author> authors;
	private ArrayList<Paper> papers;
	private int cardinality;
	private ArrayList<String> globalKeywordSet;
	
	public Corpus(ArrayList<Author> authors, ArrayList<Paper> papers, int cardinality) {
		super();
		this.authors = authors;
		this.papers = papers;
		this.cardinality = cardinality;
	}
	
	/**
	 * Restituisce l'oggetto Paper associato all'id dato.
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
	
	public TreeMap<Integer, ArrayList<Author>> getClustersBasedOnConcepts(String path, String fileName) throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoAuthorsWithSuchIDException, WrongClusteringException {
		TreeMap<Integer, ArrayList<Author>> clustersBasedOnConcepts = new TreeMap<Integer, ArrayList<Author>>();
		
        this.getTop3SVDAuthor(path, fileName);  // sfruttiamo il side-effect del fatto che stampi su file!
        
		String readFileName = "U_" + fileName;
		
		// estraggo la lista degli id degli autori
		ArrayList<Author> authors = this.getAuthors();
		ArrayList<Integer> authorsIDs = new ArrayList<Integer>();
		for(Author a : authors) {
			authorsIDs.add(a.getAuthorID());
		}
		// System.out.println(authorsIDs);
		
		// Leggiamo il file corrispondente alla matrice U_SimilarityMatrixAuthor.csv
		Table<Integer, Integer, Double> top3ReadTable = IO.readTop3SVDMatrixAuthorFromFile(path, readFileName, authorsIDs);
		
		// FIXME: rimuovere metodi di stampa (debug)
//		System.out.println("stampa matrice top3Read");
//		Printer.printTop3SVDMatrixAuthorWithCaptions(top3ReadTable);
//		System.out.println("-------------------------------------------------------------\n");
		
		Double max = 0.0;
		Double current = 0.0;
		Integer maxCol = 0;
		Integer maxRow = 0;
		ArrayList<Author> cluster0 = new ArrayList<Author>();
		ArrayList<Author> cluster1 = new ArrayList<Author>();
		ArrayList<Author> cluster2 = new ArrayList<Author>();
		
//		System.out.println("colonne: " + top3ReadTable.columnKeySet());
//		System.out.println("righe: " + top3ReadTable.rowKeySet());
		
		for (int i : top3ReadTable.columnKeySet()) {
			max = 0.0 - Double.MAX_VALUE;
			maxCol = 0;
			maxRow = 0;
			for (int j : top3ReadTable.rowKeySet()) {				
				current = top3ReadTable.get(j, i);
//				System.out.println("i: " + i + ", j: " + j);
//				System.out.println("Current: " + current);
				if (max < current) {
					max = current;
					maxCol = i;
					maxRow = j;
				}
//				System.out.println("max: " + max + ", maxCol: " + maxCol + ", maxRow: " + maxRow);
			}
			switch (maxRow) {
				case 0:
					cluster0.add(this.getAuthorByID(maxCol));
					break;
				case 1:
					cluster1.add(this.getAuthorByID(maxCol));
					break;
				case 2:
					cluster2.add(this.getAuthorByID(maxCol));
					break;
				default:
					throw new WrongClusteringException("Il valore non appartiene a nessun cluster!");
			}
		}
		
		clustersBasedOnConcepts.put(0, cluster0);
		clustersBasedOnConcepts.put(1, cluster1);
		clustersBasedOnConcepts.put(2, cluster2);
		
		return clustersBasedOnConcepts;
	}
	
	// Phase 2 - Task 3b
	
	public ArrayList<String> getGlobalKeywordSet() {
		if(globalKeywordSet == null) {
			globalKeywordSet = calculateGlobalKeywordSet();
		}
		return globalKeywordSet;
	}
	
	/**
	 * Calcola il keyword set globale, contenente l'insieme di
	 * tutte le keyword presenti nel DB;
	 * viene ordinato lessicograficamente.
	 * 
	 * @return ArrayList<String> keyword set globale
	 */
	private ArrayList<String> calculateGlobalKeywordSet() {
		ArrayList<String> globalKeywordSet = new ArrayList<String>();
		
		ArrayList<Paper> papersList = this.getPapers();
		
		for (Paper paper : papersList) {
			ArrayList<String> keywordSet = paper.getKeywordSet();
			for (String keyword : keywordSet) {
				if (!globalKeywordSet.contains(keyword)) {
					globalKeywordSet.add(keyword);
				}
			}
		}
		
		Collections.sort(globalKeywordSet);
		
		return globalKeywordSet;		
	}
	
	
	/**
	 * Associa un keyword vector a ciascun concetto sulla base dei keyword vector
	 * degli autori e del loro grado di appartenenza al concetto
	 * 
	 * @param path
	 * @param fileName
	 * 
	 * @return Table<Integer, String, Double> table contenente i keyword vector
	 * 
	 * @throws AuthorWithoutPapersException 
	 * @throws MatlabInvocationException 
	 * @throws MatlabConnectionException 
	 * @throws NoAuthorsWithSuchIDException 
	 * @throws Exception 
	 * 
	 */
	public Table<Integer, String, Double> getConceptsKeywordVectors(String path, String fileName) throws MatlabConnectionException, MatlabInvocationException, AuthorWithoutPapersException, NoAuthorsWithSuchIDException {
		Table<Integer, String, Double> conceptsKeywordVectors = TreeBasedTable.create();
		
		this.getTop3SVDAuthor(path, fileName);  // sfruttiamo il side-effect del fatto che stampi su file!
        
		String readFileName = "U_" + fileName;
		
		// estraggo la lista degli id degli autori
		ArrayList<Author> authors = this.getAuthors();
		ArrayList<Integer> authorsIDs = new ArrayList<Integer>();
		for(Author a : authors) {
			authorsIDs.add(a.getAuthorID());
		}
		// System.out.println(authorsIDs);
		
		// Leggiamo il file corrispondente alla matrice U_SimilarityMatrixAuthor.csv
		Table<Integer, Integer, Double> top3ReadTable = IO.readTop3SVDMatrixAuthorFromFile(path, readFileName, authorsIDs);
		
		// Estraggo l'insieme globale delle keyword
		ArrayList<String> globalKeywordSet = this.getGlobalKeywordSet();
			
		// Inizializziamo la struttura, mettendo tutte le celle a 0 della Table risultato
		// TODO: controllare se è necessario!!!
		for(Integer conceptID : top3ReadTable.rowKeySet()) {
			for (String keyword : globalKeywordSet) {
				conceptsKeywordVectors.put(conceptID, keyword, 0.0);
			}
		}
		
		// Popoliamo la Table risultato
		for (Integer conceptID : top3ReadTable.rowKeySet()) {
			for (Integer authorID : top3ReadTable.columnKeySet()) {
				Author currentAuthor = this.getAuthorByID(authorID);
				TreeMap<String, Double> currentKeywordVector = currentAuthor.getWeightedTFIDFVector(this);
				for (Map.Entry<String, Double> entry : currentKeywordVector.entrySet()) {
					String keyword = entry.getKey();
					Double value = entry.getValue();
					Double weight = top3ReadTable.get(conceptID, authorID);
					Double previousValue = conceptsKeywordVectors.get(conceptID, keyword);
					
					/* TODO: studiare se e' necessario (e, nel caso, come fare a) normalizzarlo!
					 *  ^- 	ci ho pensato, e probabilmente ci sta bene cosi' visto che
					 *  	non	mi pare che lo riutilizziamo da qualche altra parte
					 * 
					 * Attualmente ha 2 "caratteristiche" che non sono sicuro che ci piacciano:
					 *  - fare la moltiplicazione "abbassa" il valore corrente;
					 *  - sommare piu' prodotti può portare a valori piu' alti di 1.
					 */
					Double currentValue = previousValue + (value * weight);

					conceptsKeywordVectors.put(conceptID, keyword, currentValue);
				}
			}
		}
		
		return conceptsKeywordVectors;
	}
	
	// Phase 3 - Task 1
	/**
	 * Crea un grafo dei coautori pesato in cui ci sia un arco tra due autori se sono
	 * stati coautori di almeno un articolo, il cui peso rappresenti la similarita’
	 * dei corrispondenti autori, misurata in termini di keyword vectors o top 3 semantiche latenti degli autori (PCA e SVD).
	 * 
	 * @return Graph grafo dei coautori pesato
	 * @throws NoAuthorsWithSuchIDException 
	 */
	public Graph getCoAuthorsGraphBasedOnKeywordVectors() throws NoAuthorsWithSuchIDException {
		
		//TODO: verificare se e come spostare le chiamate in modo da nascondere il GraphEngine
		GraphEngine ge = GraphEngine.getGraphEngine();
        UndirectedGraph coAuthorsGraphBasedOnKeywordVectors = ge.getGraphModel().getUndirectedGraph();

        ArrayList<Author> authorsList = this.getAuthors();
        
        // I passata: riempiamo il grafo con i nodi 
        for (Author a : authorsList) {
        	String authorID = a.getAuthorID().toString();
        	Node n = ge.getGraphModel().factory().newNode(authorID);
        	n.getNodeData().setLabel(authorID);
        	coAuthorsGraphBasedOnKeywordVectors.addNode(n);        	
        }
        
        // II passata: colleghiamo i nodi che rappresentano due autori che sono coautori con un arco
        for (Author author1 : authorsList) {
        	ArrayList<Integer> coAuthorsIDs = author1.getCoAuthorsIDs();
        	Node node1 = coAuthorsGraphBasedOnKeywordVectors.getNode(author1.getAuthorID().toString());
        	for(Integer author2ID : coAuthorsIDs) {
        		String authorIDString = author2ID.toString();
        		Author author2 = this.getAuthorByID(author2ID);
        		Node node2 = coAuthorsGraphBasedOnKeywordVectors.getNode(authorIDString);
        		
        		// se l'arco tra i 2 nodi non esiste gia', lo aggiungo
        		if (coAuthorsGraphBasedOnKeywordVectors.getEdge(node1, node2) == null) {
        			float weight = (float) author1.getSimilarityOnKeywordVector(author2, this);
        			Edge edge = ge.getGraphModel().factory().newEdge(node1, node2, weight, false);
        			edge.getEdgeData().setLabel(String.valueOf(weight));
        			coAuthorsGraphBasedOnKeywordVectors.addEdge(edge);
        		}
        	}     	
        }
        
		return coAuthorsGraphBasedOnKeywordVectors;		
	}
		
	// Phase 3 - Task 2
	/**
	 * Create un grafo degli articoli di cui si e’ stati coautori, in cui esiste un arco
	 * tra due articoli se condividono almeno un autore, e in cui i pesi rappresentano la similarita'
	 * tra i corrispondenti articoli misurata in termini di TF-IDF keyword-vectors.
	 * 
	 * @return Graph grafo degli articoli
	 * @throws NoAuthorsWithSuchIDException 
	 */
	public Graph getCoAuthoredPapersGraphBasedOnKeywordVectors() throws NoAuthorsWithSuchIDException {
		
		//TODO: verificare se e come spostare le chiamate in modo da nascondere il GraphEngine
		GraphEngine ge = GraphEngine.getGraphEngine();
        UndirectedGraph coAuthoredPapersGraphBasedOnKeywordVectors = ge.getGraphModel().getUndirectedGraph();

        ArrayList<Paper> papersList = this.getPapers();
        
        // I passata: riempiamo il grafo con i nodi 
        for (Paper p : papersList) {
        	String paperID = p.getPaperID().toString();
        	Node n = ge.getGraphModel().factory().newNode(paperID);
        	n.getNodeData().setLabel(paperID);
        	coAuthoredPapersGraphBasedOnKeywordVectors.addNode(n);        	
        }
        
        // II passata: colleghiamo i nodi che rappresentano due paper che hanno autori in comune con un arco
        for (Paper paper1 : papersList) {
        	ArrayList<Integer> paper1AuthorsIDs = paper1.getAuthors();
        	Node node1 = coAuthoredPapersGraphBasedOnKeywordVectors.getNode(paper1.getPaperID().toString());
        	
        	// scorriamo tutti gli autori del paper1 e colleghiamo i loro paper al nodo
        	for(Integer author2ID : paper1AuthorsIDs) {
        		Author author2 = this.getAuthorByID(author2ID);
        		
        		for(Paper paper2 : author2.getPapers()) {
	        		Node node2 = coAuthoredPapersGraphBasedOnKeywordVectors.getNode(paper2.getPaperID().toString());
	        		
	        		// se l'arco tra i 2 nodi non esiste gia', lo aggiungo
	        		if (coAuthoredPapersGraphBasedOnKeywordVectors.getEdge(node1, node2) == null) {
	        			
	        			//TODO: calcolare il peso come similarita' tra gli articoli
	        			// misurata in termini di TF keyword-vectors o TF-IDF keyword-vectors.
	        			float weight = (float) paper1.getSimilarityBasedOnTFIDFVector(paper2, this); //(float) author1.getSimilarityOnKeywordVector(author2, this);
	        			Edge edge = ge.getGraphModel().factory().newEdge(node1, node2, weight, false);
	        			edge.getEdgeData().setLabel(String.valueOf(weight));
	        			coAuthoredPapersGraphBasedOnKeywordVectors.addEdge(edge);
	        		}
        		}
        	}     	
        }
        
		return coAuthoredPapersGraphBasedOnKeywordVectors;		
	}
	

	/**
	 * Crea (e apre) un file che consente di visualizzare graficamente il grafo dei coautori.
	 * 
	 * E' possibile scegliere come file di output sia il formato PDF che GEXF (per Gephi).
	 * 
	 * @param type il tipo di file: PDF o GEXF
	 * @throws WrongFileTypeException
	 * @throws NoAuthorsWithSuchIDException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes") //FIXME: vedere come risolvere i warning
	public void printGraphOnFile(Graph graph, String fileName, String type) throws WrongFileTypeException, NoAuthorsWithSuchIDException, IOException {
        
        //Count nodes and edges
        System.out.println("Nodes: " + graph.getNodeCount() + " Edges: " + graph.getEdgeCount());
        //Iterate over nodes
        for(Node n : graph.getNodes()) {
	        Node[] coauthors = graph.getNeighbors(n).toArray();
	        System.out.println(n.getNodeData().getLabel() + " has " + coauthors.length + " coauthors");
        }
        
        GraphEngine ge = GraphEngine.getGraphEngine();
        GraphModel graphModel = ge.getGraphModel();
        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();

        RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
      
        //Rank color by Degree
        Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
        AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
        
        colorTransformer.setColors(new Color[]{new Color(0x66CCCC), new Color(0x7EB6FF)});
        rankingController.transform(degreeRanking, colorTransformer);

        //Get Centrality
        GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(graphModel, attributeModel);

        //Rank size by centrality
        AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
        Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
        AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
        sizeTransformer.setMinSize(5);
        sizeTransformer.setMaxSize(30);
        rankingController.transform(centralityRanking, sizeTransformer);

        //Rank label size - set a multiplier size
        Ranking centralityRanking2 = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
        AbstractSizeTransformer labelSizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.LABEL_SIZE);
        labelSizeTransformer.setMinSize(1);
        labelSizeTransformer.setMaxSize(3);
        rankingController.transform(centralityRanking2,labelSizeTransformer);

        //Set 'show labels' option in Preview - and disable node size influence on text size
        PreviewModel previewModel = Lookup.getDefault().lookup(PreviewController.class).getModel();
        previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(new Color(0x7EB6FF)));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR, new DependantOriginalColor(new Color(0x101010)));
        
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.FALSE);

        //Export
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
       	
    	File parentDirectory = new File(System.getProperty("user.dir")).getParentFile();
    	
    	switch(type) {
        	case "PDF":
        		File pdfFile = new File(parentDirectory + "/data/" + fileName + ".pdf");
        		ec.exportFile(pdfFile);
        		Desktop.getDesktop().open(pdfFile);
        		break;
        	case "GEXF":
        		File gexfFile = new File(parentDirectory + "/data/" + fileName + ".gexf");
            	ec.exportFile(gexfFile);
            	Desktop.getDesktop().open(gexfFile);
        		break;
        	default:
        		throw new WrongFileTypeException("Wrong file type exception: try with \"PDF\" or \"GEXF\".");
    	}       	
	}
	
	// Phase 3 - Task 3 (Clustering)
	// -- rimossi metodi "home made" (presenti fino a rev. 340) --
	// -- per questa fase vedere la src.utils.Clusterer.java --
	
	// Phase 3 - Task 4
	
	/**
	 * Dato il grafo dei “co-autori”, individuare ed elencare i K nodi maggiormente dominanti
	 * utilizzando l’algoritmo Page Rank (PR) (per un valore K dato in input).
	 * 
	 * Si veda “S. Brin and L. Page ”The anatomy of a large-scale hypertextual Web search engine”.
	 * 	Computer Networks and ISDN Systems 30: 107 -117, 1998” 
	 * 
	 * @param graph il grafo da analizzare
	 * @param k il numero di nodi dominanti richiesto
	 * 
	 * @return Node[] i k nodi maggiormente dominanti
	 */
	public Node[] pageRank(Graph graph, int k) {
		Node[] mostDominantNodes;
		
		GraphModel graphModel = graph.getGraphModel();
		AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
		AttributeModel attributeModel = ac.getModel();
		
		//See visible graph stats
		UndirectedGraph graphVisible = graphModel.getUndirectedGraphVisible();
		
		PageRank pageRank = new PageRank();
		pageRank.execute(graphModel, attributeModel);
		
		//Sort nodes by Pagerank
		Node[] nodes = graphVisible.getNodes().toArray();
		Arrays.sort(nodes, new Comparator<Node>() {

		    public int compare(Node o1, Node o2) {
		      Double f1 = (Double) o1.getNodeData().getAttributes().getValue("pageranks");
		      Double f2 = (Double) o2.getNodeData().getAttributes().getValue("pageranks");
		      return f2.compareTo(f1);
		    }
		});

//		//Print top k
//		for (int i = 0; i < nodes.length && i < k; i++) {
//		    Double p = (Double) nodes[i].getNodeData().getAttributes().getValue("pageranks");
//		    System.out.println(nodes[i].getNodeData().getLabel() + " - " + p);
//		}
		
		mostDominantNodes = nodes;
		
		return mostDominantNodes;
	}
	
	// Phase 3 - Task 5
	/**
	 * Dato il grafo dei coautori e dato un autore,
	 * individuare ed elencare i K nodi piu' simili
	 * dal punto di vista del contenuto (per un valore K dato in input).
	 * 
	 * @param graph il grafo
	 * @param author l'autore di partenza
	 * @param k il numero di autori richiesti
	 * @return Map<String, Double> i K autori piu' simili all'autore, con relativa similarita'
	 */
	public Map<String, Double> getKMostSimilarAuthors(Graph graph, Author author, int k) {
		Map<String, Double> kMostSimilarAuthors = new LinkedHashMap<String, Double>();
		
		// Recupero il nodo corrispondente all'autore
		String authorIDLabel = author.getAuthorID().toString();
		// (i nodi sono etichettati con una stringa che contiene l'authorID)
		Node author1Node = graph.getNode(authorIDLabel);

		// Recupero gli archi incidenti nel nodo costituito dall'autore
		EdgeIterable nodeEdges = graph.getEdges(author1Node);
		
		// Recupero i nodi collegati al nodo dell'autore
		HashMap<String, Double> connectedNodes = new HashMap<String, Double>();
		Node source, target;
		for(Edge anEdge : nodeEdges) {
			source = anEdge.getSource();
			target = anEdge.getTarget();
			if(source.equals(author1Node)) {
				connectedNodes.put(target.getNodeData().getLabel(), (double) anEdge.getWeight());
			}
			else {
				connectedNodes.put(source.getNodeData().getLabel(), (double) anEdge.getWeight());
			}
		}
		
		// Ordino la HashMap in maniera decrescente sui valori
		
//		System.out.println("Before:");
//		MapUtils.printMapStringDouble(connectedNodes);
		@SuppressWarnings("unchecked")
		Map<String, Double> sortedConnectedNodes = MapUtils.sortByComparator(connectedNodes);
//		System.out.println("After:");
//		MapUtils.printMapStringDouble(sortedConnectedNodes);
		
//		System.out.println("Found " + sortedConnectedNodes.size() + " authors");
		
		Set<Entry<String, Double>> connectedNodesEntrySet = sortedConnectedNodes.entrySet();
		for(Entry<String, Double> connectedNode : connectedNodesEntrySet) {
			if (k > 0) {
				kMostSimilarAuthors.put(connectedNode.getKey(), connectedNode.getValue());
				k--;
			}
			else {
				break;
			}
		}		
		
		return kMostSimilarAuthors;
	}
	
	//######################### PPR Draft ######################################
	
//	public LinkedList<TermTF> getPersonalizedPageRank(String authorID, int n)
//			throws SQLException, IOException, ParseException {
//		return getPersonalizedPageRank(authorID, n, 0.85);
//	}
//
//	public LinkedList<TermTF> getPersonalizedPageRank(String authorID, int n, double probability) {
//		HashMap<String, Double> outmap = new HashMap<String, Double>();
//		Graph<String, Edge> graph;
//		graph = getCoauthorsGraph(TFIDF);
//
//		int numberNode = graph.getVertexCount();
//
//		if (n > numberNode) {
//			throw new IOException("N is too big");
//		}
//
//		Collection<String> vertices = graph.getVertices();
//		String[] nodes = new String[0];
//		nodes = vertices.toArray(nodes);
//		double[][] z = new double[numberNode][numberNode];
//
//		// creo la matrice di adiacenza
//		for (int i = 0; i < numberNode; i++) {
//			for (int j = 0; j < numberNode; j++) {
//				if (graph.isNeighbor(nodes[i], nodes[j])) {
//					z[i][j] = 1;
//				} else {
//					z[i][j] = 0;
//				}
//			}
//		}
//
//		int pos = -1;
//		for (int k = 0; k < ireaderAuthor.numDocs(); k++) {
//			String authorIdCorrente = ireaderAuthor.document(k)
//					.getField(DocumentField.ID).stringValue();
//			if (authorID.equals(authorIdCorrente)) {
//				pos = k;
//			}
//		}
//
//		if (pos >= 0) {
//			if (verboseMode) {
//				System.out.print("Executes MATLAB function...");
//			}
//			Object[] tdObj = latsem.getPersonalizedPageRank(1, z, pos,
//					probability);
//			double[] ret = (double[]) ((MWArray) tdObj[0]).getData();
//			if (verboseMode) {
//				System.out.println("OK");
//			}
//
//			for (int k = 0; k < ret.length; k++) {
//				outmap.put(nodes[k], ret[k]);
//			}
//		}
//		LinkedList<TermTF> outmap2 = GeneralUtils.sortProbabilityDescendent(outmap);
//		LinkedList<TermTF> outmapok = new LinkedList();
//
//		for (int i = 0; i < n; i++) {
//			outmapok.add(outmap2.get(i));
//		}
//
//		return outmapok;
//	}
	
	
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
