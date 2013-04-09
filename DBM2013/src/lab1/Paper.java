package lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class Paper {
	private int paperid;
	private String title;
	private int year;
	private String publisher;
	private String paperAbstract;
	
	public Paper(int paperid) {
		Connection conn = null;
		Statement stmt = null;
		String query = "SELECT * FROM papers WHERE paperid = " + paperid;
		ResultSet res = null;
		try 
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dblp", "root", "root");
			stmt = (Statement) conn.createStatement();
			res = stmt.executeQuery(query);
			
			this.paperid = paperid;
			title = res.getString("title");
			year = res.getInt("year");
			publisher = res.getString("publisher");
			paperAbstract = res.getString("paperAbstract");
		} 
		catch (SQLException e) {		
			System.out.println("SQLException");
		}
		
	}

	public int getPaperid() {
		return paperid;
	}

	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPaperAbstract() {
		return paperAbstract;
	}

	public void setPaperAbstract(String paperAbstract) {
		this.paperAbstract = paperAbstract;
	}
	
	
}
