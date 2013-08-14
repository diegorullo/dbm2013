package utils;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.junit.Test;
import org.openide.util.Lookup;

import dblp.Corpus;
import dblp.Factory;
import exceptions.NoAuthorsWithSuchIDException;

public class GraphEngineTest {

	private final static boolean DEBUG = true;
	
	@SuppressWarnings("rawtypes") //FIXME: vedere come risolvere i warning
	@Test
	public void testGetCoAuthorsGraphBasedOnKeywordVectors() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException {
		if(!DEBUG) {
			Factory factory = new Factory();
	        Corpus dblp = factory.getCorpus();
	        
	        Graph graph = dblp.getCoAuthorsGraphBasedOnKeywordVectors();
	        
	        //Count nodes and edges
	        System.out.println("Nodes: " + graph.getNodeCount() + " Edges: " + graph.getEdgeCount());
	        //Iterate over nodes
	        for(Node n : graph.getNodes()) {
		        Node[] coauthors = graph.getNeighbors(n).toArray();
		        System.out.println(n.getNodeData().getLabel() + " has " + coauthors.length + " coauthors");
	        }
	        
	//      //Find node by id
	//      Node node2 = directedGraph.getNode("n2");
	//      //Modify the graph while reading
	//      //Due to locking, you need to use toArray() on Iterable to be able to modify the graph in a read loop
	//      for(Node n : directedGraph.getNodes().toArray()) {
	//      	directedGraph.removeNode(n);
	//      }
	        
	        GraphEngine ge = GraphEngine.getGraphEngine();
	        GraphModel graphModel = ge.getGraphModel();
	        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
	
	      RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
	      
	        //Rank color by Degree
	        Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
	        AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
	        
	        colorTransformer.setColors(new Color[]{new Color(0x66CCCC), new Color(0x7EB6FF)});
	        rankingController.transform(degreeRanking, colorTransformer);
	
	        //Get Centrality
	        GraphDistance distance = new GraphDistance();
	        distance.setDirected(true);
	        distance.execute(graphModel, attributeModel);
	
	        //Rank size by centrality
	        AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
	        Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
	        AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
	        sizeTransformer.setMinSize(5);
	        sizeTransformer.setMaxSize(30);
	        rankingController.transform(centralityRanking, sizeTransformer);
	
	        //Rank label size - set a multiplier size
	        Ranking centralityRanking2 = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
	        AbstractSizeTransformer labelSizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.LABEL_SIZE);
	        labelSizeTransformer.setMinSize(1);
	        labelSizeTransformer.setMaxSize(3);
	        rankingController.transform(centralityRanking2,labelSizeTransformer);
	
	        //Set 'show labels' option in Preview - and disable node size influence on text size
	        PreviewModel previewModel = Lookup.getDefault().lookup(PreviewController.class).getModel();
	        previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(new Color(0x7EB6FF)));
	        previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR, new DependantOriginalColor(new Color(0x101010)));
	        
	        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
	        previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE);
	        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.FALSE);
	
	        //Export
	        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
	        try {
	        	File pdfFile = new File("[Phase 3 - Task 1] coAuthorsGraphBasedOnKeywordVectors.pdf");
	        	File gexfFile = new File("[Phase 3 - Task 1] coAuthorsGraphBasedOnKeywordVectors.gexf");
	        	ec.exportFile(pdfFile); //ec.exportFile(new File("[Phase 3 - Task 1] coAuthorsGraphBasedOnKeywordVectors.pdf"));
	        	ec.exportFile(gexfFile);
	            Desktop.getDesktop().open(pdfFile);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            return;
	        }
		}        
	}
	
	@SuppressWarnings("rawtypes") //FIXME: vedere come risolvere i warning
	@Test
	public void testGetCoAuthoredPapersGraphBasedOnKeywordVectors() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException, NoAuthorsWithSuchIDException {
		if(DEBUG) {
			Factory factory = new Factory();
	        Corpus dblp = factory.getCorpus();
	        
	        Graph graph = dblp.getCoAuthoredPapersGraphBasedOnKeywordVectors();
	        System.out.println("ciao");
	        //Count nodes and edges
	        System.out.println("Nodes: " + graph.getNodeCount() + " Edges: " + graph.getEdgeCount());
	        //Iterate over nodes
	        for(Node n : graph.getNodes()) {
		        Node[] coauthors = graph.getNeighbors(n).toArray();
		        System.out.println(n.getNodeData().getLabel() + " has " + coauthors.length + " coauthors");
	        }
	        
	//      //Find node by id
	//      Node node2 = directedGraph.getNode("n2");
	//      //Modify the graph while reading
	//      //Due to locking, you need to use toArray() on Iterable to be able to modify the graph in a read loop
	//      for(Node n : directedGraph.getNodes().toArray()) {
	//      	directedGraph.removeNode(n);
	//      }
	        
	        GraphEngine ge = GraphEngine.getGraphEngine();
	        GraphModel graphModel = ge.getGraphModel();
	        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
	
	      RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
	      
	        //Rank color by Degree
	        Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
	        AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
	        
	        colorTransformer.setColors(new Color[]{new Color(0x66CCCC), new Color(0x7EB6FF)});
	        rankingController.transform(degreeRanking, colorTransformer);
	
	        //Get Centrality
	        GraphDistance distance = new GraphDistance();
	        distance.setDirected(true);
	        distance.execute(graphModel, attributeModel);
	
	        //Rank size by centrality
	        AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
	        Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
	        AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
	        sizeTransformer.setMinSize(5);
	        sizeTransformer.setMaxSize(30);
	        rankingController.transform(centralityRanking, sizeTransformer);
	
	        //Rank label size - set a multiplier size
	        Ranking centralityRanking2 = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
	        AbstractSizeTransformer labelSizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.LABEL_SIZE);
	        labelSizeTransformer.setMinSize(1);
	        labelSizeTransformer.setMaxSize(3);
	        rankingController.transform(centralityRanking2,labelSizeTransformer);
	
	        //Set 'show labels' option in Preview - and disable node size influence on text size
	        PreviewModel previewModel = Lookup.getDefault().lookup(PreviewController.class).getModel();
	        previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(new Color(0x7EB6FF)));
	        previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR, new DependantOriginalColor(new Color(0x101010)));
	        
	        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
	        previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE);
	        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.FALSE);
	
	        //Export
	        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
	        try {
	        	File pdfFile = new File("[Phase 3 - Task 2] coAuthoredPapersGraphBasedOnKeywordVectors.pdf");
	        	File gexfFile = new File("[Phase 3 - Task 2] coAuthoredPapersGraphBasedOnKeywordVectors.gexf");
	        	ec.exportFile(pdfFile); //ec.exportFile(new File("[Phase 3 - Task 2] coAuthoredPapersGraphBasedOnKeywordVectors.pdf"));
	        	ec.exportFile(gexfFile);
	            Desktop.getDesktop().open(pdfFile);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            return;
	        }
		}
	}
}
