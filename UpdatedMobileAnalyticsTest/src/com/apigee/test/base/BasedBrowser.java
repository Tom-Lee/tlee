package com.apigee.test.base;

import junit.framework.TestCase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class BasedBrowser extends TestCase{
	
	protected Selenium browser;
	private String screenShotDir;
	
	@Parameters({"seleniumserverhost", "serverport", "browserpath", "baseurl"})
	@BeforeClass(alwaysRun = true)
	protected void init(@Optional("localhost")String server, @Optional("90")int port, @Optional("*firefoxe")String br, @Optional("http://www.apigee.com")String appurl) throws Exception {
		
		//Process p = Runtime.getRuntime().exec("Xvfb :99");
	
		this.browser = new DefaultSelenium(server, port, br, appurl);

		this.browser.start();
		this.browser.windowMaximize();
		
	}

	@AfterClass(alwaysRun = true)
	protected void stop() throws Exception {
		this.browser.stop();
	}

	public Selenium getBrowser() {
		return browser;
	}	
	
	@Parameters({"screenshotdir"})
	@BeforeSuite(alwaysRun = true)
	public void setScreenShotDir(String screenshotdir){
		this.screenShotDir = screenshotdir;
	}
	
	public String getScreenShotDir(){
		return this.screenShotDir;
	}
	
	public void closeAndReopenBrowser()throws Exception {
		// stop current one
		this.browser.stop();
		
		// re-initiate
		this.browser.start();
	}
	
	public void maximizeBrowser(){
		//maximize the window as much a possible using selenium
		this.browser.windowMaximize();
	}
	
	public void setBrowserFocus() {
		//bring the browser window into focus
		this.browser.windowFocus();
	}

}

