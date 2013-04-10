package lab1;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

import com.mysql.jdbc.Statement;

public class PrintPaperVector 
{
	
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
	
	private static Set<String> caricaStopWord() throws FileNotFoundException
	{
		
		Set<String> stopWords = new HashSet<String>();
		Scanner input = new Scanner(new File ("resources/english.stop"));
		input.useDelimiter("\n");

		while(input.hasNext()) 
		{
			stopWords.add(input.next());
		}
		
		return stopWords;
	}
	
	public static String removeStopWordsAndStem(Set<String> stopWords,String input) throws IOException {

		String lowerCasedInput = input.toLowerCase(); 
		TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_36, new StringReader(lowerCasedInput));
	    tokenStream = new StopFilter(Version.LUCENE_36, tokenStream, stopWords);
	    tokenStream = new PorterStemFilter(tokenStream);
	    
	    //TokenStream tokenStream = analyzer.tokenStream(fieldName, reader);
	    OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
	    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

	    String out = ""; 
	    
	    while (tokenStream.incrementToken()) {
	        int startOffset = offsetAttribute.startOffset();
	        int endOffset = offsetAttribute.endOffset();
	        out = out + " " + charTermAttribute.toString();
	    }
	    
	    return out;

	    /*StringBuilder sb = new StringBuilder();
	    TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
	    while (tokenStream.incrementToken()) {
	        if (sb.length() > 0) {
	            sb.append(" ");
	        }
	        sb.append(termAttr.term());
	    }*/
	    //return sb.toString();
	}
	
	// calcolo di TF per ogni keyword
	private static void key_TF (ResultSet paper)
	{}
	
	// calcolo di TFID per ogni keyword
	private static void key_TFID (ResultSet paper)
	{}

	public static void main(String args[]) throws SQLException, FileNotFoundException
	{
		
		Scanner input = new Scanner(System.in);
		Set<String> stopWords = caricaStopWord();
		
		System.out.println("Inserisci il codice dell'articolo: ");
		int paperid = input.nextInt();
		
		Paper p = new Paper(paperid);
		
		try {
			System.out.println(p.getYear());
			System.out.println(removeStopWordsAndStem(stopWords, p.getTitle()));
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
    }
}
