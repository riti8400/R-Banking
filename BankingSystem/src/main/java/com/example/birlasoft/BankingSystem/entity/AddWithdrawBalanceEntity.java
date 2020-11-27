package com.example.birlasoft.BankingSystem.entity;

/*
 * @author Riti Mishra
 * 
 * 
 * POJO for adding/withdrawing amount to/from account
 * 
 * 
 */

public class AddWithdrawBalanceEntity {
	private long customerId;
	private double balance;
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
}
