package utils;

import java.util.ArrayList;

import org.junit.Test;

public class IOTest {

	@Test
	public void testReadDocumentTermMatrixFromFileDummy() throws Exception {
		ArrayList<ArrayList<Double>> matrix_x = IO.readDocumentTermMatrixFromFile("../data/X.csv");
		Printer.printMatrix(matrix_x);
	}
	
}
