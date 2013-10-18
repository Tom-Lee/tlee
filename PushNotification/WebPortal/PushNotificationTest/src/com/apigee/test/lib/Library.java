package com.apigee.test.lib;

import org.apache.commons.logging.Log;
import com.apigee.test.util.Util;
import com.thoughtworks.selenium.Selenium;
/**
 * This class is to do the push notification automation 
 * @author tlee
 */
public class Library{

	Util util = new Util();
	
	/**
	 * Test login
	 */
	public boolean login(String host, String email, String passwd, Selenium browser, Log log){
		
		boolean isLogin = true;
		
		try {

			browser.open(host);
			
			if(util.validateTest(1,util.getXpath("email_id", "LoginPage"), browser, log) && util.validateTest(1,util.getXpath("password_id", "LoginPage"), browser, log)){
				
				log.info("Enter email, "+email+", and password, "+passwd+",");
				
				browser.type(util.getXpath("email_id", "LoginPage"),email);
				browser.type(util.getXpath("password_id", "LoginPage"),passwd);
				
				if(util.validateTest(1, util.getXpath("submit_id", "LoginPage"), browser, log)){
					
					browser.click(util.getXpath("submit_id", "LoginPage"));
					
					if (util.waitforElementText(browser,"Dashboard", log)){
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
			log.error("Error during login" + e.getMessage());
			isLogin = false;
		}
		
		return isLogin;
	}
	
	/**
	 * This method does following: 
	 * create a notifier, send push notification, verify the correctness of sent notifications, delete the created notifier
	 * @throws Exception 
	 */
	public void appPushNotification(Selenium browser, String type, Log log) throws Exception{
		
		String uuid;
		
		try{
			
			if (util.waitforElementText(browser, util.getXpath("openportal_id", "DashboradPage"), log)){
				util.waitTime(8);
				browser.click(util.getXpath("openportal_id", "DashboradPage"));
				util.waitTime(8);
				browser.click(util.getXpath("dataexplore_id", "AppService"));
				util.waitTime(8);
				browser.click(util.getXpath("device_id", "AppService"));
				util.waitTime(5);
				uuid=browser.getText(util.getXpath("uuid_id", "AppService"));
				if (uuid==null){
					Exception e = new Exception("Invalid uuid");
                    throw e;
				}
				log.info("Found the uuid: "+uuid);
				if (util.waitforElementText(browser, util.getXpath("notification1_id", "AppService"), log)){
					browser.click(util.getXpath("notification2_id", "AppService"));
					util.waitTime(3);
					browser.click(util.getXpath("configuration_id", "AppService"));
					util.waitTime(4);
					if (!util.validateTest(1, util.getXpath("notifiercheck_id", "AppService"), browser, log)){
						if (type.equals("Android")){
							browser.click(util.getXpath("android_id", "AppService"));
							util.waitTime(2);
							browser.type(util.getXpath("notifier_id", "AppService"),util.getXpath("notifierinput_id", "AppService"));
							browser.type(util.getXpath("notifierkey_id", "AppService"),util.getXpath("apikey_id", "AppService"));
							browser.click(util.getXpath("createnotifier_id", "AppService"));
							if (!util.waitforElementText(browser, util.getXpath("notifierinput_id", "AppService"), log)){
								Exception e = new Exception("Failed to add a notifier (Android)");
		                    	throw e;
							}
						}else if (type.equals("iOS")){
							browser.click(util.getXpath("apple_id", "AppService"));
							util.waitTime(2);
							browser.type(util.getXpath("applenotifier_id", "AppService"), util.getXpath("applenotifierinput_id", "AppService"));
							accessWindows(browser, log, false);
							browser.click(util.getXpath("createnotifierapple_id", "AppService"));
							if (!util.waitforElementText(browser, util.getXpath("applenotifierinput_id", "AppService"), log)){
								Exception e = new Exception("Failed to add a notifier (Apple iOS)");
		                    	throw e;
							}
						}
					}
					util.waitTime(2);
					log.info("Go to send notifications page");
					browser.click(util.getXpath("dataexplore_id", "AppService"));
					util.waitTime(3);
					browser.click(util.getXpath("notification2_id", "AppService"));
					util.waitTime(3);
					browser.click(util.getXpath("sendnotification_id", "AppService"));
					util.waitTime(3);
					browser.type(util.getXpath("message_id", "AppService"),util.getXpath("messageinfo_id", "AppService"));
					log.info("Enter the push notification message");
					
					for (int i=0;i<util.send;i++){
						browser.click(util.getXpath("schedule_id", "AppService"));
						if (!util.waitforElementText(browser, "Your notification has been scheduled", log)){
							Exception e = new Exception("Failed to schedule the notification");
		                    throw e;
						}
						util.waitTime(4);
					}
					util.waitTime(12);
					if (util.waitforElementText(browser,util.getXpath("sendHistory_id", "AppService"), log)){
						util.waitTime(8);
						browser.click(util.getXpath("sendHistory_id", "AppService"));
						util.waitTime(5);
						for (int i=1; i<(util.send+1);i++){
							if (i==2)
								browser.click(util.getXpath("historydata_id", "AppService")+"/div["+(i+1)+"]/div[1]/div[2]/a");
							else
								browser.click(util.getXpath("historydata_id", "AppService")+"/div["+i+"]/div[1]/div[2]/a");
							util.waitTime(6);
							if (util.validateTest(2, "No Receipts found", browser, log)){
								Exception e = new Exception("Failed to send out the notification");
			                    throw e;
							}else{ 
								if (browser.getText(util.getXpath("created_id", "AppService"))!=null &&
									browser.getText(util.getXpath("payload_id", "AppService"))!=null &&
									browser.getText(util.getXpath("sent_id", "AppService"))!=null &&
									browser.getText(util.getXpath("error_id", "AppService")).isEmpty()){
										log.info("The push notification "+i+" was sent out correctly from the Web");
								}else{
									Exception e = new Exception("Failed to send out the push notification "+i);
				                    throw e;
								}
							}
							browser.goBack();
							util.waitTime(3);
						}
						browser.click(util.getXpath("dataexplore_id", "AppService"));
						util.waitTime(3);
						browser.click(util.getXpath("notification2_id", "AppService"));
						util.waitTime(3);
						browser.click(util.getXpath("configuration_id", "AppService"));
						util.waitTime(3);
						if (type.equals("Android"))
							browser.click(util.getXpath("android_id", "AppService"));					
						else if (type.equals("iOS"))
							browser.click(util.getXpath("apple_id", "AppService"));		
						util.waitTime(2);
						if (util.waitforElementText(browser, util.getXpath("notifiercheck_id", "AppService"), log)){
							browser.click(util.getXpath("notifiercheck_id", "AppService"));
							browser.click(util.getXpath("delete_id", "AppService"));
							accessWindows(browser, log, true) ;
							if (!util.validateTest(2, util.getXpath("notifierinput_id", "AppService"), browser, log))
								log.info("Successfully deleted the notifier");
							else
								log.info("Error: failed to delete the notifier");
						}
					}						
				}else{
					Exception e = new Exception("Failed to add the notifier");
                    throw e;
				}					
			}	
		}catch (Exception e){
			log.error("Error during appPushNotification: " + e.getMessage());
		}	
	}
	
	/**
	 * Access the 2nd windows to performance actions 
	 */
	private void accessWindows(Selenium browser, Log log, Boolean flag) throws InterruptedException{
		
		String feedWinId;
		String[] winFocus; 
		
		try{
			feedWinId = browser.getEval(util.getXpath("script_id", "AppService"));
			if (feedWinId!=null){
				browser.selectWindow(feedWinId);
				browser.windowFocus();
				if (flag)
					browser.click(util.getXpath("deletenotifier_id", "AppService"));
				else
					browser.type(util.getXpath("certificate_id", "AppService"), util.getXpath("location_id", "AppService"));
				browser.selectWindow(null);
			}else{
				log.error("Failed to reterieve the popup");
			}
			util.waitTime(2);;
			winFocus = browser.getAllWindowTitles();
			if (!winFocus[0].isEmpty()) {
				browser.selectWindow(winFocus[0]);
			}else{
				log.error("Failed to reterieve the main window");
			}
		} catch (Exception e) {		
			log.error("Error at accessWindows "+e.getMessage());
		}
	}
	
	/**
	 * Test logout 
	 */
	public void logout(Selenium browser, Log log){
		
		try {
		
			if (util.validateTest(1, util.getXpath("email_link", "LogoutPage"), browser, log)){
				browser.click(util.getXpath("email_link", "LogoutPage"));
				browser.click(util.getXpath("signout_link", "LogoutPage"));
				if (util.waitforElementText(browser,"Sign in to your Apigee account", log)){
					log.info("Log out successfully");
				}
            }
		}catch (Exception e){
			log.error("Error during logout " + e.getMessage());
		}
	}
	
	/**
	 * Go back to Dashboard 
	 */
	public void goBacktoDashboard(Selenium browser, Log log){
		
		try {
			browser.open(util.getXpath("dashboard_id", "DashboradPage"));
			util.waitTime(3);
			log.info("Access the dashboard");
		
		}catch (Exception e){
			log.error("==>Error in goBacktoDashboar " + e.getMessage());
		}
	}
}
