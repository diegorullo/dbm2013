package lab1;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

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
		DBEngine db = new DBEngine();
		db.init();
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
				System.out.println("Modello TF per \"" + p.getTitle() + "\" (" + paperid + "):");
				keywordVector = p.key_TF(p.getKeywords());
				for (Map.Entry<String, Double> entry : keywordVector.entrySet()) {
		            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
		        }
			}			
			else if(model.equalsIgnoreCase("TFIDF")) {
				System.out.println("Modello TFIDF per \"" + p.getTitle() + "\" (" + paperid + "):");
				tfVector = p.key_TF(p.getKeywords());
				keywordVector = dblp.key_TFIDF(p.getKeywords(), tfVector);
//				for (Map.Entry<String, Double> entry : keywordVector.entrySet()) {
//		            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
//		        }
			}			
			else System.out.print("Modello per i pesi ERRATO !!!");			

			//input.close();
    }
}
