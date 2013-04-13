package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Statement;

public class DBEngine {
	private static Connection conn;
	private static Statement stmt;

	public DBEngine() {
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dblp", "root", "root");
			stmt = (Statement) conn.createStatement();
		} catch (SQLException e) {
			System.out.println("SQLException: cannot create DBEngine");
		}
	}
	
	
}
