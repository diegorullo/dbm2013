package utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class IOTest {

	@Test
	public void testReadDocumentTermMatrixFromFile() throws Exception {
		IO.readDocumentTermMatrixFromFile("ciao Stefania");
	}

}
