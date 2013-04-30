package dblp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.DBEngine;

public class PaperTestAdvanced {
	
	static DBEngine db = new DBEngine();
	@BeforeClass
	public static void testSetup() throws SQLException {
		db.init();
	}

	@AfterClass
	public static void testCleanup() throws SQLException {
		db.shutdown();
	}
	
	@Test
	public void testTFAUnoDummy() {
		ArrayList<String> authorsNames = new ArrayList<String>();
		authorsNames.add("Stefania");
		ArrayList<Integer> authors = new ArrayList<Integer>();
		authors.add(2013);
		ArrayList<String> keywords = new ArrayList<String>();
		keywords.add("media");
		keywords.add("media");
		keywords.add("keyword");
		keywords.add("keyword");
		keywords.add("keyword");
		ArrayList<String> titlesKeywords = new ArrayList<String>();
		titlesKeywords.add("testare");
		titlesKeywords.add("tf");
		titlesKeywords.add("uno");
		Paper paper = new Paper(1, "Testare i TF a uno", 2013, "Gruppo DBM DLS", "media media keyword keyword keyword", authorsNames, authors, keywords, titlesKeywords);
		ArrayList<Paper> paperList = new ArrayList<Paper>();
		paperList.add(paper);
		Author a = new Author(2013, "Stefania", paperList);
		
		
		for (Paper p : a.getPapers()) {
			//System.out.println("#kw = " + p.getKeywordSet().size() + "  " + p);
			double uno = 0.0;
			Map<String, Integer> ks = p.getKeywordSet();
			Iterator<Entry<String, Integer>> it = ks.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
				//System.out.println(p.getTF(k.getKey()));
				uno += p.getTF(k.getKey());
			}
			assertEquals("La somma dei tf per le varie keyword vale 1.", 1.0, uno, 1/1000000);
			//System.out.println("1 = " + uno);
		}
		
		for (Paper p1 : a.getPapers()) {
			try {
				p1.getTFVector();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void testTFAUno() throws SQLException, IOException {
		Paper paper = db.newPaper(1279177);

			double uno = 0.0;
			Map<String, Integer> ks = paper.getKeywordSet();
			Iterator<Entry<String, Integer>> it = ks.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, Integer> k = (Map.Entry<String, Integer>) it.next();
				uno += paper.getTF(k.getKey());
			}
			assertEquals("La somma dei tf per le varie keyword vale 1.", 1.0, uno, 0);
			
			paper.getTFVector();
	}
}
