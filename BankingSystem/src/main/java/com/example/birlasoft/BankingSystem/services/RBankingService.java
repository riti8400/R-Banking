package com.example.birlasoft.BankingSystem.services;


/**
 * @author Riti Mishra
 * 
 */


import com.example.birlasoft.BankingSystem.dao.DatabaseConnection;

import com.example.birlasoft.BankingSystem.dao.*;
import com.example.birlasoft.BankingSystem.entity.RBankingDetails;
import com.example.birlasoft.BankingSystem.entity.SetResponseEntity;
import com.example.birlasoft.BankingSystem.entity.AddWithdrawBalanceEntity;
import com.example.birlasoft.BankingSystem.utils.AppConstants;
import com.example.birlasoft.Exception.OutOfBalanceException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Class Implementation of Service

public class RBankingService implements AppConstants{
	
	
	/**
	 * @param accountDetails
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	
	public List<SetResponseEntity>  createAccount(RBankingDetails accountDetails) throws ClassNotFoundException ,SQLException{
		
		//Creating POJO object to set the response of Account Creation Service
		SetResponseEntity entity=new SetResponseEntity();
		
		//Created a List to set the return value of the service
		List<SetResponseEntity> response=new ArrayList<SetResponseEntity>();
		
		/*
		 * Used timestamp and combination of random numbers to generate the account number of User
		 */
		Timestamp timestamp=new Timestamp(System.currentTimeMillis());
		Random rand=new Random();
		long customerId=timestamp.getTime();
		long accountNumber=Long.parseLong(Integer.toString(AppConstants.bankId)+Long.toString(customerId)+Integer.toString(rand.nextInt(90)+10));
		
		//Set the account number and customer id to object of POJO RBankingDetails
		accountDetails.setAccountNumber(accountNumber);
		accountDetails.setCustomerId(customerId);
		
		//Set the initial balance of any User to zero
		double balance=0;
		
		//Database connection is initialized as null
		Connection con = null;  
		
		try {
			
			// For Database connection 
			con=DatabaseConnection.getConnection();
			
			//Calling the store database procedure for creating an account
			CallableStatement stmt = con.prepareCall(DBQueryServiceInterface.createAccountQuery);
			
			//Set the attributes of database table
			stmt.setString(1,Long.toString(accountDetails.getCustomerId()));
			stmt.setString(2, accountDetails.getName());
			stmt.setString(3, accountDetails.getEmail());
			stmt.setString(4, Long.toString(accountDetails.getContactNumber()));
			
			stmt.setString(5, Long.toString(accountDetails.getAccountNumber()));
			stmt.setString(6, accountDetails.getPassword());
			stmt.setDouble(7, balance);
			stmt.registerOutParameter(8, Types.INTEGER);
			
			
			//For executing the database query
			ResultSet rs=stmt.executeQuery();
			
			//if query executed successfully the set the desired response
			if (rs.next()) {
				int responseCode=rs.getInt("response_code");
				if(responseCode==200) {
					entity.setMsg("Account Created");
					entity.setResponseCode(200);
					entity.setCustomerId(customerId);
				}
				else {
					entity.setMsg("User Already Exists");
					entity.setResponseCode(502);
				}
				
			}
			//if query fails the set response code as 401
			else {
				entity.setMsg("Query fail");
				entity.setResponseCode(401);
			}
			response.add(entity);
		} catch (Exception e) {
			
			//Set response as 401 during exception
			entity.setMsg("Error while Registering in Service");
			entity.setResponseCode(401);
			response.add(entity);
			e.printStackTrace();
		} finally {
			//Close the database connection
			try {
				DatabaseConnection.closeConnection(con);
			} catch (Exception e) {
				System.out.println("Exception in closing");
			}
		}
		
