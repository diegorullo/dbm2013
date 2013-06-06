package utils;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Normalization {
	
	/**
	 * Controlla che gli elementi presenti nel vettore siano tutti 0
	 * @param treemap
	 * @return boolean true/false
	 */
	public static boolean isAllZeros(TreeMap<String, Double> treemap) 
	{
		boolean all_zeroes = true;
		
		// Controlliamo che la treemap non contenga tutti valori a 0.0:
		for (Entry<String, Double> entry : treemap.entrySet()) {
			if (!entry.getValue().toString().equals("0.0")) {
				all_zeroes = false;
				break;
			}
		}
		return all_zeroes;
	}
	
	public static boolean isAllZeros(ArrayList<Double> arrayList) 
	{
		boolean all_zeroes = true;
		
		// Controlliamo che la treemap non contenga tutti valori a 0.0:
		for (Double entry : arrayList) {
			if (!entry.toString().equals("0.0")) {
				all_zeroes = false;
				break;
			}
		}
		return all_zeroes;
	}
	
	/**
	 * Controlla che la somma degli elementi di map sia uno (a meno di un epsilon)
	 * @param treemap
	 * @param epsilon
	 * @return boolean true/false
	 */
	public static boolean isNormalized(TreeMap<String, Double> treemap, double epsilon) {
		boolean result = false;
		double uno = 0.0;
				
		// Se sono tutti 0.0 si considera normalizzato
		if(isAllZeros(treemap)) {
			result = true;
		}
		else {
			for (Entry<String, Double> entry : treemap.entrySet()) {			
				uno += Math.abs(entry.getValue());
			}
			
			epsilon = Math.abs(epsilon);
					
			if(((1.0 - epsilon) <= uno) && (uno <= (1.0 + epsilon))) {
			result = true;
			} 
			
			//System.out.println("Debug: " + uno);		
		}
		return result;
	}
	
	public static boolean isNormalized(ArrayList<Double> arrayList, double epsilon) {
		boolean result = false;
		double uno = 0.0;
				
		// Se sono tutti 0.0 si considera normalizzato
		if(isAllZeros(arrayList)) {
			result = true;
		}
		else {
			for (Double entry : arrayList) {			
				uno += Math.abs(entry);
			}
			
			epsilon = Math.abs(epsilon);
					
			if(((1.0 - epsilon) <= uno) && (uno <= (1.0 + epsilon))) {
			result = true;
			} 
			
			//System.out.println("Debug: " + uno);		
		}
		return result;
	}
	
	/**
	 * Normalizza la treemap data in input;
	 * se l'input è già normalizzato, non fa nulla
	 */
	public static TreeMap<String, Double> normalize(TreeMap<String, Double> treemap) {
		TreeMap<String, Double> normalizedTreeMap = new TreeMap<String, Double>();
		
		double epsilon = (double) 1/1000000;
		double denominatore = 0.0;
		@SuppressWarnings("unused")
		double debug = 0.0;
		
		TreeMap<String, Double> treemapWithoutNegativeZeroes = new TreeMap<String, Double>();
		
		// Rimuoviamo eventuali valori a -0.0, sostituendoli con 0.0
		for (Entry<String, Double> entry : treemap.entrySet()) {
			if(entry.getValue().toString().equals("-0.0")) {
				treemapWithoutNegativeZeroes.put(entry.getKey(), 0.0);
				//System.out.println("Ho corretto 0.0 in " + entry.getKey());
			}
			else {
				treemapWithoutNegativeZeroes.put(entry.getKey(), treemap.get(entry.getKey()));
				//System.out.println("Ho messo " + treemap.get(entry.getKey()) + " in " + entry.getKey());
			}
		}
		
		//treemap = treemapWithoutNegativeZeroes;
		
		if(Normalization.isNormalized(treemapWithoutNegativeZeroes, epsilon)) {
			//System.out.println("La map in input è già normalizzata!");
			normalizedTreeMap = treemapWithoutNegativeZeroes;
		}
		else {
			for (Entry<String, Double> entry : treemapWithoutNegativeZeroes.entrySet()) {
				denominatore += Math.abs(entry.getValue());
			}
			for (Entry<String, Double> entry : treemapWithoutNegativeZeroes.entrySet()) {
				normalizedTreeMap.put(entry.getKey(), (double)(entry.getValue() / denominatore));
				debug += Math.abs(entry.getValue()) / denominatore;
			}
//			System.out.println("Debug (treemap.1st = " + treemap.firstKey() +  "): " + debug);
			
			//System.out.println("denominatore: " + denominatore);
			//System.out.println("normalizedTreeMap.size(): " + normalizedTreeMap.size());
		}

		return normalizedTreeMap;
	}

	public static ArrayList<Double> normalize(ArrayList<Double> arrayList) {
		ArrayList<Double> normalizedArrayList = new ArrayList<Double>();
		
		double epsilon = (double) 1/1000000;
		double denominatore = 0.0;
		@SuppressWarnings("unused")
		double debug = 0.0;
		
		ArrayList<Double> arrayListWithoutNegativeZeroes = new ArrayList<Double>();
		
		// Rimuoviamo eventuali valori a -0.0, sostituendoli con 0.0
		for (Double entry : arrayList) {
			if(entry.toString().equals("-0.0")) {
				arrayListWithoutNegativeZeroes.add(0.0);
			}
			else {
				arrayListWithoutNegativeZeroes.add(entry);
			}
		}
		
		if(Normalization.isNormalized(arrayListWithoutNegativeZeroes, epsilon)) {
			//System.out.println("La map in input è già normalizzata!");
			normalizedArrayList = arrayListWithoutNegativeZeroes;
		}
		else {
			for (Double entry : arrayListWithoutNegativeZeroes) {
				denominatore += Math.abs(entry);
			}
			for (Double entry : arrayListWithoutNegativeZeroes) {
				normalizedArrayList.add((double)(entry / denominatore));
				debug += entry / denominatore;
			}
			
			//System.out.println("denominatore: " + denominatore);
		}

		return normalizedArrayList;
	}

}
