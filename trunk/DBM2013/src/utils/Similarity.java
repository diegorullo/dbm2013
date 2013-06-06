package utils;

import java.util.Map;
import java.util.TreeMap;

public class Similarity {
	
    /**
     * Calcola la magnitudo di un vettore
     * @param doc il vettore di termini pesati 
     * @return magnitudo di un vettore di termini pesati
     */
    public static double doc_magnitudo(TreeMap<String, Double> doc) {
            double magnitudo = 0.0;
            for (Map.Entry<String, Double> term : doc.entrySet())
            {
                    magnitudo += Math.pow(term.getValue(),2);
            }

            return Math.sqrt(magnitudo);
    }

	/**
	 * Calcola la similarita' coseno tra due vettori (normalizzati)
	 * 
	 * CosineSimilarity(A,B) = (A | B)/||A||*||B||
	 * 
	 * (A | B)	-> prodotto scalare tra A e B
	 * ||A||	-> magnitudo di A
	 * ||B||	-> magnitudo di B
	 * 
	 * @param a_vector primo vettore da confrontare
	 * @param b_vector secondo vettore da confrontare
	 * @return similarita' coseno tra due vettori
	 * 
	 */
	public static double getCosineSimilarity(TreeMap<String, Double> a_vector, TreeMap<String, Double> b_vector) {
		double scalarProd = 0.0;
        double a_magnitudo = 0.0;
        double b_magnitudo = 0.0;
        double cosSim = 0.0;
		double epsilon = 1.0/100000.0;
				
		if(!Normalization.isNormalized(a_vector,epsilon)) {
			a_vector = Normalization.normalizeTreeMap(a_vector);
//			System.out.println(">>> a_vector: " + a_vector);
		}
		else {
//			System.out.println("IL PRIMO VETTORE E NORMALIZZATO");
//			System.out.println("a_vector: " + a_vector);
		}
		if(!Normalization.isNormalized(b_vector,epsilon)) {
			b_vector = Normalization.normalizeTreeMap(b_vector);
//			System.out.println(">>> b_vector: " + b_vector);
		}
		else {
//			System.out.println("IL SECONDO VETTORE E NORMALIZZATO");
//			System.out.println("b_vector: " + b_vector);
		}
		// A questo punto i vettori sono normalizzati in norma1
		
		// Le magnitudo vengono utilizzate per normalizzare in norma2
		a_magnitudo = Similarity.doc_magnitudo(a_vector);
        b_magnitudo = Similarity.doc_magnitudo(b_vector); 
        
		for(Map.Entry<String, Double> term : a_vector.entrySet())
		{
			if(b_vector.containsKey(term.getKey()))  
			{	
				// (prodotto scalare)
				scalarProd += (a_vector.get(term.getKey()) * b_vector.get(term.getKey()));
				//System.out.println("scalarProd" + scalarProd);
			}
		}	
		
		cosSim = scalarProd / (a_magnitudo * b_magnitudo);
		return cosSim;
	}

}
