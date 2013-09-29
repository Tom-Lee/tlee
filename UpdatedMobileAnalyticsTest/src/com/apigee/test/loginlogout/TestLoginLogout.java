package com.apigee.test.loginlogout;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.apigee.test.base.TestLogger;
import com.apigee.test.base.BasedBrowser;
import com.thoughtworks.selenium.Selenium;

public class TestLoginLogout extends  BasedBrowser {

	static int failedCount=0;
	protected Log log = TestLogger.getLogger(this.getClass());
	
	@Parameters({"baseurl", "screenshotdir","testdataDir"})
	@Test(enabled=true)
	public void testSendInvite(String baseurl, String screenshotdir, String testdataDir) throws IOException {

		log.info("Start to test login logout ...");
		
		Selenium browser = getBrowser();

		browser.open("/about/");
		browser.click("link=Sign In");
		//browser.click("link=Sign In");
		browser.waitForPageToLoad("30000");
		browser.type("id=email", "tlee+4@apigee.com");
		browser.type("id=password", "123Rcheck");
		browser.click("id=btnSubmit");
		browser.waitForPageToLoad("30000");
		browser.click("id=logout-link");
		browser.waitForPageToLoad("30000");

		
		
	}
}
