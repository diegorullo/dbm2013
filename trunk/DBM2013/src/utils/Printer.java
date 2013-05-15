package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import dblp.Author;

public class Printer {
	//FIXME
	final static String documentTermMatrixFilePath = "../data/X.csv";
	
	
	// Stampa del vettore TFIDF (Paper):
	// prtType=0 tipo ordinamento: alfabetico
	// prtType=1 tipo ordinamento: indice numerico
	public static void printVector(Map<String, Double> m, int prtType)
			throws IOException {

		if (prtType == 0) {
			System.out.println("<Vettore TF(Paper) [ordinamento: alfabetico]>");
			for (Map.Entry<String, Double> entry : m.entrySet()) {
				System.out.println("<" + entry.getKey() + ", "
						+ entry.getValue() + ">");
			}
		} else {
			ArrayList<Map.Entry<String, Double>> ordV = orderVectorMap(m);
			System.out
					.println("<Vettore TF(Paper) [ordinamento: value decrescente]>");

			for (Entry<String, Double> entry : ordV) {

				System.out.println("<" + entry.getKey() + ", "
						+ entry.getValue() + ">");
			}

		}
	}

	public static ArrayList<Map.Entry<String, Double>> orderVectorMap(
			Map<String, Double> m) {

		ArrayList<Map.Entry<String, Double>> ordVector = new ArrayList<Map.Entry<String, Double>>(
				m.entrySet());

		Collections.sort(ordVector,
				new Comparator<Map.Entry<String, Double>>() {
					public int compare(Map.Entry<String, Double> o1,
							Map.Entry<String, Double> o2) {
						Double first = (Double) o1.getValue();
						Double second = (Double) o2.getValue();
						return second.compareTo(first);
					}
				});

		return ordVector;
	}

	public static void printDocumentTermMatrix(ArrayList<TreeMap<String, Double>> documentTermMatrix, Author author) {

		System.out.println("Document-term matrix");
		for (String s : author.getKeywordSet()) {
			System.out.print(s + ",\t\t");
		}
		System.out.print("\n");
		for (TreeMap<String, Double> riga : documentTermMatrix) {
			for (Map.Entry<String, Double> cella : riga.entrySet()) {
				System.out.printf("%.4f", cella.getValue());
				System.out.print(",\t\t");
			}
			System.out.print("\n");
		}

		return;
	}
	
	/**
	 * stampa su file, percorso e nome statici, della matrice DocumentTermMatrix
	 * @param documentTermMatrix matrice
	 * @param path path della destinazione
	 * @throws Exception
	 */
	public static void printDocumentTermMatrixOnFile(ArrayList<TreeMap<String, Double>> documentTermMatrix, String path) throws Exception{
		try {
		  //FileOutputStream file = new FileOutputStream(path);
		  FileOutputStream file = new FileOutputStream(documentTermMatrixFilePath);
		  PrintStream Output = new PrintStream(file);

		  for(TreeMap<String, Double> riga : documentTermMatrix) {
			  int i=0;
			  for(Map.Entry<String, Double> cella : riga.entrySet()) {
				  Output.print(cella.getValue());
				  i++;
				  if (i<riga.size()){
					  Output.print(",");
				  }
			  }
			  Output.print("\n");
		  }	
		  Output.close();
	    } 
		catch (IOException e) {
	      System.out.println("Errore: " + e);
	      System.exit(1);
	    }

	}

//	public static void main(String args[]) throws IOException {
//
//		Map<String, Double> testTreeMap = new TreeMap<String, Double>();
//
//		// Populate example map with values
//		testTreeMap.put("Sam", 25.1);
//		testTreeMap.put("John", 58.9);
//		testTreeMap.put("Sunny", 58.9);
//		testTreeMap.put("Linda", 53.3);
//		testTreeMap.put("Amit", 53.3);
//		testTreeMap.put("Sheila", 45.6);
//		testTreeMap.put("Lili", 85.2);
//
//		printVector(testTreeMap, 1);
//	}
}
