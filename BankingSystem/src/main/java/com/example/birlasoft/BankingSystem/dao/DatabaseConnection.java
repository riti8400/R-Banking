package com.example.birlasoft.BankingSystem.dao;


/**
 * @author Riti Mishra
 * 
 * Class to establish database connection to local as well as cloud database
 * 
 */





import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Database connection implementation
public class DatabaseConnection {
	public static Connection getConnection() throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		//For using local database then uncomment the below line
		
		//Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank", "ritim", "Geeta@8400");
	
		//For using the cloud database use below connection
		Connection con=DriverManager.getConnection("jdbc:mysql://b5fcc297a66b3a:0ec4a567@us-cdbr-east-02.cleardb.com/heroku_fe2dc889c924c0a?reconnect=true","b5fcc297a66b3a","0ec4a567");
		System.out.println(con);
		return con;
	}

	public static void closeConnection(Connection con) throws SQLException{
		con.close();
	}

}
