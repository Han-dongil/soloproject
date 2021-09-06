package co.hdo.finder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO
{ 

	PreparedStatement psmt;
	Connection conn;
	Statement stmt;
	ResultSet rs;
	String user ="demo";
	String pass ="demo";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	void connect()
	{
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "demo", "demo");
		} catch (ClassNotFoundException | SQLException e) 
		{
			e.printStackTrace();
		}	
	
	}
	//연결해제
	void disconnect()
	{
		if(conn !=null)
		{
			try 
			{
				conn.close();
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}

}
