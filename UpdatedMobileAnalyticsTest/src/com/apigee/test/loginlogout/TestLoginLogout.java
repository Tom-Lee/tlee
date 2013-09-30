package com.apigee.test.loginlogout;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.apigee.test.base.TestLogger;
import com.apigee.test.base.BasedBrowser;
import com.apigee.test.lib.Library;
import com.apigee.test.util.GlobalUtil;
import com.thoughtworks.selenium.Selenium;

public class TestLoginLogout extends BasedBrowser {

	static int failedCount=0;
	GlobalUtil util = new GlobalUtil();
	Library library=new Library();
	protected Log log = TestLogger.getLogger(this.getClass());
	
	@Parameters({"baseurl", "screenshotdir","testdataDir"})
	@Test(enabled=true)
	public void testSendInvite(String baseurl, String screenshotdir, String testdataDir) throws IOException {
		
		Selenium browser = getBrowser();

		try{
		
			log.info("Start to test login logout");
			
			//Production environment
			//if (library.loginMobileAnalytics("/accounts/sign_in", "tlee+4@apigee.com","123Rcheck", browser, log)){
			//	library.logoutMobileAnalytics(browser, log);
			//}
			
			//Dit environment
			if (library.loginMobileAnalytics("/accounts/sign_in", "tlee+ditapp@apigee.com","123Rcheck", browser, log)){
				library.logoutMobileAnalytics(browser, log);
			}
			
			log.info("End to test login logout");
			
		}catch(Exception e){
			failedCount++;
			util.takeScreenShot ("doLoginLogout", browser, failedCount, screenshotdir);
			log.error("Error"+util.getStackTrace(e));
		}
	}

}
