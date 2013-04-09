package lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.jdbc.Statement;

public class PrintPaperVector {
	
	/* Task 1:
	    - tokenizare
		- stemming
		- eliminare le stop words
		- calcolo di tf e tfidf per ogni keyword
		- costruzione del keyword vector <keyword, weight>
		
		BLA BLA BLA !!!
	*/
	
	public static void main(String args[]) throws SQLException
	{
		while(true) {
		Scanner input = new Scanner(System.in);
		System.out.print("Inserisci il codice autore: ");
		int personid = input.nextInt();
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dblp", "root", "root");
			Statement stmt = (Statement) conn.createStatement();
			String query = "SELECT name FROM authors WHERE personid = " + personid;
			ResultSet res = stmt.executeQuery(query);
			while (res.next()) {
				String name = res.getString("name");
				System.out.println("Hello, " + name + "!");
			}		
		}
    }

	
}
