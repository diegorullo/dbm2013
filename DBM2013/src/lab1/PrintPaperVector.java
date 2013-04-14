package lab1;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import utils.DBEngine;

public class PrintPaperVector {
	
	/* Task 1:
	    - tokenizare
		- stemming
		- eliminare le stop words
		- calcolo di tf e tfidf per ogni keyword
		- costruzione del keyword vector <keyword, weight>
	*/
	public static void main(String args[]) throws SQLException, IOException	{
		
		stampaMap();
//		DBEngine db = new DBEngine();
//		db.init();
//		Map<String, Double> keywordVector;
//		Map<String, Double> tfVector;
//		Corpus dblp = db.newCorpus();
//		int paperid = 237222;
//		//Scanner input = new Scanner(System.in);		
//		//System.out.println("Inserisci il codice dell'articolo: ");
//		//int paperid = input.nextInt();	
//		Paper p = db.newPaper(paperid);		
//
//		//System.out.println("Inserisci il modello per i pesi (TF oppure TFIDF): ");
//		//String model = input.next();
//		String model = "tfidf";
//
//			if(model.equalsIgnoreCase("TF")) {
//				System.out.println("Modello TF per \"" + p.getTitle() + "\" (" + paperid + "):");
//				keywordVector = p.key_TF(p.getKeywords());
//				for (Map.Entry<String, Double> entry : keywordVector.entrySet()) {
//		            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
//		        }
//			}			
//			else if(model.equalsIgnoreCase("TFIDF")) {
//				System.out.println("Modello TFIDF per \"" + p.getTitle() + "\" (" + paperid + "):");
//				tfVector = p.key_TF(p.getKeywords());
//				keywordVector = dblp.key_TFIDF(p.getKeywords(), tfVector);
//				for (Map.Entry<String, Double> entry : keywordVector.entrySet()) {
//		            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
//		        }
//			}			
//			else System.out.print("Modello per i pesi ERRATO !!!");			
//
//			//input.close();
    }
	
	public static void stampaMap()
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
	 
	        System.out.println("<PRIMA>");
	        for (Map.Entry<String, Double> entry : testTreeMap.entrySet())
	        {
	            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
	        }

	        ArrayList<Map.Entry<String, Double>> as = new ArrayList<Map.Entry<String, Double>>( testTreeMap.entrySet());
	        
	        Collections.sort( as , new Comparator<Map.Entry<String, Double>>() {  
	            public int compare( Map.Entry<String, Double> o1 , Map.Entry<String, Double> o2 )  
	            {  
	                Double first = (Double)o1.getValue();  
	                Double second = (Double)o2.getValue();  
	                return second.compareTo( first );  
	            }  
	        });  
	        
	        System.out.println("<DOPO>");
	        for (Entry<String, Double> entry : as) 
	        {  
	        	System.out.println("<" + entry.getKey() + "," + entry.getValue()+">"); 
	        }  	
	}
	
}
