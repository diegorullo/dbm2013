package lab1;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

import com.mysql.jdbc.Statement;

public class PrintPaperVector 
{
	
	private static Scanner input;

	/* Task 1:
	    - tokenizare
		- stemming
		- eliminare le stop words
		- calcolo di tf e tfidf per ogni keyword
		- costruzione del keyword vector <keyword, weight>
	*/
	
	private static ResultSet queryDB (int paperid) 
	{
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dblp", "root", "root");
		} 
		catch (SQLException e) 
		{
			System.out.println("SQLException : Impossibile connettersi al DB");
		}
		Statement stmt = null;
		try 
		{
			stmt = (Statement) conn.createStatement();
		} 
		catch (SQLException e) 
		{
			System.out.println("SQLException : Impossibile creare la query");
		}
		
		String query = "SELECT abstract FROM papers WHERE paperid = " + paperid;
		ResultSet res = null;
		try {
			res = stmt.executeQuery(query);
		} 
		catch (SQLException e) 
		{
			System.out.println("SQLException : Impossibile eseguire la query");
		}
		
		return res;
	}
	
	public static String removeStopWordsAndStem(String input) throws IOException {
	    Set<String> stopWords = new HashSet<String>();
	    stopWords.add("a");
	    stopWords.add("I");
	    stopWords.add("the");

	    TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_36, new StringReader(input));
	    tokenStream = new PorterStemFilter(tokenStream);
	    tokenStream = new StopFilter(Version.LUCENE_36, tokenStream, stopWords);

	    StringBuilder sb = new StringBuilder();
	    TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
	    while (tokenStream.incrementToken()) {
	        if (sb.length() > 0) {
	            sb.append(" ");
	        }
	        sb.append(termAttr.term());
	    }
	    return sb.toString();
	}
	
	// calcolo di TF per ogni keyword
	private static void key_TF (ResultSet paper)
	{}
	
	// calcolo di TFID per ogni keyword
	private static void key_TFID (ResultSet paper)
	{}

	public static void main(String args[]) throws SQLException
	{
		input = new Scanner(System.in);
		
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
			
			/*while (res.next()) {
				String name = res.getString("name");
				System.out.println("Hello, " + name + "!");
			}*/		
		}
    }
}
