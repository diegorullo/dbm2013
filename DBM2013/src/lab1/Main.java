package lab1;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.naming.NameNotFoundException;

import utils.DBEngine;
import dblp.Author;
import dblp.Paper;
import dblp.Corpus;

public class Main {
	

	public static void main(String args[]) throws Exception {
		DBEngine db = new DBEngine();
		db.init();
		
		Author a = db.newAuthor(2390072);
		
		System.out.println(a);
		System.out.println("-------------------");
		System.out.println(">Weighted TF model based on author " + a.getAuthorID() + ":");
		System.out.println(a.getWTFVector());
		System.out.println("-------------------");
		
		Corpus dblp = db.newCorpus();
		for (Author coa : dblp.getCoAuthors(a))
			System.out.println(coa.getName());
		System.out.println("-------------------");
		for (Author coa : dblp.getCoAuthorsAndSelf(a))
			System.out.println(coa.getName());
		
		
//		System.out.println(">TFIDF model on author's papers:");
//		Corpus dblp = db.newCorpus();
//		System.out.println(a.getWTFIDFVector(dblp));
//		System.out.println("-------------------");
//		System.out.println(">TF model on all papers:");
//		System.out.println(a.getPapers().get(0).getTFVector());
//		System.out.println("-------------------");
//		System.out.println(">TFIDF model on all papers:");
//		System.out.println(a.getPapers().get(0).getTFIDFVector(dblp));
		
		

//		Map<String, Double> TFVector = new TreeMap<String, Double>();
//		Map<String, Double> TFVectorG = new TreeMap<String, Double>();
//	
//		ArrayList<Paper> lp = a.getPapers();
//		double denominatore = lp.size();
//		
//		for (Paper p : lp) {
////			System.out.println(p);
////			System.out.println("Peso del paper " + p.getPaperID() + ": " + p.getWeightBasedOnAge());
////			System.out.println(p.getKeywordSet().get("algorithm"));
//			
//			TFVector = p.getTFVector();
//			Iterator<Entry<String, Double>> it = TFVector.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry<String, Double> k = (Map.Entry<String, Double>) it.next();
//				if (!TFVectorG.containsKey(k.getKey())) {
//					TFVectorG.put(k.getKey(), k.getValue()/denominatore);
//				}
//				else {
//					TFVectorG.put(k.getKey(), TFVectorG.get(k.getKey()) + k.getValue()/denominatore);
//				}
//			}
//		}
//		System.out.println("-------------------");
//		System.out.println(">TF W Stefania (pesi a 1):");
//		System.out.println(TFVectorG);
		
		
//		System.out.println(dblp.getCoAuthors(a));
		
//		List<Author> coAuthors = dblp.getCoAuthors(a);
//		ArrayList<String> kw;
//		for (Paper p : dblp.getRestrictedCorpus(coAuthors)) {
//			kw = p.getKeywords();
//			for (String k : kw) {
//				System.out.println(dblp.getRestrictedIDF(k, coAuthors));
//			}
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
