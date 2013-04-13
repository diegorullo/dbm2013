package lab1;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import utils.Weights;

public class PrintPaperVector {
	
	/* Task 1:
	    - tokenizare
		- stemming
		- eliminare le stop words
		- calcolo di tf e tfidf per ogni keyword
		- costruzione del keyword vector <keyword, weight>
	*/
	
	public static void main(String args[]) throws SQLException, IOException	{
		Map<String, Double> keywordVector;
		//Scanner input = new Scanner(System.in);
		
		// System.out.println("Inserisci il codice dell'articolo: ");
//		int paperid = input.nextInt();	
		int paperid = 237222;
		Paper p = new Paper(paperid);

//		System.out.println("Inserisci il modello per i pesi (TF oppure TFIDF): ");
//		String model = input.next();
		String model = "tf";

			if(model.equalsIgnoreCase("TF")) {
				System.out.println("Modello TF per \"" + p.getTitle() + "\" (" + paperid + "):");
				keywordVector = Weights.key_TF(p.getKeywords());
				for (Map.Entry<String, Double> entry : keywordVector.entrySet()) {
		            System.out.println("<" + entry.getKey() + ",  " + entry.getValue()+">");
		        }
			}			
			else if(model.equalsIgnoreCase("TFIDF")) {
//				//keywordVector = Weights.key_TFIDF(p.getKeywords());
			}			
			else System.out.print("Modello per i pesi ERRATO !!!");			
//			
//			System.out.println("Fine!");
//		input.close();
    }
}
