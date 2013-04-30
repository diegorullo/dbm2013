package dblp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.DBEngine;

public class PaperTest {

	@BeforeClass
	public static void testSetup() {
		DBEngine db = new DBEngine();
		try {
			db.init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Corpus dblp = db.newCorpus();
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void testCleanup() {
		// Teardown for data used by the unit tests
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
//		Paper p = fail("Not yet implemented");
	}

}
