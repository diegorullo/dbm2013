package utils;

import org.junit.Test;

public class IOTest {

	@Test
	public void testReadDocumentTermMatrixFromFile() throws Exception {
		IO.readDocumentTermMatrixFromFile("ciao Stefania");
		Printer.printMatrix(IO.readDocumentTermMatrixFromFile("ciao Stefania"));
	}

}
