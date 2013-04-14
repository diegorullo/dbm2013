package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import lab1.Paper;

import com.mysql.jdbc.Statement;

public class DBEngine {
	private static Connection conn;
	private static Statement stmt;
	private static final String dbAddress = "jdbc:mysql://localhost:3306/dblp";
	private static final String username = "root";
	private static final String password = "root";

	public void init() throws SQLException {
		if (conn != null)
			conn = DriverManager.getConnection(dbAddress, username, password);
	}
	
	public Paper newPaper(String paperID) throws SQLException {
		String query = "SELECT authors.name,papers.* FROM authors,papers,writtenby WHERE papers.paperid = "
				+ paperID
				+ " AND writtenby.personid=authors.personid AND writtenby.paperid=papers.paperid;";
		stmt = (Statement) conn.createStatement();
		stmt.executeQuery(query);
	
		return null;
	}
	
	public void shutdown() throws SQLException{
		if(conn!=null)
			conn.close();
	}
	
	public class Main{
		DBEngine engine = new DBEngine();
		
		public void doSomething(){
			try {
				Paper p = this.engine.newPaper(null);
			} catch (SQLException e) {
				System.err.println("Dannazione!");
			}
		}
		
	}

}