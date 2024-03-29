package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.Table;

import dblp.Author;
import dblp.Paper;
import exceptions.AuthorWithoutPapersException;

public class Printer {
	
	// Stampa del vettore TFIDF (Paper):
	// prtType=0 tipo ordinamento: alfabetico
	// prtType=1 tipo ordinamento: indice numerico
	public static void printVector(TreeMap<String, Double> m, int prtType) throws IOException {

		if (prtType == 0) {
			System.out.println("<Vettore TF(Paper) [ordinamento: alfabetico]>");
			for (Map.Entry<String, Double> entry : m.entrySet()) {
				System.out.println("<" + entry.getKey() + ", "
						+ entry.getValue() + ">");
			}
		} else {
			ArrayList<Map.Entry<String, Double>> ordV = orderVectorTreeMap(m);
			System.out
					.println("<Vettore TF(Paper) [ordinamento: value decrescente]>");

			for (Entry<String, Double> entry : ordV) {

				System.out.println("<" + entry.getKey() + ", "
						+ entry.getValue() + ">");
			}

		}
	}

	public static ArrayList<Map.Entry<String, Double>> orderVectorTreeMap(TreeMap<String, Double> m) {

		ArrayList<Map.Entry<String, Double>> ordVector = new ArrayList<Map.Entry<String, Double>>(
				m.entrySet());

		Collections.sort(ordVector, new Comparator<Map.Entry<String, Double>>() {
					public int compare(Map.Entry<String, Double> o1,
							Map.Entry<String, Double> o2) {
						Double first = (Double) o1.getValue();
						Double second = (Double) o2.getValue();
						return second.compareTo(first);
					}
				});

		return ordVector;
	}

	/**
	 * Stampa a video matrice document-term
	 * 
	 * @param documentTermMatrix matrice
	 * @param author autore
	 * @throws AuthorWithoutPapersException 
	 */
	public static void printDocumentTermMatrix(ArrayList<TreeMap<String, Double>> documentTermMatrix, Author author) throws AuthorWithoutPapersException {

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
	 * Stampa a video una matrice 
	 * 
	 * @param matrix matrice
	 * 
	 */
	public static void printMatrix(ArrayList<ArrayList<Double>> matrix) {
		for (ArrayList<Double> riga : matrix) {
			for (Double cella : riga) {
				System.out.printf("\t%.7f", cella);
				System.out.print(",");
			}
			System.out.print("\n");
		}

		return;
	}
	
	public static void printLinkedHashMap(LinkedHashMap<String,Double> rank) {
		for (Entry<String, Double> entry : rank.entrySet()) {

			System.out.println("<" + entry.getKey() + ", "
					+ entry.getValue() + ">");
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
	
	
	public static void printSimilarityTable(Table<Integer, Integer, Double> table) {
		for (int i : table.rowKeySet()) {
			for (int j : table.columnKeySet()) {
				System.out.printf("\t%.7f", table.get(i, j));
			}
			System.out.println();
		}
	}
	
	public static void printAuthorAuthorSimilarityTableWithCaptions(Table<Integer, Integer, Double> table) {
		int columnsSize = table.columnKeySet().size();
		System.out.printf("Autori |  ");
		for (int i : table.rowKeySet()) {
			System.out.printf("\t%d\t", i);
		}
		System.out.println();
		//Riga orizzontale
		for(int k = 0; k < 16 * (columnsSize + 3); k++) {
			System.out.printf("-");
		}
		System.out.println();
		for (int i : table.rowKeySet()) {
			System.out.printf("%d|", i);
			for (int j : table.columnKeySet()) {
				System.out.printf("\t%.7f", table.get(i, j));
			}
			System.out.println();
		}
	}
	
	public static void printTop3SVDMatrixAuthorWithCaptions(Table<Integer, Integer, Double> table) {
		int columnsSize = table.columnKeySet().size();		
		System.out.printf("top 3\t|  ");
		for (int i : table.columnKeySet()) {
			System.out.printf("\t%d\t", i);
		}
		System.out.println();
		//Riga orizzontale
		for(int k = 0; k < 16 * (columnsSize + 3); k++) {
			System.out.printf("-");
		}
		System.out.println();
		for (int i : table.rowKeySet()) {
			System.out.printf("%d\t|", i);
			for (int j : table.columnKeySet()) {
				System.out.printf("\t%.7f", table.get(i, j));
			}
			System.out.println();
		}
	}
	
	/**
	 * Stampa una lista di autori, disponendoli su una sola riga
	 * ed elencandone id e nome
	 * @param authorsList
	 */
	public static void printAuthorsList(ArrayList<Author> authorsList) {
		for(Author a : authorsList) {
			System.out.print("<[" + a.getAuthorID() + "] " + a.getName() + "> ");
		}
		System.out.print("\n");
	}
	
	/**
	 * Stampa una lista di paper, disponendoli su una sola riga
	 * ed elencandone id e titolo
	 * @param papersList
	 */
	public static void printPapersList(ArrayList<Paper> papersList) {
		for(Paper p : papersList) {
			System.out.print("<[" + p.getPaperID() + "] " + p.getTitle() + "> ");
		}
		System.out.print("\n");
	}
	
	public static void printConceptsKeywordsTableWithCaptions(Table<Integer, String, Double> table) {
		int columnsSize = table.columnKeySet().size();
		System.out.printf("KW     |  ");
		for (String s : table.columnKeySet()) {
			System.out.printf("\t" + s + "\t");
		}
		System.out.println();
		//Riga orizzontale
		for(int k = 0; k < 16 * (columnsSize + 3); k++) {
			System.out.printf("-");
		}
		System.out.println();
		for (int i : table.rowKeySet()) {
			System.out.printf("%d|", i);
			for (String j : table.columnKeySet()) {
				System.out.printf("\t%.7f", table.get(i, j));
			}
			System.out.println();
		}
	}
	
}
