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
		double uno = 0.0;
			
		for (Entry<String, Double> entry : treemap.entrySet()) {
			uno += entry.getValue();
		}
		
		epsilon = Math.abs(epsilon);
				
		if(((1.0 - epsilon) <= uno) && (uno <= (1.0 + epsilon))) {
			result = true;			
		} 
		
		//System.out.println("Debug: " + uno);		
		
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
		
		if(Normalization.isNormalized(treemap, epsilon)) {
			//System.out.println("La map in input è già normalizzata!");
			normalizedTreeMap = treemap;
		}
		else 
		{
			
			for (Entry<String, Double> entry : treemap.entrySet()) {
				denominatore += entry.getValue();
			}
			for (Entry<String, Double> entry : treemap.entrySet()) {
				normalizedTreeMap.put(entry.getKey(), (double)(entry.getValue() / denominatore));
				debug += entry.getValue() / denominatore;
			}
//			System.out.println("Debug (treemap.1st = " + treemap.firstKey() +  "): " + debug);
			
			//System.out.println("denominatore: " + denominatore);
			//System.out.println("normalizedTreeMap.size(): " + normalizedTreeMap.size());
		}

		return normalizedTreeMap;
	}

}
