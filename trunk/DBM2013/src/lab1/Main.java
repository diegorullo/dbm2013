package lab1;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import dblp.Corpus;
import dblp.Paper;

import utils.DBEngine;

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
		Map<String, Double> keywordVector;
		Map<String, Double> tfVector;
		Corpus dblp = db.newCorpus();
		int paperid = 237222;
		//Scanner input = new Scanner(System.in);		
		//System.out.println("Inserisci il codice dell'articolo: ");
		//int paperid = input.nextInt();	
		Paper p = db.newPaper(paperid);		

		//System.out.println("Inserisci il modello per i pesi (TF oppure TFIDF): ");
		//String model = input.next();
		String model = "tf";

			if(model.equalsIgnoreCase("TF")) {
				p.printPaperTFVector();
//				System.out.println("Modello TF per \"" + p.getTitle() + "\" (" + paperid + "):");
//				keywordVector = p.getTFVector();
//				for (Map.Entry<String, Double> entry : keywordVector.entrySet()) {
//		            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
//		        }
			}			
			else if(model.equalsIgnoreCase("TFIDF")) {
				System.out.println("Modello TFIDF per \"" + p.getTitle() + "\" (" + paperid + "):");
				tfVector = p.getTFVector();
				keywordVector = p.key_TFIDF(p.getKeywords(), tfVector, dblp);
				for (Map.Entry<String, Double> entry : keywordVector.entrySet()) {
		            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
		        }
			}			
			else System.out.print("Modello per i pesi ERRATO !!!");			

			//input.close();
			
			
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
