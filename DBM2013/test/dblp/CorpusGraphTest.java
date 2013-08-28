package dblp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.NoAuthorsWithSuchIDException;

public class CorpusGraphTest {

	static Corpus dblp;

	private final static boolean DEBUG = true;
	@SuppressWarnings("unused")
	private final static boolean PRINT = true;
	
	@BeforeClass
	public static void getEnvironment() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException {
		Factory f = new Factory();
		dblp = f.getCorpus();
	}

	@Test
	public void testPageRank() throws NoAuthorsWithSuchIDException {
		if(DEBUG) {
			Node[] rankedNodes;
	
			Graph coAuthorsGraph = dblp.getCoAuthorsGraphBasedOnKeywordVectors();
			int k = 15;
			
			rankedNodes = dblp.pageRank(coAuthorsGraph, k);
			
			System.out.println("I " + k + " autori più dominanti sono:");
			for(int i = 0; i < k; i++) {
				System.out.println("[" + (i+1) + "] " +  dblp.getAuthorByID(Integer.parseInt(rankedNodes[i].getNodeData().getLabel())).getName()  + " (" + rankedNodes[i].toString() + ")");
			}
		}
	}
	
	@Test
	public void testGetKMostSimilarAuthors() throws NoAuthorsWithSuchIDException {
		if(DEBUG) {		
			int k = 10;
			int id = 1636579;
			Author author = dblp.getAuthorByID(id);
			Graph graph = dblp.getCoAuthorsGraphBasedOnKeywordVectors();
			
			System.out.println(k + " authors most similar to " + author.getName() + " (" + id + ")");
			Map<String, Double> result = dblp.getKMostSimilarAuthors(graph, author, k);
			
			
			// Stampo gli autori piu' simili in ordine decrescente
			System.out.println("Found " + result.size() + " authors.");
			
			int j = k;
			Set<Entry<String, Double>> resultEntrySet = result.entrySet();
			for(Entry<String, Double> similarAuthor : resultEntrySet) {
				System.out.println((k - j + 1) + ") " + dblp.getAuthorByID(Integer.parseInt(similarAuthor.getKey())).getName() + " [" + similarAuthor.getKey() + "]: " +  similarAuthor.getValue());
				j--;
			}
			
			System.out.println("...aaaaand done!");
		}
	}

}
