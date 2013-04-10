package lab1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.TreeMap;


import utils.Weights;

public class PrintPaperVector 
{
	
	/* Task 1:
	    - tokenizare
		- stemming
		- eliminare le stop words
		- calcolo di tf e tfidf per ogni keyword
		- costruzione del keyword vector <keyword, weight>
	*/
	
	
//	Enumeration<String> enumKey = keywordSet.keys();
//	while(enumKey.hasMoreElements()) {
//	    String key = enumKey.nextElement();
//	    n = keywordSet.get(key);
//	    tf = n/K;
//	    keywordVectorTF.put(key, tf);
//	}

	public static void main(String args[]) throws SQLException, IOException
	{
		TreeMap<String, Double> keywordVector;
		Scanner input = new Scanner(System.in);

		
		System.out.println("Inserisci il codice dell'articolo: ");
		int paperid = input.nextInt();		
		Paper p = new Paper(paperid);

		System.out.println("Inserisci il modello per i pesi (TF oppure TFIDF): ");
		String model = input.next();
		
		
		//while(true)
			
			
			if(model.equalsIgnoreCase("TF"))
			{
				keywordVector = Weights.key_TF(p.getKeywords());
			}
			
			else if(model.equalsIgnoreCase("TFIDF"))
			{
				//keywordVector = Weights.key_TFIDF(p.getKeywords());
			}
			
			else System.out.print("Modello per i pesi ERRATO !!!");
			
			
			System.out.println("Fine!");
		input.close();
    }
}
