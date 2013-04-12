package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import lab1.Paper;

import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import com.mysql.jdbc.Statement;

public class Weights {

	private static Scanner input;

	private static Set<String> caricaStopWord() throws FileNotFoundException {
		Set<String> stopWords = new HashSet<String>();
		input = new Scanner(new File("resources/english.stop"));
		input.useDelimiter("\n");

		while (input.hasNext()) {
			stopWords.add(input.next());
		}

		return stopWords;
	}

	public static ArrayList<String> removeStopWordsAndStem(String input) throws IOException {

		Set<String> stopWords = caricaStopWord();
		String lowerCasedInput = input.toLowerCase();
		TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_36,
				new StringReader(lowerCasedInput));
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

		tokenStream = new StopFilter(Version.LUCENE_36, tokenStream, stopWords);
		tokenStream = new PorterStemFilter(tokenStream);
		ArrayList<String> keywords = new ArrayList<String>();
		
		while (tokenStream.incrementToken()) 
		{			
			keywords.add(charTermAttribute.toString());
		}
		tokenStream.close();
		return keywords;
	}

	// calcolo di TF per ogni keyword
	public static Map<String, Double> key_TF(TokenStream tokenStream) throws IOException {
		
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		Map<String, Double> keywordVectorTF = new TreeMap<String, Double>();
		int n = 0;
		int K = 0;
		double tf;
		String currentKeyword;
		int currentValue;
		String keywordMultiset = "";
		Hashtable<String, Integer> keywordSet = new Hashtable<String, Integer>();

		while (tokenStream.incrementToken()) {
			K++;
			currentKeyword = charTermAttribute.toString();
			if (!keywordSet.containsKey(currentKeyword)) {
				keywordSet.put(currentKeyword, 1);
			}
			else {
				currentValue = keywordSet.get(currentKeyword) + 1;
				keywordSet.put(currentKeyword, currentValue);
			}
			keywordMultiset = keywordMultiset + currentKeyword;
		}

		Enumeration<String> enumKey = keywordSet.keys();
		while (enumKey.hasMoreElements()) {
			String key = enumKey.nextElement();
			n = keywordSet.get(key);
			tf = n / K;
			keywordVectorTF.put(key, tf);
		}

		return keywordVectorTF;
	}

	// calcolo di TFID per ogni keyword
	public static Map<String, Double> key_TFIDF(TokenStream tokenStream) {
		Map<String, Double> keywordVectorTFIDF = null;
		int cardDB=0;
		// CharTermAttribute charTermAttribute =
		// tokenStream.addAttribute(CharTermAttribute.class);
		
		Connection conn = null;
		Statement stmt = null;
		String query = "SELECT COUNT(*) FROM papers;";
		ResultSet res = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dblp", "root", "root");
			stmt = (Statement) conn.createStatement();
			
			res = stmt.executeQuery(query);
			
			res.next();
			cardDB=res.getInt(1);
		
		} catch (SQLException e) {
			System.out.println("SQLException CIAO");
		}

		int paperid = 237222;
		Paper p = new Paper(paperid);
		for (String k : p.getKeywords()) {
			//contare il numero di paper nel db che contengono k
			
		}
		
		return keywordVectorTFIDF;
	}
}
