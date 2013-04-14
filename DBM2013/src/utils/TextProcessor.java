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

	// calcolo di TF per ogni keyword
//	public static Map<String, Double> key_TF(ArrayList<String> keywords) throws IOException {
//		
//		int n = 0;
//		int K = keywords.size();
//		double tf;
//		HashMap<String, Integer> keywordSet = new HashMap<String, Integer>();
//		Map<String, Double> keywordVectorTF = new TreeMap<String, Double>();
//		
//		for(String k : keywords) {
//			if (!keywordSet.containsKey(k)) {
//				keywordSet.put(k, 1);
//			}
//			else {
//				keywordSet.put(k, keywordSet.get(k) + 1);
//			}
//		} 		//System.out.println(keywordSet);
//		
///*		while (keywords.incrementToken()) {
//			K++;
//			currentKeyword = charTermAttribute.toString();
//			if (!keywordSet.containsKey(currentKeyword)) {
//				keywordSet.put(currentKeyword, 1);
//			}
//			else {
//				currentValue = keywordSet.get(currentKeyword) + 1;
//				keywordSet.put(currentKeyword, currentValue);
//			}
//			keywordMultiset = keywordMultiset + currentKeyword;
//		} */
//
////		Enumeration<String> enumKey = keywordSet.keys();
////		while (enumKey.hasMoreElements()) {
////			String key = enumKey.nextElement();
////			n = keywordSet.get(key);
////			tf = n / K;
////			keywordVectorTF.put(key, tf);
////		}
//
//		Iterator<Entry<String, Integer>> it = keywordSet.entrySet().iterator();
//		while (it.hasNext()) {
//			Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
//			n = k.getValue();
//			tf = (double)n/K;
//			keywordVectorTF.put(k.getKey(), tf);
//		}
//		
//		return keywordVectorTF;
//	}

//	// calcolo di TFIDF per ogni keyword
//	public static Map<String, Double> key_TFIDF(ArrayList<String> keywords, Map<String, Double> tfVector, Corpus dblp) {
//		Map<String, Double> keywordVectorTFIDF = new TreeMap<String, Double>();
//		
//		int N = dblp.getCardinality();	//Numero totale di documenti del corpus
//		int m = 0;	//Numero di documenti in cui la feature occorre
//		double idf = 0;
//		double tfidf = 0; 
//		
//		for (String k : keywords) {
//			//contare il numero di paper nel db che contengono k
//			dblp.getPapers();
////			for(Paper p : dblp.getPapers()) {
////				if(p.getKeywords().contains(k)) {
////					m++;
////				}
////			}
////			idf = Math.log(m/N);
//			keywordVectorTFIDF.put(k, idf);
//		}
//		
//		return keywordVectorTFIDF;
//	}
}
