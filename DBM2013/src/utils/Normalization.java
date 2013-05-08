package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Normalization {
	
	/**
	 * Controlla che la somma degli elementi di map sia uno (a meno di un epsilon)
	 * @param map
	 * @param epsilon
	 * @return boolean true/false
	 */
	public static boolean isNormalized(Map<String, Double> map, double epsilon) {
		boolean result = false;
		double uno = 0.0;
		
		for (Entry<String, Double> entry : map.entrySet()) {
			uno += entry.getValue();
		}
		
		if(epsilon > 0) {
			if(((1 - epsilon) <= uno) && (uno < (1 + epsilon))) {
				result = true;
			} 
		}
		else {
			if(((1 + epsilon) <= uno) && (uno < (1 - epsilon))) {
				result = true;
			}
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
		
		if(Normalization.isNormalized(treemap, epsilon)) {
			System.out.println("La map in input è già normalizzata!");
			normalizedTreeMap = treemap;
		}
		else {
			double denominatore = 0.0;
			for (Entry<String, Double> entry : treemap.entrySet()) {
				denominatore += entry.getValue();
			}
			for (Entry<String, Double> entry : treemap.entrySet()) {
				normalizedTreeMap.put(entry.getKey(), entry.getValue() / denominatore);
			}
		}
		
		return normalizedTreeMap;
	}
	
	/**
	 * Normalizza la hashmap data in input;
	 * se l'input è già normalizzato, non fa nulla
	 */
	public static HashMap<String, Double> normalizeHashMap(HashMap<String, Double> hashmap) {
		HashMap<String, Double> normalizedHashMap = new HashMap<String, Double>();
		
		double epsilon = (double) 1/1000000;
		
		if(Normalization.isNormalized(hashmap, epsilon)) {
			System.out.println("La map in input è già normalizzata!");
			normalizedHashMap = hashmap;
		}
		else {
			double denominatore = 0.0;
			for (Entry<String, Double> entry : hashmap.entrySet()) {
				denominatore += entry.getValue();
			}
			for (Entry<String, Double> entry : hashmap.entrySet()) {
				normalizedHashMap.put(entry.getKey(), entry.getValue() / denominatore);
			}
		}
		
		return normalizedHashMap;
	}
}
