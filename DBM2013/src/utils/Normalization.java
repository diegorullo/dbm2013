package utils;

import java.util.Map;
import java.util.Map.Entry;

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
}
