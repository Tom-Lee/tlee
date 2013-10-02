package com.apigee.test.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import com.apigee.test.util.GlobalUtil;
import com.thoughtworks.selenium.Selenium;

public class Library{

	GlobalUtil util = new GlobalUtil();
	
	/**
	 * Test login
	 */
	public boolean loginMobileAnalytics(String host, String email, String passwd, Selenium browser, Log log){
		
		boolean isLogin = true;
		
		try {

			browser.open(host);
			
			if(util.validateTest(1,util.getXpath("email_id", "LoginPage"), browser, log) && util.validateTest(1,util.getXpath("password_id", "LoginPage"), browser, log)){
				
				log.info("Enter email, "+email+", and password, "+passwd+",");
				browser.type("id=email",email);
				browser.type("id=password",passwd);
				
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
			log.error("==>Error during login" + e.getMessage());
			isLogin = false;
		}
		
		return isLogin;
	}
	
	/**
	 * Test App Logs Analytics page 
	 */
	public void appLogsAnalytics(Selenium browser, Log log){
		
		String [] dateList={"link=1h","link=3h","link=6h","link=12h","link=24h","link=1 week"};
		String [] monitoringTabs={"link=App Logs Analysis","link=Network Performance", "link=App Usage"};
		String [] errorTabs={"link=Errors","link=Errors by App Versions","link=Errors by Config Overrides","link=Errors by Device Platforms","link=Errors by Device Models"};
		String [] performanceTabs={"xpath=(//a[contains(text(),'Overview')])[2]","link=Errors by Network Types","link=Response Time by Network Types","link=Errors by Carriers","link=Response Time by Carriers"};
		String [] appUsageTabs={"link=Sessions","link=Sessions by App Versions","link=Sessions by Config Overrides","link=Sessions by Device Platforms","link=Sessions by Device Models","link=Session Duration"};
		
		try{
			
			if (util.waitforElementText(browser,util.getXpath("mobileAnalytics_id","DashboradPage"), log)){
				log.info("Click 'Open Analytics'");		
				util.waitTime(6);
				browser.click(util.getXpath("mobileAnalytics_id","DashboradPage"));
				util.waitTime(6);
				if (util.validateTest(2,"App Overview" , browser, log )){
					
					if (util.waitforElementText(browser,util.getXpath("monitor_id","ActionPage"), log)){
						log.info("Click monitoring icon of sandbox");
						browser.click(util.getXpath("monitor_id","ActionPage"));
						util.waitTime(6);
						log.info("Select 'Acme Bank Demo App' app");
						browser.select(util.getXpath("dropdown_id","MobileAnalyticsPage"),util.getXpath("app_id","MobileAnalyticsPage"));
						util.waitforElementText(browser,"Error", log);
						util.waitTime(6);
					}	
				}
			}
			
			for (int i=0; i<monitoringTabs.length;i++){
				
				browser.click(monitoringTabs[i]);
				log.info("Access "+monitoringTabs[i].replace("link=", "")+" tab");
				
				if (monitoringTabs[i].contains("App Logs Analysis")){
				
					for (int j=0; j<errorTabs.length;j++){
						
						util.waitTime(4);
						if (!errorTabs[j].equals("link=Errors")){
							util.waitTime(4);
							browser.click(errorTabs[j]);
						}
						util.waitforElementText(browser,errorTabs[j].replace("link=", ""),log);
						log.info("Access "+errorTabs[j].replace("link=", "")+" tab");
						
						for (int k=0;k<dateList.length; k++){
							
							util.waitTime(4);
							browser.click(dateList[k]);
							util.waitTime(4);
							log.info("Access "+dateList[k].replace("link=", "")+" tab");
							
							validateErrorLogPages(browser,k,log );	
							
						}
					}
				}
				
				if (monitoringTabs[i].contains("Network Performance")){
					continue;
				}
				
				if (monitoringTabs[i].contains("App Usage")){
					continue; 
				}
			}
			
		}catch (Exception e){
			log.error("Error during appLogsAnalytics" + e.getMessage());
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void validateErrorLogPages(Selenium browser, int num, Log log) {
		
		String regex="^[0-9]+";
		HashMap item = new HashMap();
		
		try {
			
			item.put("Total Error", browser.getText(util.getXpath("totalError_id", "MobileAnalyticsPageErrorTab")));
			item.put("Total Warning", browser.getText(util.getXpath("totalError_id", "MobileAnalyticsPageErrorTab")));
			item.put("Total Event", browser.getText(util.getXpath("totalEvent_id", "MobileAnalyticsPageErrorTab")));
			item.put("Application Crash", browser.getText(util.getXpath("applicationCrash_id", "MobileAnalyticsPageErrorTab")).replace("Application Crashes : ",""));
			item.put("Application Log Event", browser.getText(util.getXpath("applicationLogEvent_id", "MobileAnalyticsPageErrorTab")).replace("Application Log Events: ",""));
			
			Iterable set = item.entrySet();      
		    Iterator i = set.iterator();
		     
		    while(i.hasNext()) {
			
		    	Map.Entry me = (Map.Entry)i.next();
		        if(me.getValue().toString().matches(regex)) 
		        	log.info("Found "+me.getKey().toString()+": "+me.getValue());
		        else
		        	log.error(me.getKey().toString()+" is not correct: "+me.getValue());
		    }
		    
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error during validateErrorLogPages" + e.getMessage());
		}
		
	}
	
	/**
	 * Test logout 
	 */
	public void logoutMobileAnalytics(Selenium browser, Log log){
		
		try {
		
			if (util.validateTest(1, util.getXpath("email_link", "LogoutPage"), browser, log)){
				browser.click(util.getXpath("email_link", "LogoutPage"));
				browser.click(util.getXpath("signout_link", "LogoutPage"));
				if (util.waitforElementText(browser,"Sign in to your Apigee account", log)){
					log.info("Log out successfully");
				}
            }

		}catch (Exception e){
			log.error("==>Error during logout" + e.getMessage());
		}
	}
	
	
}
