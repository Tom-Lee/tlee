package com.apigee.test.pushios;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.apigee.test.base.BasedBrowser;
import com.apigee.test.base.TestLogger;
import com.apigee.test.lib.Library;
import com.apigee.test.util.Util;
import com.thoughtworks.selenium.Selenium;

public class TestPushNotificationiOS  extends BasedBrowser{
	
	static int failedCount=0;
	Util util = new Util();
	Library library=new Library();
	protected Log log = TestLogger.getLogger(this.getClass());
	
	@Parameters({"baseurl", "screenshotdir","testdataDir"})
	@Test(enabled=true)
	public void testPushNotificationiOS(String baseurl, String screenshotdir, String testdataDir) throws IOException {
		
		Selenium browser = getBrowser();

		try{
		
			log.info("Start to test iOS push notifications");
			browser.deleteAllVisibleCookies();
			if (library.login(util.getXpath("loginPath","Account"),util.getXpath("bemailUpdate","Account"),util.getXpath("bpasswordUpdate","Account"), browser, log)){
				library.appPushNotification(browser,"iOS",log);
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
			
			log.info("End to test iOS push notifications");
			System.exit(0);
			
			
		}catch(Exception e){
			failedCount++;
			util.takeScreenShot ("testPushNotificationiOS ", browser, failedCount, screenshotdir);
			log.error("Error in testPushNotificationiOS "+util.getStackTrace(e));
		}
	}
}

