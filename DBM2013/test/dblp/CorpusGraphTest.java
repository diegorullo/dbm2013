package dblp;

import java.io.IOException;
import java.sql.SQLException;

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

//	@Test
//	public void testSelectNextLeader() throws NoAuthorsWithSuchIDException {
//		if(DEBUG) {
//			
//			int clusterCount = 4;
//			
//			Author firstLeader = dblp.getAuthorByID(1768163);
//			Author[] currentLeaders = new Author[clusterCount];
//			currentLeaders[0] = firstLeader;
//			
//			Author nextLeader = dblp.selectNextLeader(currentLeaders, clusterCount);
//			
//			if(PRINT) {
//				System.out.println("Next leader: " + nextLeader.getName() + " (" + nextLeader.getAuthorID() + ")");
//			}
//		}
//	}
//	
//	@Test
//	public void testClusterAuthorsByMaxAMin() throws NoAuthorsWithSuchIDException {
//		if(!DEBUG) {
//			
//			int clusterCount = 4;
//			
//			HashMap<Integer, Integer> clusters = dblp.clusterAuthorsByMaxAMin(clusterCount);
//			
//			if(PRINT) {
//				System.out.println(clusters);
//			}
//		}
//	}
	
	@Test
	public void testPageRank() throws NoAuthorsWithSuchIDException {
		if(DEBUG) {
			Node[] rankedNodes;
	
			Graph coAuthorsGraph = dblp.getCoAuthorsGraphBasedOnKeywordVectors();
			int k = 10;
			
			rankedNodes = dblp.pageRank(coAuthorsGraph, k);
			
			System.out.println("I " + k + " autori più dominanti sono:");
			for(int i = 0; i < k; i++) {
				System.out.println("[" + (i+1) + "] " +  dblp.getAuthorByID(Integer.parseInt(rankedNodes[i].getNodeData().getLabel())).getName()  + " (" + rankedNodes[i].toString() + ")");
			}
		}
	}

}
