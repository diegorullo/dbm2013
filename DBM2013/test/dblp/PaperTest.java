package dblp;

import static org.junit.Assert.fail;

import java.sql.SQLException;

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
	public void testGetKeywordSet() {
		fail("Not yet implemented");
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
