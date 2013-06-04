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

	/**
	 * FIXME: stampa su file, percorso e nome statici, della matrice
	 * DocumentTermMatrix
	 * 
	 * @param documentTermMatrix    matrice
	 * @param path		path della destinazione
	 * @throws Exception
	 */
	public static void printDocumentTermMatrixOnFile(
			ArrayList<TreeMap<String, Double>> documentTermMatrix, String path)
			throws Exception {
		try {
			FileOutputStream file = new FileOutputStream(path);
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
	 * FIXME: legge da file, percorso e nome statici, la matrice DocumentTermMatrix
	 * @param path		path della sorgente
	 * @throws Exception
	 */
	public static ArrayList<ArrayList<Double>> readDocumentTermMatrixFromFile(String path) throws Exception {
		ArrayList<ArrayList<Double>> documentTermMatrix = new ArrayList<ArrayList<Double>>();
		
		try {
			FileInputStream file = new FileInputStream(path);

			Scanner sc = new Scanner(file);
			
			String val;
			String[] elem ;
			while (sc.hasNextLine()) 
			{
				val = sc.nextLine();
				elem = val.split(",");
				ArrayList<Double> riga = new ArrayList<Double>();
				for(String v : elem)
				{
					riga.add(Double.valueOf(v));
				}
				documentTermMatrix.add(riga);

			}
			sc.close();
			
//			String csvFilename = documentTermMatrixFilePath;
//			CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
//			String[] row = null;
//			
//			while((row = csvReader.readNext()) != null) {
//				System.out.println(row[0]);
//				
//			}
//			csvReader.close();

		} catch (IOException e) {
			System.out.println("Errore: " + e);
			System.exit(1);
		}

		return documentTermMatrix;

	}
	
	/**
	 * FIXME: legge da file, percorso e nome statici, la matrice
	 * DocumentTermMatrix
	 * 
	 * @param path		path della sorgente
	 * @param nTop	numero di righe della matrice da leggere
	 * @throws Exception
	 */
	public static ArrayList<ArrayList<Double>> readNTOPDocumentTermMatrixFromFile(String path,int nTop) throws Exception {
		ArrayList<ArrayList<Double>> documentTermMatrix = new ArrayList<ArrayList<Double>>();
		
		try {
			FileInputStream file = new FileInputStream(path);

			Scanner sc = new Scanner(file);
			
			String val;
			String[] elem ;
			int n=0;
			while (sc.hasNextLine() && n < nTop) 
			{
				val = sc.nextLine();
				elem = val.split(",");
				ArrayList<Double> riga = new ArrayList<Double>();
				for(String v : elem)
				{
					riga.add(Double.valueOf(v));
				}
				documentTermMatrix.add(riga);
				n++;
			}
			sc.close();

		} catch (IOException e) {
			System.out.println("Errore: " + e);
			System.exit(1);
		}

		return documentTermMatrix;

	}
}
