package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class TextProcessor {

	private static Scanner input;

	private static Set<String> loadStopWords() throws FileNotFoundException {
		Set<String> stopWords = new HashSet<String>();
		input = new Scanner(new File("resources/english.stop"));
		input.useDelimiter("\n");

		while (input.hasNext()) {
			stopWords.add(input.next());
		}

		return stopWords;
	}

	public static ArrayList<String> removeStopWordsAndStem(String input) throws IOException {

		Set<String> stopWords = loadStopWords();
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
}
