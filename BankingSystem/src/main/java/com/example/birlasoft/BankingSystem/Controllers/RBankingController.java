/**
 * 
 */
package com.example.birlasoft.BankingSystem.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.birlasoft.BankingSystem.entity.RBankingDetails;
import com.example.birlasoft.BankingSystem.entity.SetResponseEntity;
import com.example.birlasoft.BankingSystem.entity.AddWithdrawBalanceEntity;
import com.example.birlasoft.BankingSystem.services.RBankingService;
import com.example.birlasoft.Exception.OutOfBalanceException;

/**
 * @author Riti Mishra
 * 
 */


//Rest Controller Annotations Start
@RestController                         
@RequestMapping("/RBanking")
@CrossOrigin
//Rest Controller Annotations End


//Controller Class Start
public class RBankingController {
	
	//Post Mapping For Creating an Account
	@PostMapping("/createAccount")
	
	//Function to Create Account
	public List<SetResponseEntity> createUserAccount(@RequestBody RBankingDetails accountDetails) {
		
		//Creating POJO object to set the response of Account Creation Service
		SetResponseEntity entity=new SetResponseEntity();
		
		//Created a List to set the return value of the controller
		List<SetResponseEntity> response=new ArrayList<SetResponseEntity>();
		try {
			
			//Calling Service
			RBankingService bankingService = new RBankingService();
			//Service function to create Account
			response=bankingService.createAccount(accountDetails);
			
			System.out.println(accountDetails.getName());
		} catch (Exception e) {
			//Response Code is Set to 401 for handling exception
			entity.setMsg("Error While Registering in Controller");
			entity.setResponseCode(401);
		}
		
		// Response is returned
		return response;
	}
	
	
	//Get Mapping to Fetch User Details
	@GetMapping("/getDetails")
	public List<RBankingDetails> getUserDetails(@RequestParam long customerId ) {
		
		//List to set the return value of function as RBankingDetails POJO
		List<RBankingDetails> details = null;
		try {
			
			//Calling Service
			RBankingService bankingService = new RBankingService();
			//Service Function to fetch User Details
			details = bankingService.getDetails(customerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Response is returned
		return details;
	}
	
	
	//Post Mapping for Adding Amount to the Account
	@PostMapping("/addBalance")
	public List<SetResponseEntity> addBalance (@RequestBody AddWithdrawBalanceEntity add) {
		
		//List to Set the return value of function as an Object type of POJO Set Response Entity
		List<SetResponseEntity> response=new ArrayList<SetResponseEntity>();
		//Creating POJO object to set the response of Adding balance service
		SetResponseEntity entity=new SetResponseEntity();
		try {
			
			//Calling Service
			RBankingService bankingService = new RBankingService();
			//Service Function for adding amount to account
			response=bankingService.addBalanceAmount(add);
		}
		catch(Exception e) {
			//Set response code to 401 for handling the exception
			entity.setMsg("Error while Adding Balance");
			entity.setResponseCode(401);
		}
		return response;
	}
	
	
	//Post Mapping for Withdrawing Amount from the Account
	@PostMapping("/withdraw")
	public List<SetResponseEntity> withdraw(@RequestBody AddWithdrawBalanceEntity withdraw) {
		
		//List to Set the return value of function as an Object type of POJO Set Response Entity
		List<SetResponseEntity> details=new ArrayList<SetResponseEntity>();
		
		//Creating POJO object to set the response of withdrawing service
		SetResponseEntity bankingResponse=new SetResponseEntity();
		
		//Variable to store response code 
		int code;
		try {
			
			//Calling Service
			RBankingService bankingService=new RBankingService();
			
			//Service function for withdrawing amount from the account and assign the return value of function to variable code
			code=bankingService.withdrawAmount(withdraw);
			
			//if response code is 200 then set the response message as successful
			if(code==200) {
				bankingResponse.setMsg("Balance Updated Successful");
				bankingResponse.setResponseCode(200);
			}
			
			//if response code is 401 then set the response message as Invalid Customer id
			else if(code==401) {
				bankingResponse.setMsg("Invalid Customer ID");
				bankingResponse.setResponseCode(401);
			}
		}
		catch(OutOfBalanceException oe) {
			//if any exception occur then set the response message as Insufficient Amount and set response code as 402
			bankingResponse.setMsg("Insufficient Amount in your Account");
			bankingResponse.setResponseCode(402);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		details.add(bankingResponse);
		return details;
	}
	
	
	//Post Mapping for Users to login in their account
	@PostMapping("/login")
	public List<SetResponseEntity> loginUser(@RequestBody RBankingDetails login) {
		//List to Set the return value of function as an Object type of POJO Set Response Entity
		List<SetResponseEntity> response=new ArrayList<SetResponseEntity>();
		
		//Creating POJO object to set the response of user login service
		SetResponseEntity entity=new SetResponseEntity();
		try {
			
			//Calling Service
			RBankingService bankingService = new RBankingService();
			//Service function for user login
			response=bankingService.loginBankUser(login);
		}
		catch(Exception e) {
			//Set response code as 401 for handling the exception
			entity.setMsg("Error While Login");
			entity.setResponseCode(401);
		}
		return response;
	}
}
