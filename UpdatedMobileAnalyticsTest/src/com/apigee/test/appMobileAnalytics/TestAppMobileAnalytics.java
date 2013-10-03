package com.apigee.test.appMobileAnalytics;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.apigee.test.base.TestLogger;
import com.apigee.test.base.BasedBrowser;
import com.apigee.test.lib.Library;
import com.apigee.test.util.Util;
import com.thoughtworks.selenium.Selenium;

public class TestAppMobileAnalytics extends BasedBrowser{
	
	static int failedCount=0;
	Util util = new Util();
	Library library=new Library();
	protected Log log = TestLogger.getLogger(this.getClass());
	
	@Parameters({"baseurl", "screenshotdir","testdataDir"})
	@Test(enabled=true)
	public void testAppMobileAnalytics(String baseurl, String screenshotdir, String testdataDir) throws IOException {
		
		Selenium browser = getBrowser();

		try{
		
			log.info("Start to test App Logs Analytics page");
			
			//Production environment
			if (library.loginMobileAnalytics("/accounts/sign_in", "tlee+4@apigee.com","123Rcheck", browser, log)){
				library.appMobileAnalytics(browser,log);
				library.logoutMobileAnalytics(browser, log);
			}
			
			//Dit environment
			/*if (library.loginMobileAnalytics("/accounts/sign_in", "tlee+ditapp@apigee.com","123Rcheck", browser, log)){
				library.logoutMobileAnalytics(browser, log);
			}*/
			
			log.info("End to test App Logs Analytics page");
			
		}catch(Exception e){
			failedCount++;
			util.takeScreenShot ("testAppMobileAnalytics ", browser, failedCount, screenshotdir);
			log.error("Error"+util.getStackTrace(e));
		}
	}
}
