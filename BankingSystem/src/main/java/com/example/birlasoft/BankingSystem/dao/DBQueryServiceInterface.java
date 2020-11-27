package com.example.birlasoft.BankingSystem.dao;


/*
 * @author Riti Mishra
 * 
 * 
 * Interface for stored queries
 * 
 */

public interface DBQueryServiceInterface {
	//to create account
	public static final String createAccountQuery = "call createAccount(?,?,?,?,?,?,?,?)";
	
	//to fetch user details
	public static final String getDetailsQuery = "select customer_id,name,acc_number,email,contact,balance from user_details where customer_id= ?";
	
	//for adding amount to the account
	public static final String addBalanceQuery = "update user_details set balance = balance+? where customer_id = ?";
	
	//for withdrawing amount from the account
	public static final String withdrawQuery = "call withdraw(?,?,?,?)";
	
	//for user login in the account
	public static final String userLogin= "select * from user_details where email=? and password=?";
}