		return response;
		
		}
	
	
	//Function to fetch User Details
	public List<RBankingDetails> getDetails(long customerId) throws SQLException {
		
		//Create database object
		Connection con=null;
		
		//Response value of the service
		List<RBankingDetails> details = null;
		try {
			
			//Establish Database connection
			con=DatabaseConnection.getConnection();
			
			//Calling Query
			PreparedStatement stmt=con.prepareStatement(DBQueryServiceInterface.getDetailsQuery);
			stmt.setString(1,Long.toString(customerId));
			
			//Execute the query
			ResultSet rs= stmt.executeQuery();
			details=new ArrayList<RBankingDetails>();
			
			//if query executed successfully set the details of user to RBanking Details POJO
			while(rs.next()) {
				RBankingDetails bankingDetails = new RBankingDetails();
				bankingDetails.setCustomerId(Long.parseLong(rs.getString("customer_id")));
				bankingDetails.setAccountNumber(Long.parseLong(rs.getString("acc_number")));
				bankingDetails.setName(rs.getString("name"));
				bankingDetails.setEmail(rs.getString("email"));
				bankingDetails.setContactNumber(Long.parseLong(rs.getString("contact")));
				bankingDetails.setBalance(rs.getDouble("balance"));
				
				details.add(bankingDetails);
			}
		}
		catch(Exception e) {
			
			System.out.println("Error While Getting Details");
		} finally {
			//Close the connection
			DatabaseConnection.closeConnection(con);
		}
		
		return details;
	}
	
	//Function for adding balance to the account
	public List<SetResponseEntity> addBalanceAmount(AddWithdrawBalanceEntity add) throws SQLException {
		
		//Create database connection
		Connection con=null;
		
		//Return response of the function
		List<SetResponseEntity> response=new ArrayList<SetResponseEntity>();
		SetResponseEntity entity=new SetResponseEntity();
		try {
			
			//Establish database connection
			con=DatabaseConnection.getConnection();
			PreparedStatement stmt=con.prepareStatement(DBQueryServiceInterface.addBalanceQuery);
			stmt.setDouble(1,add.getBalance());
			stmt.setString(2, Long.toString(add.getCustomerId()));
			
			//execute the query
			int line = stmt.executeUpdate();
			
			//if query executed successfully set desired response
			if (line >0) {
				entity.setMsg("Amount Added Successfully");
				entity.setResponseCode(200);
			}
			//if query execute unsuccessfully set response code to 401
			else {
				entity.setMsg("Error While Quering");
				entity.setResponseCode(401);
			}
			response.add(entity);
		}
		catch(Exception e) {
			
			//Set response code to 401 during exception
			entity.setMsg("Error While Adding Amount");
			entity.setResponseCode(401);
			response.add(entity);
		}
		finally {
			//Close the connection
			DatabaseConnection.closeConnection(con);
		}
		return response;
	}
	
	
	//Function for withdrawing amount from the account
	public int withdrawAmount(AddWithdrawBalanceEntity withdraw) throws SQLException ,OutOfBalanceException{
		
		//Create database connection object
		Connection con=null;
		int code=401;
		try {
			
			//Establish database connection
			con=DatabaseConnection.getConnection();
			
			//Call the stored procedure for withdrawing amount
			CallableStatement stmt=con.prepareCall(DBQueryServiceInterface.withdrawQuery);
			
			//Set the database relation attribute
			stmt.setString(1, Long.toString(withdraw.getCustomerId()));
			stmt.setDouble(2,withdraw.getBalance());
			
			stmt.registerOutParameter(3, Types.DOUBLE);
			stmt.registerOutParameter(4, Types.INTEGER);
			
			//Execute the query
			ResultSet rs=stmt.executeQuery();
			
			
			//if query executed successfully the set then desired response
			if(rs.next()) {
				int responseCode=rs.getInt("response_code");
				System.out.println(responseCode);
				if(responseCode==200) {
					code=200;
				}
				else if(responseCode==402) {
					throw new OutOfBalanceException("Insufficient Balance");
				}
				else {
					code=401;
				}
			}

		}
		catch(Exception e) {
			System.out.println("Adding Balance Interrupted");
		}
		finally {
			
			//Close the connection
			DatabaseConnection.closeConnection(con);
		}
		return code;
	}
	
	
	//Function for user login
	public List<SetResponseEntity> loginBankUser(RBankingDetails login) throws SQLException {
		
		//Create database connection object
		Connection con=null;
		
		List<SetResponseEntity> response=null;
		SetResponseEntity entity=new SetResponseEntity();
		try {
			response=new ArrayList<SetResponseEntity>();
			
			//Establish database connection
			con=DatabaseConnection.getConnection();
			PreparedStatement stmt=con.prepareStatement(DBQueryServiceInterface.userLogin);
			stmt.setString(1, login.getEmail());
			stmt.setString(2, login.getPassword());
			
			//Execute the query
			ResultSet rs=stmt.executeQuery();
			
			//if query executed successfully or fails then set the desired response
			if(rs.next()) {
				entity.setMsg("Login Succesful");
				entity.setResponseCode(200);
				entity.setCustomerId(Long.parseLong(rs.getString("customer_id")));
			}
			else {
				entity.setMsg("Invalid Cridentials");
				entity.setResponseCode(401);
			}
			response.add(entity);
		}
		catch(Exception e) {
			//Set response code to 401 during exception
			entity.setMsg("Error While Login");
			entity.setResponseCode(401);
			response.add(entity);
		}
		finally {
			
			//Close the connection
			DatabaseConnection.closeConnection(con);
		}
		return response ;
	}
}
