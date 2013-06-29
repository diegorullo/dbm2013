package utils;

import java.io.IOException;
import java.sql.SQLException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import dblp.Author;
import dblp.Corpus;
import dblp.Factory;

public class GraphEngine {
	public static void initialize () throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException {
		//Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		@SuppressWarnings("unused")
		Workspace workspace = pc.getCurrentWorkspace();

        //Get controllers and models
//        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
//      AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
        
        Factory factory = new Factory();
        Corpus dblp = factory.getCorpus();
        
        DirectedGraph directedGraph = graphModel.getDirectedGraph();
        for (Author a : dblp.getAuthors()) {
        	String authorID = a.getAuthorID().toString();
        	Node n = graphModel.factory().newNode(authorID);
        	n.getNodeData().setLabel(authorID);
        	directedGraph.addNode(n);        	
        }
        
//        Node n0 = graphModel.factory().newNode("n0");
//        n0.getNodeData().setLabel("Node 0");
//        
//        Node n1 = graphModel.factory().newNode("n1");
//        n1.getNodeData().setLabel("Node 1");
//        //Create an edge - directed and weight 1
//        Edge e1 = graphModel.factory().newEdge(n1, n0, 1f, true);
//        //Append as a Directed Graph
//        DirectedGraph directedGraph = graphModel.getDirectedGraph();
//        directedGraph.addNode(n0);
//        directedGraph.addNode(n1);
//        directedGraph.addEdge(e1);
        
      //Count nodes and edges
        System.out.println("Nodes: "+directedGraph.getNodeCount()+" Edges: "+directedGraph.getEdgeCount());
        //Iterate over nodes
        for(Node n : directedGraph.getNodes()) {
        Node[] neighbors = directedGraph.getNeighbors(n).toArray();
        System.out.println(n.getNodeData().getLabel()+" has "+neighbors.length+" neighbors");
        }
        
        
//        //Find node by id
//        Node node2 = directedGraph.getNode("n2");
//        //Modify the graph while reading
//        //Due to locking, you need to use toArray() on Iterable to be able to modify the graph in a read loop
//        for(Node n : directedGraph.getNodes().toArray()) {
//        	directedGraph.removeNode(n);
//        }

	}
}
