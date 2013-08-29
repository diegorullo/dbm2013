package utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

import org.junit.BeforeClass;
import org.junit.Test;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import dblp.Author;
import dblp.Corpus;
import dblp.Factory;
import exceptions.AuthorWithoutPapersException;
import exceptions.NoAuthorsWithSuchIDException;

public class ClustererTest {

	static Corpus dblp;
	static Clusterer clusterer;

	private final static boolean DEBUG = true;
	private final static boolean PRINT = true;
	
	@BeforeClass	
	public static void getEnvironment() throws SQLException, MatlabConnectionException, MatlabInvocationException, IOException {
		Factory f = new Factory();
		dblp = f.getCorpus();
		
		clusterer = new Clusterer();
	}
	
	@Test
	public void testCreateInstance() throws NoAuthorsWithSuchIDException, IOException, AuthorWithoutPapersException {
		//Creiamo
		
		if (DEBUG) {			
			//"Maria Luisa Sapino"
			Author authorSapino = dblp.getAuthorByID(1677020);
								
			Instance i = clusterer.createInstance(authorSapino, dblp);
			if (PRINT) {			
				System.out.println("Instance (Sapino - 1677020): " + i);
				System.out.println("----------------------------------\n\n");
			}
		}
	}
	
	@Test
	public void testCreateAttributes() {
		if (DEBUG) {								
			ArrayList<Attribute> attributeList = clusterer.createAttributes(dblp);
			
			System.out.println("Attributes (" + attributeList.size() + "): ");
			if (PRINT) {
				for(Attribute a : attributeList) {
					System.out.println(a.toString());
				}
				System.out.println("----------------------------------\n\n");
			}
		}
	}
	
	@Test
	public void testCreateInstances() throws IOException, AuthorWithoutPapersException {
		if (DEBUG) {								
			Instances instances = clusterer.createInstances(dblp);
			
			System.out.println("Instances (" + instances.size() + "): ");
			if (PRINT) {
				for(Instance i : instances) {
					System.out.println(i.toString());
				}
				System.out.println("----------------------------------\n\n");
			}			
		}
	}
	
	@Test
	public void testClusterer() throws IOException, AuthorWithoutPapersException {
		if (DEBUG) {			
			SimpleKMeans sKM = clusterer.getSimpleKMeans();
			int numClusters = 10;
			
			sKM.setSeed(numClusters * 2);
			sKM.setPreserveInstancesOrder(true);
			
			try {
				sKM.setNumClusters(numClusters);
			} catch (Exception e1) {
				System.err.println("Error setting the number of clusters!");
				e1.printStackTrace();
			}
			
			Instances instances = clusterer.createInstances(dblp);
			
			try {
				sKM.buildClusterer(instances);
			} catch (Exception e) {
				System.err.println("Error building the clusterer!");
				e.printStackTrace();
			}
			
//			for(String s: sKM.getOptions()) {
//				System.out.println("sKM option: " + s);
//			}
				
			if (PRINT) {
				for (Instance anInstance : instances) {
					// Uso l'ultimo attributo (contiene l'authorID) come nome dell'istanza
					int lastAttribute = anInstance.numAttributes() - 1;
					try {
						System.out.println("i: " + (int)anInstance.value(lastAttribute) + " c: " + sKM.clusterInstance(anInstance));
					} catch (Exception e) {
						System.err.println("Error clustering the instance " + anInstance + "!");
						e.printStackTrace();
					}
				}
				System.out.println("----------------------------------\n\n");
			}
		}
	}
	
	@Test
	public void testClusterAuthorsBySimpleKMeans() throws IOException, AuthorWithoutPapersException, NoAuthorsWithSuchIDException {		
		if (DEBUG) {
			int numClusters = 11; 

			Map<Integer, Integer> clusters = clusterer.clusterAuthorsBySimpleKMeans(dblp, numClusters);
			if (PRINT) {
				System.out.println("Clustering degli autori tramite SimpleKMeans:\n");
				Set<Entry<Integer, Integer>> clustersEntrySet = clusters.entrySet();
				for(Entry<Integer, Integer> clusteredAuthor: clustersEntrySet) {
					int authorID = clusteredAuthor.getKey();
					System.out.println(dblp.getAuthorByID(authorID).getName() + " (" + authorID + ") > cluster " + clusteredAuthor.getValue());
				}
			}
		}
	}
	
	@Test
	public void testClusterAuthorsByFarthestFirst() throws IOException, AuthorWithoutPapersException, NoAuthorsWithSuchIDException {		
		if (DEBUG) {
			int numClusters = 11; 

			Map<Integer, Integer> clusters = clusterer.clusterAuthorsByFarthestFirst(dblp, numClusters);
			
			if (PRINT) {
				System.out.println("Clustering degli autori tramite FarthestFirst:\n");
				Set<Entry<Integer, Integer>> clustersEntrySet = clusters.entrySet();
				for(Entry<Integer, Integer> clusteredAuthor: clustersEntrySet) {
					int authorID = clusteredAuthor.getKey();
					System.out.println(dblp.getAuthorByID(authorID).getName() + " (" + authorID + ") > cluster " + clusteredAuthor.getValue());
				}
			}
		}
	}
	
	@Test
	public void testPrintInstancesOnCSV() throws AuthorWithoutPapersException {
		if (DEBUG) {			
			clusterer.printInstancesOnCSV(dblp, "instances.csv");
		}
	}
}
