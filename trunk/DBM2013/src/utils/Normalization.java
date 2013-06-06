package utils;

import java.util.Map.Entry;
import java.util.TreeMap;

public class Normalization {
	
	/**
	 * Controlla che la somma degli elementi di map sia uno (a meno di un epsilon)
	 * @param treemap
	 * @param epsilon
	 * @return boolean true/false
	 */
	public static boolean isNormalized(TreeMap<String, Double> treemap, double epsilon) {
		boolean result = false;
		boolean all_zeroes = true;
		double uno = 0.0;
		
		// Controlliamo che la treemap non contenga tutti valori a 0.0:
		// in tal caso la treemap è normalizzata
		for (Entry<String, Double> entry : treemap.entrySet()) {
			if(!entry.getValue().toString().equals("0.0")) {
				all_zeroes = false;
				break;
			}
		}		
		if(all_zeroes) {
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
	
	/**
	 * Normalizza la treemap data in input;
	 * se l'input è già normalizzato, non fa nulla
	 */
	public static TreeMap<String, Double> normalizeTreeMap(TreeMap<String, Double> treemap) {
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

}
