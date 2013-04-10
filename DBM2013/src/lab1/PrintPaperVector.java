package lab1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;


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
	


	public static void main(String args[]) throws SQLException, FileNotFoundException
	{
		
		Scanner input = new Scanner(System.in);

		
		System.out.println("Inserisci il codice dell'articolo: ");
		int paperid = input.nextInt();
		
		Paper p = new Paper(paperid);
		
		try {
			System.out.println(p.getYear());
			System.out.println(Weights.removeStopWordsAndStem(p.getTitle()));
		} catch (IOException e) {
			System.out.println("IOException");
		}
		
		//System.out.println("Inserisci il modello per i pesi (TF oppure TFID): ");
		//String model = input.next();
		
		/*
		while(true)
		{
		
		System.out.println("Inserisci il codice dell'articolo: ");
		int paperid = input.nextInt();
		
		System.out.println("Inserisci il modello per i pesi (TF oppure TFID): ");
		String model = input.next();
		
		ResultSet res = queryDB (paperid);
			
			
			if(model.equalsIgnoreCase("TF"))
			{
				key_TF (res);
			}
			
			else if(model.equalsIgnoreCase("TFID"))
			{
				key_TFID (res);
			}
			
			else System.out.print("Modello per i pesi ERRATO !!!");
			
			while (res.next()) {
				String name = res.getString("name");
				System.out.println("Hello, " + name + "!");
			}	
		}*/
		input.close();
    }
}
