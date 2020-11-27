/**
 * 
 */
package com.example.birlasoft.Exception;

/**
 * @author Riti Mishra
 * 
 * Custom exception occurs when user tries to withdraw amount greater than current balance in their respective account
 *
 */

public class OutOfBalanceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4263376762628814809L;
	String str;
	public OutOfBalanceException(String str){
		this.str=str;
	}
	public String toString() {
		return (str);
	}
	
}
