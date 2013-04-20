package lab1;

import java.io.IOException;
import java.sql.SQLException;

import utils.DBEngine;
import dblp.Author;
import dblp.Paper;
import dblp.Corpus;

public class Main {
	

	public static void main(String args[]) throws Exception	{
		DBEngine db = new DBEngine();
		db.init();
		
		Author a = db.newAuthor(2390072);
		System.out.println(a);
		System.out.println("-------------------");
		System.out.println(">TF model:");
		System.out.println(a.getWTFVector());
		System.out.println("-------------------");
		System.out.println(">TFIDF model:");
		Corpus dblp = db.newCorpus();
		System.out.println(a.getWTFIDFVector(dblp));
		
		
		
//		for (Paper p : a.getPapers()) {
//			//System.out.println(p.getKeywordSet());
//			//System.out.println("Paper published in " + p.getYear() + "; it is " + p.getAge() + " years old.");		}
//			System.out.println(p.getWTFVector(p.getWeightBasedOnAge()));				
//			//System.out.println(a.getCombinedKeywordSet());
//		}
		
//		/* Task 1:
//	    - tokenizare
//		- stemming
//		- eliminare le stop words
//		- calcolo di tf e tfidf per ogni keyword
//		- costruzione del keyword vector <keyword, weight>
//		 */		
//		Map<String, Double> tfidfVector = new TreeMap<String, Double>();
//		Corpus dblp = db.newCorpus();
//		int paperid = 237222;
//		//Scanner input = new Scanner(System.in);		
//		//System.out.println("Inserisci il codice dell'articolo: ");
//		//int paperid = input.nextInt();	
//		Paper p = db.newPaper(paperid);		
//
//		//System.out.println("Inserisci il modello per i pesi (TF oppure TFIDF): ");
//		//String model = input.next();
//		String model = "ciao";
//
//			if(model.equalsIgnoreCase("TF")) {
//				// ordinamento alfabetico prtType=0
//				Printer.printPaperTFVector(p.getTFVector(), 0);
//			}			
//			else if(model.equalsIgnoreCase("TFIDF")) {
//				System.out.println("Modello TFIDF per \"" + p.getTitle() + "\" (" + paperid + "):");
//				try {
//					tfidfVector = p.getTFIDFVector(dblp);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					System.out.println("W Stefania.");
//					e.printStackTrace();
//				}
//				for (Map.Entry<String, Double> entry : tfidfVector.entrySet()) {
//		            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
//		        }
//			}			
//			//else System.out.println("Modello per i pesi ERRATO !!!");			
//			
//			//input.close();
//			
//		 	//Populate example map with values
////			67 	+ 	testTreeMap.put("Sam", 25.1);
////			68 	+ 	testTreeMap.put("John", 58.9);
////			69 	+ 	testTreeMap.put("Sunny", 58.9);
////			70 	+ 	testTreeMap.put("Linda", 53.3);
////			71 	+ 	testTreeMap.put("Amit", 53.3);
////			72 	+ 	testTreeMap.put("Sheila", 45.6);
////			73 	+ 	testTreeMap.put("Lili", 85.2);
//			
//			/* Task 2:
//		    - ricerca articoli per autore
//		    - tokenizare
//			- stemming
//			- eliminare le stop words
//			- calcolo di tf e tfidf per ogni keyword
//			- costruzione del combined keyword vector per autore <keyword, weight>
//			 */
//			
//			/* Task 3:
//		    - ricerca articoli per autore
//		    - tokenizare
//			- stemming
//			- eliminare le stop words
//			- calcolo di tf e tfidf per ogni keyword
//			- costruzione del combined keyword vector per autore <keyword, weight>
//			 */	
    }
}
