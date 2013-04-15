package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class Printer {

	public static void printPaperTFVector(Map<String, Double> m)
			throws IOException {

		System.out.println("<PRIMA>");
		for (Map.Entry<String, Double> entry : m.entrySet()) {
			System.out.println("<" + entry.getKey() + ", " + entry.getValue()
					+ ">");
		}

		ArrayList<Map.Entry<String, Double>> as = new ArrayList<Map.Entry<String, Double>>(
				m.entrySet());

		Collections.sort(as, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				Double first = (Double) o1.getValue();
				Double second = (Double) o2.getValue();
				return second.compareTo(first);
			}
		});

		System.out.println("<DOPO>");
		for (Entry<String, Double> entry : as) {
			System.out.println("<" + entry.getKey() + "," + entry.getValue()
					+ ">");
		}
	}
}
