package com.apigee.test.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.apigee.test.util.Util;
import com.thoughtworks.selenium.Selenium;

public class Library{

	Util util = new Util();
	
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
	 * Access the mobile analytics pages (App Logs Analysis, Network Performance, App Usage)
	 */
	public void appMobileAnalytics(Selenium browser, Log log){
		
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
						util.waitforElementText(browser,util.getXpath("dropdown_id","MobileAnalyticsPage"), log);
						util.waitforElementText(browser,util.getXpath("app_id","MobileAnalyticsPage"), log);
						log.info("Select 'Acme Bank Demo App' app");
						browser.select(util.getXpath("dropdown_id","MobileAnalyticsPage"),util.getXpath("app_id","MobileAnalyticsPage"));
						util.waitforElementText(browser,"Error", log);
						util.waitTime(6);
					}	
				}
			}
			
			for (int i=0; i<monitoringTabs.length;i++){
				
				browser.click(monitoringTabs[i]);
				util.waitTime(5);
				log.info("Access "+monitoringTabs[i].replace("link=", "")+" tab");
				
				if (monitoringTabs[i].contains("App Logs Analysis")){
					
					accessTabs(browser, errorTabs,dateList, "App Logs Analysis", log);
				}
				
				if (monitoringTabs[i].contains("Network Performance")){
					
					accessTabs(browser, performanceTabs,dateList, "Network Perfermance", log);
				}
				
				if (monitoringTabs[i].contains("App Usage")){
					
					accessTabs(browser, appUsageTabs,dateList, "App Usage", log);
				}
			}
			
			browser.open(util.getXpath("dashboard_id", "DashboradPage"));
			util.waitTime(4);
			
		}catch (Exception e){
			log.error("Error during appMobileAnalytics" + e.getMessage());
		}
		
	}
	
	/**
	 * Access the sub tabs under App Logs Analysis, Network Performance, and App Usage and their different time period tabs
	 */
	private void accessTabs(Selenium browser, String [] subTabs, String [] timeTabs, String tabName, Log log) throws InterruptedException{
		
		try{
			for (int j=0; j< subTabs.length;j++){
				
				util.waitTime(5);
				
				if (subTabs[j].equals("xpath=(//a[contains(text(),'Overview')])[2]")){
					util.waitforElementText(browser, "Overview",log);
					log.info("Access OverView tab");
				}else if (subTabs[j].equals("link=Errors")){
					util.waitforElementText(browser, "Errors",log);
					log.info("Access Errors tab");
				}else if (subTabs[j].equals("link=Sessions")){
					util.waitforElementText(browser, "Sessions",log);
					log.info("Access Sessions tab");
				}else{
					browser.click(subTabs[j]);
					util.waitforElementText(browser,subTabs[j].replace("link=", ""),log);
					log.info("Access "+subTabs[j].replace("link=", "")+" tab");
				}
				
				for (int k=0;k<timeTabs.length; k++){
					
					util.waitTime(5);
					browser.click(timeTabs[k]);
					util.waitTime(4);
					log.info("Access "+timeTabs[k].replace("link=", "")+" tab");
					
					validateErrorLogPages(browser,tabName, log );	
					
				}
			}
		}catch (Exception e){
			log.error("Error during accessTabs " + e.getMessage());
		}	
		
	}
	
	/**
	 * Extract the data from the web page, and record them to the log 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void validateErrorLogPages(Selenium browser, String tabName, Log log) {
		
		String regex="^[0-9]+";
		HashMap item = new HashMap();
		
		try {
			
			if (tabName.equals("App Logs Analysis")){
				item.put("Total Error", browser.getText(util.getXpath("totalError_id", "MobileAnalyticsPageErrorTab")));
				item.put("Total Warning", browser.getText(util.getXpath("totalError_id", "MobileAnalyticsPageErrorTab")));
				item.put("Total Event", browser.getText(util.getXpath("totalEvent_id", "MobileAnalyticsPageErrorTab")));
				item.put("Application Crash", browser.getText(util.getXpath("applicationCrash_id", "MobileAnalyticsPageErrorTab")).replace("Application Crashes : ",""));
				item.put("Application Log Event", browser.getText(util.getXpath("applicationLogEvent_id", "MobileAnalyticsPageErrorTab")).replace("Application Log Events: ",""));
			}else if (tabName.equals("Network Perfermance")){
				item.put("Average Latency", browser.getText(util.getXpath("averageLatency_id", "MobileAnalyticsNetworkPerformanceTab")));
				item.put("Max Latency", browser.getText(util.getXpath("maxLatency_id", "MobileAnalyticsNetworkPerformanceTab")));
				item.put("Total Network Requests", browser.getText(util.getXpath("totalNetworkRequests_id", "MobileAnalyticsNetworkPerformanceTab")));
				item.put("Total Network Errors", browser.getText(util.getXpath("totalNetworkErrors_id", "MobileAnalyticsNetworkPerformanceTab")));
				item.put("Application Network Events", browser.getText(util.getXpath("applicationNetworkEvents_id", "MobileAnalyticsNetworkPerformanceTab")).replace("Application Network Events: ",""));
			}else if (tabName.equals("App Usage")){
				item.put("Total Sections", browser.getText(util.getXpath("totalSections_id", "MobileAnalyticsAppUsageTab")));
				item.put("Max Concurrent Sessions", browser.getText(util.getXpath("maxConcurrentSessions_id", "MobileAnalyticsAppUsageTab")));
				item.put("Avg Session Length", browser.getText(util.getXpath("avgSessionLength_id", "MobileAnalyticsAppUsageTab")));
				item.put("Total Unique Users", browser.getText(util.getXpath("totalUniqueUsers_id", "MobileAnalyticsAppUsageTab")));
			}
			
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
			log.error("Error during validateErrorLogPages " + e.getMessage());
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
