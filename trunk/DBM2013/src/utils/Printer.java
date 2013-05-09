package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Printer {
// Stampa del vettore TFIDF (Paper): 
// prtType=0 tipo ordinamento: alfabetico
// prtType=1 tipo ordinamento: indice numerico
	public static void printPaperTFIDFVector(Map<String, Double> m, int prtType)
			throws IOException {

		if (prtType==0) 
		{
			System.out.println("<Vettore TF(Paper) [ordinamento: alfabetico]>");
			for (Map.Entry<String, Double> entry : m.entrySet()) {
				System.out.println("<" + entry.getKey() + ", " + entry.getValue()
						+ ">");
			}
		}
		else
		{
			ArrayList<Map.Entry<String, Double>> ordV = orderVectorMap(m);
			System.out.println("<Vettore TF(Paper) [ordinamento: value decrescente]>");

			for (Entry<String, Double> entry : ordV) 
			{

				System.out.println("<" + entry.getKey() + ", " + entry.getValue()
						+ ">");
			}
			
		}
	}
	
	public static ArrayList<Map.Entry<String, Double>> orderVectorMap(Map<String, Double> m)
	{

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
	
	public static void main(String args[]) throws IOException
	{
		
		Map<String, Double> testTreeMap = new TreeMap<String, Double>();
		
		//Populate example map with values
		testTreeMap.put("Sam", 25.1);
		testTreeMap.put("John", 58.9);
		testTreeMap.put("Sunny", 58.9);
		testTreeMap.put("Linda", 53.3);
		testTreeMap.put("Amit", 53.3);
		testTreeMap.put("Sheila", 45.6);
		testTreeMap.put("Lili", 85.2);
		
		printPaperTFIDFVector(testTreeMap, 1);
	}
}

