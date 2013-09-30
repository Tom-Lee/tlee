package com.apigee.test.lib;

import org.apache.commons.logging.Log;
import com.apigee.test.util.GlobalUtil;
import com.thoughtworks.selenium.Selenium;

public class Library{

	GlobalUtil util = new GlobalUtil();
	
	/**
	 * Login
	 */
	public boolean loginMobileAnalytics(String host, String email, String passwd, Selenium browser, Log log){
		
		boolean isLogin = true;
		
		try {

			//browser.open("/accounts/sign_in?callback=https://accounts.jupiter.apigee.net/accounts/dashboard");
			browser.open(host);
			if(util.validateTest(1,util.getXpath("email_id", "LoginPage"), browser, log) && util.validateTest(1,util.getXpath("password_id", "LoginPage"), browser, log)){
				
				log.info("Enter email, "+email+", and password, "+passwd+",");
				browser.type("id=email",email);
				browser.type("id=password",passwd);
				if(util.validateTest(1, util.getXpath("submit_id", "LoginPage"), browser, log)){
					browser.click(util.getXpath("submit_id", "LoginPage"));
					if (util.waitforElementText(browser,"Dashboard")){
						log.info("Log in the account");
					}else{
						log.error("Error: cannot log in the account for 30 seconds");
					}
					
				}else {
					log.error("Error: the submit button does not exist.");
					isLogin = false;
				}
			} else {
				log.error("Error: email or password id does not exist.");
				isLogin = false;
			} 
		}catch (Exception e){
			log.error("==>Error during login" + e.getMessage());
			isLogin = false;
		}
		return isLogin;
	}
	
	
	/**
	 * Logout 
	 */
	public void logoutMobileAnalytics(Selenium browser, Log log){
		
		try {
		
			if (util.validateTest(1, util.getXpath("email_link", "LogoutPage"), browser, log)){
				browser.click(util.getXpath("email_link", "LogoutPage"));
				browser.click(util.getXpath("signout_link", "LogoutPage"));
				if (util.waitforElementText(browser,"Sign in to your Apigee account")){
					log.info("Log out successfully");
				}
            }

		}catch (Exception e){
			log.error("==>Error during logout" + e.getMessage());
		}
	}
	
	
}
