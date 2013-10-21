package com.apigee.test.pushandroid;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.apigee.test.base.TestLogger;
import com.apigee.test.base.BasedBrowser;
import com.apigee.test.lib.Library;
import com.apigee.test.util.Util;
import com.thoughtworks.selenium.Selenium;

/**
 * The driver class to start the Android push notification test          
 */

public class TestPushNotificationAndroid extends BasedBrowser{
	
	static int failedCount=0;
	Util util = new Util();
	Library library=new Library();
	protected Log log = TestLogger.getLogger(this.getClass());
	
	@Parameters({"baseurl", "screenshotdir","testdataDir"})
	@Test(enabled=true)
	public void testPushNotificationAndroid(String baseurl, String screenshotdir, String testdataDir) throws IOException {
		
		Selenium browser = getBrowser();

		try{
		
			log.info("Start to test Android push notifications");
			browser.deleteAllVisibleCookies();
			if (library.login(util.getXpath("loginPath","Account"),util.getXpath("aemailUpdate","Account"),util.getXpath("apasswordUpdate","Account"), browser, log)){
				library.appPushNotification(browser,"Android",log);
				library.goBacktoDashboard(browser,log);
				library.logout(browser, log);
				stop();
			}else{
				Exception e = new Exception("Failed to log in the account");
                throw e;
			}
			
			//Dit environment
			/*if (library.loginMobileAnalytics("/accounts/sign_in", "","", browser, log)){
				library.logoutMobileAnalytics(browser, log);
			}*/
			
			log.info("End to test Android push notifications");
			System.exit(0);
			
		}catch(Exception e){
			failedCount++;
			util.takeScreenShot ("testPushNotificationAndroid ", browser, failedCount, screenshotdir);
			log.error("Error in testPushNotificationAndroid "+util.getStackTrace(e));
		}
	}
}
