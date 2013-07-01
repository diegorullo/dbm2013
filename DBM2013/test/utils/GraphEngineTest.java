package utils;

import java.io.IOException;
import java.sql.SQLException;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Node;
import org.junit.Test;

import dblp.Author;
import dblp.Corpus;
import dblp.Factory;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

public class GraphEngineTest {

	@Test
	public void testGraphEngine() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException {
		
		GraphEngine ge = GraphEngine.getGraphEngine();
		
        Factory factory = new Factory();
        Corpus dblp = factory.getCorpus();
        
//      Node n0 = graphModel.factory().newNode("n0");
//      n0.getNodeData().setLabel("Node 0");
//      
//      Node n1 = graphModel.factory().newNode("n1");
//      n1.getNodeData().setLabel("Node 1");
//      //Create an edge - directed and weight 1
//      Edge e1 = graphModel.factory().newEdge(n1, n0, 1f, true);
//      //Append as a Directed Graph
//      DirectedGraph directedGraph = graphModel.getDirectedGraph();
//      directedGraph.addNode(n0);
//      directedGraph.addNode(n1);
//      directedGraph.addEdge(e1);
        
//      AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
        
        DirectedGraph directedGraph = ge.getGraphModel().getDirectedGraph();
        for (Author a : dblp.getAuthors()) {
        	String authorID = a.getAuthorID().toString();
        	Node n = ge.getGraphModel().factory().newNode(authorID);
        	n.getNodeData().setLabel(authorID);
        	directedGraph.addNode(n);        	
        }
        
        for (Author a : dblp.getAuthors()) {
        	String authorID = a.getAuthorID().toString();
        	Node n = ge.getGraphModel().factory().newNode(authorID);
        	n.getNodeData().setLabel(authorID);
        	directedGraph.addNode(n);        	
        }
        
        
        //Count nodes and edges
        System.out.println("Nodes: "+directedGraph.getNodeCount()+" Edges: "+directedGraph.getEdgeCount());
        //Iterate over nodes
        for(Node n : directedGraph.getNodes()) {
        Node[] coauthors = directedGraph.getNeighbors(n).toArray();
        System.out.println(n.getNodeData().getLabel()+" has "+coauthors.length+" coauthors");
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
