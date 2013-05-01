package dblp;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.DBEngine;

public class PaperTest {

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
	public void testGetKeywordSet() throws SQLException, IOException {		
		int paperid = 943390;
		Paper paper = db.newPaper(paperid);
		Map<String, Integer> ks = paper.getKeywordSet();
		org.junit.Assert.assertNotNull("keywordset paper "+ paperid + "creato correttamente", ks);
		
//FIXME: se il paper non ha titolo nè abstract, ks è nullo 
//		e l'asserzione fallisce...verificare
		
	}

	@Test
	public void testGetTF() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTFIDF() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTFVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTFIDFVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAge() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWeightBasedOnAge() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWTF() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWTFVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWTFIDFVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsKeyword() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
//
//		Author a = db.newAuthor(2390072);
//		
//		Paper p = 
		fail("Not yet implemented");
	}

}
