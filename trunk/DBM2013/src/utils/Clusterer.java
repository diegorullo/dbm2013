package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.TreeMap;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import dblp.Author;
import dblp.Corpus;

public class Clusterer {
	private SimpleKMeans simpleKMeans;
	
	/**
	 * Crea un'Instance (Weka Instance = oggetto della clusterizzazione) a partire da un Author e dal Corpus a cui questo appartiene
	 * @param author
	 * @param corpus
	 * @return l'Instance corrispondente all'Author
	 */
	public Instance createInstance(Author author, Corpus corpus) {
		
		TreeMap<String, Double> authorsKeywordVector = author.getWeightedTFIDFVector(corpus);
		
		ArrayList<String> corpusKeywords = corpus.getGlobalKeywordSet();
		int corpusKeywordsCount = corpusKeywords.size();
		
		// Genero una nuova istanza (vuota) con un numero di valori pari al numero totale di keyword;
		// inserisco un attributo in piu' alla fine, per rappresentare l'ID dell'autore
		Instance instance = new DenseInstance(corpusKeywordsCount + 1);
		
		for(String keyword : corpusKeywords) {
			if (authorsKeywordVector.containsKey(keyword)) {
				// nell'istanza, la chiave è costituita dall'indice della keyword nel globalKeywordSet
				instance.setValue(corpusKeywords.indexOf(keyword), authorsKeywordVector.get(keyword));
			}
			else {
				instance.setValue(corpusKeywords.indexOf(keyword), 0.0);
			}
		}		
		instance.setValue(corpusKeywordsCount, author.getAuthorID());
		
		return instance;
	}
	
	/**
	 * Crea una lista di Attribute (Weka Attribute), ovvero intestazioni di colonna (keyword) di ogni istanza,
	 * estraendole dal Corpus
	 * 
	 * @param corpus
	 * @return lista di Attribute
	 */
	public ArrayList<Attribute> createAttributes(Corpus corpus) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				
		ArrayList<String> corpusKeywords = corpus.getGlobalKeywordSet();
		for (String keyword : corpusKeywords) {
			attributes.add(new Attribute(keyword));
		}
		// Aggiungiamo una colonna in fondo che contiene l'authorID relativo all'istanza
		attributes.add(new Attribute("authorID"));
		
		return attributes;
	}
	
	/**
	 * Crea l'oggetto Instances (aggregato di Weka Instance) con tutti gli Author del Corpus
	 * @param corpus
	 * @return oggetto Instances
	 */
	public Instances createInstances(Corpus corpus) {
		
		// Creo un oggetto vuoto Instances...
		ArrayList<Attribute> attributesList = this.createAttributes(corpus);
		int capacity = attributesList.size();
		Instances instances = new Instances("corpusIntances", attributesList, capacity);
		
		// ...ci carico dentro le varie istanze create a partire dagli autori del corpus
		for(Author a : corpus.getAuthors()) {
			instances.add(this.createInstance(a, corpus));
		}
		
		return instances;
	}
	
	/**
	 * Stampa su file .csv le tutte le istanze generate a partire dagli Author del Corpus,
	 *  - per esigenza dettata dal formato csv, la prima riga contiene i nomi degli Attribute
	 * @param corpus
	 * @param filename
	 */
	public void printInstancesOnCSV(Corpus corpus, String filename) {
		try {
			FileOutputStream file = new FileOutputStream("../data/" + filename);
			PrintStream Output = new PrintStream(file);

			// Stampo l'intestazione costituita dai nomi di attributi (ovvero le keyword)...
			ArrayList<String> attributeNames = corpus.getGlobalKeywordSet();
			for(String attributeName : attributeNames) {
				Output.print(attributeName + ",");
			}
			//... seguite dall'ID dell'autore
			Output.println("authorID");
			
			//Stampo le istanze
			Instances instances = this.createInstances(corpus);
			for (Instance i : instances) {
				Output.println(i.toString());
			}
			Output.close();
		} catch (IOException e) {
			System.out.println("Errore: " + e);
			System.exit(1);
		}
	}
	
	public Clusterer() {
		simpleKMeans = new SimpleKMeans();
	}
	
	public SimpleKMeans getSimpleKMeans() {
		return simpleKMeans;
	}
}
