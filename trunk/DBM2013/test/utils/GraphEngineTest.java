package utils;

import java.io.IOException;
import java.sql.SQLException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.junit.Test;

import dblp.Corpus;
import dblp.Factory;
import exceptions.NoAuthorsWithSuchIDException;

public class GraphEngineTest {

	@Test
	public void testGetCoAuthorsGraphBasedOnKeywordVectors() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException {
		Factory factory = new Factory();
        Corpus dblp = factory.getCorpus();
        
        Graph graph = dblp.getCoAuthorsGraphBasedOnKeywordVectors();
        
        //Count nodes and edges
        System.out.println("Nodes: " + graph.getNodeCount() + " Edges: " + graph.getEdgeCount());
        //Iterate over nodes
        for(Node n : graph.getNodes()) {
	        Node[] coauthors = graph.getNeighbors(n).toArray();
	        System.out.println(n.getNodeData().getLabel() + " has "+coauthors.length + " coauthors");
        }
        
//      //Find node by id
//      Node node2 = directedGraph.getNode("n2");
//      //Modify the graph while reading
//      //Due to locking, you need to use toArray() on Iterable to be able to modify the graph in a read loop
//      for(Node n : directedGraph.getNodes().toArray()) {
//      	directedGraph.removeNode(n);
//      }
	}
}
