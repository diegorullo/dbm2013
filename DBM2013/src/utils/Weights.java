package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class Weights {
	
	private static Scanner input;

	private static Set<String> caricaStopWord() throws FileNotFoundException
	{
		
		Set<String> stopWords = new HashSet<String>();
		input = new Scanner(new File ("resources/english.stop"));
		input.useDelimiter("\n");

		while(input.hasNext()) 
		{
			stopWords.add(input.next());
		}
		
		return stopWords;
	}
	
	public static TokenStream removeStopWordsAndStem(String input) throws IOException {

		Set<String> stopWords = caricaStopWord();
		String lowerCasedInput = input.toLowerCase(); 
		TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_36, new StringReader(lowerCasedInput));
	    tokenStream = new StopFilter(Version.LUCENE_36, tokenStream, stopWords);
	    tokenStream = new PorterStemFilter(tokenStream);
	    
	    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

	    String out = ""; 
	    
	    while(tokenStream.incrementToken()) {
	        out = out + " " + charTermAttribute.toString();
	    }
	    
	    tokenStream.close();
	    
	    return tokenStream;
	}
	
		// calcolo di TF per ogni keyword
		public static TreeMap<String, Double> key_TF(TokenStream tokenStream) throws IOException
		{
			TreeMap<String, Double> keywordVectorTF = new TreeMap<String, Double>();
			CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
			int n = 0;
			int K = 0;
			double tf;
			String currentKeyword;
			int currentValue;
			String keywordMultiset = "";
			Hashtable<String, Integer> keywordSet = new Hashtable<String, Integer>();
			
			while(tokenStream.incrementToken()) {
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
			while(enumKey.hasMoreElements()) {
			    String key = enumKey.nextElement();
			    n = keywordSet.get(key);
			    tf = n/K;
			    keywordVectorTF.put(key, tf);
			}
			
			return keywordVectorTF;
		}
		
		// calcolo di TFID per ogni keyword
		public static TreeMap<String, Double> key_TFIDF(TokenStream tokenStream)
		{
			TreeMap<String, Double> keywordVectorTFIDF = null;
			CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
			
			return keywordVectorTFIDF;
		}
}
