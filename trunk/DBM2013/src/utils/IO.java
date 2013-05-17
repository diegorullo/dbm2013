package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class IO {

	// FIXME
	final static String documentTermMatrixFilePath = "../data/X.csv";

	/**
	 * FIXME: stampa su file, percorso e nome statici, della matrice
	 * DocumentTermMatrix
	 * 
	 * @param documentTermMatrix
	 *            matrice
	 * @param path
	 *            path della destinazione
	 * @throws Exception
	 */
	public static void printDocumentTermMatrixOnFile(
			ArrayList<TreeMap<String, Double>> documentTermMatrix, String path)
			throws Exception {
		try {
			// FileOutputStream file = new FileOutputStream(path);
			FileOutputStream file = new FileOutputStream(
					documentTermMatrixFilePath);
			PrintStream Output = new PrintStream(file);

			for (TreeMap<String, Double> riga : documentTermMatrix) {
				int i = 0;
				for (Map.Entry<String, Double> cella : riga.entrySet()) {
					Output.print(cella.getValue());
					i++;
					if (i < riga.size()) {
						Output.print(",");
					}
				}
				Output.print("\n");
			}
			Output.close();
		} catch (IOException e) {
			System.out.println("Errore: " + e);
			System.exit(1);
		}

	}

	/**
	 * FIXME: legge da file, percorso e nome statici, la matrice
	 * DocumentTermMatrix
	 * 
	 * @param documentTermMatrix
	 *            matrice
	 * @param path
	 *            path della sorgente
	 * @throws Exception
	 */
	public static ArrayList<TreeMap<String, Double>> readDocumentTermMatrixFromFile(
			String path) throws Exception {
		ArrayList<TreeMap<String, Double>> documentTermMatrix = new ArrayList<TreeMap<String, Double>>();
		try {
			// FileInputStream file = new FileOutputStream(path);
			FileInputStream file = new FileInputStream(
					documentTermMatrixFilePath);

			Scanner sc = new Scanner(file);
			sc.useDelimiter(",");
			while (sc.hasNext()) {
				System.out.println(sc.next());
			}
			sc.close();

			// for(TreeMap<String, Double> riga : documentTermMatrix) {
			// int i=0;
			// for(Map.Entry<String, Double> cella : riga.entrySet()) {
			// Output.print(cella.getValue());
			// i++;
			// if (i<riga.size()){
			// Output.print(",");
			// }
			// }
			// Output.print("\n");
			// }
			// Output.close();
		} catch (IOException e) {
			System.out.println("Errore: " + e);
			System.exit(1);
		}

		return documentTermMatrix;

	}
}
