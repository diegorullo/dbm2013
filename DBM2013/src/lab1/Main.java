package lab1;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import dblp.Corpus;
import dblp.Paper;

import utils.DBEngine;
import utils.Printer;

public class Main {
	

	public static void main(String args[]) throws SQLException, IOException	{
		DBEngine db = new DBEngine();
		db.init();
		
		/* Task 1:
	    - tokenizare
		- stemming
		- eliminare le stop words
		- calcolo di tf e tfidf per ogni keyword
		- costruzione del keyword vector <keyword, weight>
		 */		
		Map<String, Double> tfidfVector = new TreeMap<String, Double>();
		Corpus dblp = db.newCorpus();
		int paperid = 237222;
		//Scanner input = new Scanner(System.in);		
		//System.out.println("Inserisci il codice dell'articolo: ");
		//int paperid = input.nextInt();	
		Paper p = db.newPaper(paperid);		

		//System.out.println("Inserisci il modello per i pesi (TF oppure TFIDF): ");
		//String model = input.next();
		String model = "tfidf";

			if(model.equalsIgnoreCase("TF")) {
				// ordinamento alfabetico prtType=0
				Printer.printPaperTFVector(p.getTFVector(), 0);
			}			
			else if(model.equalsIgnoreCase("TFIDF")) {
				System.out.println("Modello TFIDF per \"" + p.getTitle() + "\" (" + paperid + "):");
				try {
					tfidfVector = p.getTFIDFVector(dblp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("W Stefania.");
					e.printStackTrace();
				}
				for (Map.Entry<String, Double> entry : tfidfVector.entrySet()) {
		            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
		        }
			}			
			else System.out.print("Modello per i pesi ERRATO !!!");			

			//input.close();
			
		 	//Populate example map with values
//			67 	+ 	testTreeMap.put("Sam", 25.1);
//			68 	+ 	testTreeMap.put("John", 58.9);
//			69 	+ 	testTreeMap.put("Sunny", 58.9);
//			70 	+ 	testTreeMap.put("Linda", 53.3);
//			71 	+ 	testTreeMap.put("Amit", 53.3);
//			72 	+ 	testTreeMap.put("Sheila", 45.6);
//			73 	+ 	testTreeMap.put("Lili", 85.2);
			
			/* Task 2:
		    - ricerca articoli per autore
		    - tokenizare
			- stemming
			- eliminare le stop words
			- calcolo di tf e tfidf per ogni keyword
			- costruzione del combined keyword vector per autore <keyword, weight>
			 */
			
			/* Task 3:
		    - ricerca articoli per autore
		    - tokenizare
			- stemming
			- eliminare le stop words
			- calcolo di tf e tfidf per ogni keyword
			- costruzione del combined keyword vector per autore <keyword, weight>
			 */	
    }
}
