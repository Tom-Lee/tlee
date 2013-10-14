package com.apigee.test.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.thoughtworks.selenium.Selenium;
import com.apigee.test.base.BasedBrowser;
import com.apigee.test.base.TestLogger;

/**
 * This class has general utility methods
 * @author tlee
 */
public class Util extends BasedBrowser {
	
	protected Log log = TestLogger.getLogger(this.getClass());
	public final int send=2;
	private static File objectXml=new File("/Users/ApigeeCorporation/Automation/EclipseWorkspace/PushNotificationTest/testdata/objectPath.xml");
											
    public void takeScreenShot (String msg, Selenium browser, int count, String path){

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-Hmmss");
    	java.util.Date today = new java.util.Date();
    	String formatedTime=formatter.format(new java.sql.Timestamp(today.getTime()));
    	long t = System.currentTimeMillis() / 1000L;
    	String unixTime=String.valueOf(t);

    	String name=formatedTime+"-"+msg+"-"+count+"-"+unixTime+".png";
    	browser.captureEntirePageScreenshot(path +"/"+name,"");


    }
    
    public String getStackTrace(Exception e)
	{
		StringWriter sWriter = new StringWriter();
	    PrintWriter pWriter = new PrintWriter(sWriter);
	    e.printStackTrace(pWriter);
	    return sWriter.toString();
	}
	
    public boolean validateTest(int num, String element, Selenium browser, Log log )
	{
			boolean flag=true;
			
			try{
				if (num==1){
		        	if (!browser.isElementPresent(element)){
		        		flag=false;
		        	}
		        }
		        if (num==2){
		        	if (!browser.isTextPresent(element)){
		        		flag=false;
		        	}
		        }
        
		} catch (Exception e){
		  	log.error(e.getMessage());
		}
			
		if (flag==true)	
			log.info(element+" is present");
		
	    return flag;
	}
   
    public boolean waitforElementText(Selenium browser,String elementText, Log log){ 
		
		int second; 
        boolean flag=false;
        
        try { 
                for (second = 0; ; second++) { 
	                if (second >= 30) { 
	                        break; 
	                } 
	                if (elementText.contains("/") || elementText.contains("[") || elementText.contains("]")){
		                if (browser.isElementPresent(elementText)) { 
		                		log.info("Found the element: "+elementText);
		                		flag=true;
		                        break; 
		                } 
	                }else{
		                if (browser.isTextPresent(elementText)) { 
		                	log.info("Found the text: "+elementText);
	                		flag=true;
	                        break; 
		                } 
	                }
	                waitTime(1); 
                } 
        }catch(Exception e) { 
        	e.printStackTrace(); 
            log.error("Exception occured while waiting for "+elementText ); 
        } 
        
        if (flag==false)
        	log.error("Error cannot logout in 30 seconds, no "+elementText ); 
        
        return flag;
    } 
    
    /*********************************************************************************************
     * Wait for title
     * 
     * @param browser
     * @param title
     * @return void
     * @throws InterruptedException
    **********************************************************************************************/
    public void waitForTitle (Selenium browser, String title) throws InterruptedException{
	    
    	browser.waitForPageToLoad("30000");
		
    	for (int second = 0;; second++) {
    		
			if (second >= 60) fail("timeout");
			
			try { 
				if (title.equals(browser.getTitle())) 
					break; 
			} catch (Exception e) {}
			
			waitTime(1); 
		}
    }
    
    /*********************************************************************************************
     * getTarget(String pageName, String uiElementName)
     * returns translated 'target' element set by objectXml and element uiobject
     * 
     * @param pageName
     * @param uiElementName
     * @return String
     * @throws Exception
    **********************************************************************************************/
	
    public String getXpath(String uiElementName,String pageName) throws Exception
    {
    	Document dom = getXMLPage(objectXml);
    	Element racine = dom.getDocumentElement();
    	
    	NodeList pages = racine.getElementsByTagName("page");
    	//System.out.println(pages.getLength());
    	for (int i=0; i<pages.getLength();i++)
    	{
    		Element e = (Element) pages.item(i);
    		String pageValue = e.getAttribute("name");
    		//System.out.println("\nPage = " + pageValue);
    		if(pageValue.equalsIgnoreCase(pageName))
    		{
	    			//System.out.println("\ninsidethis");
	    		NodeList uiElements = e.getElementsByTagName("uiobject");
	    		for (int j=0; j<uiElements.getLength();j++)
	        	{
	        		
	    			Element ui_e = (Element) uiElements.item(j);
	    		
		    		String UIObjectValue = ui_e.getAttribute("name");
		    		//System.out.println("\n UI ObjectName = " + UIObjectValue);
		    		Node targetNode = ui_e.getElementsByTagName("locator").item(0);
		    		String targetValue = targetNode.getTextContent();
		    		//System.out.println("target value = " + targetValue);
		    		
		    		if (uiElementName.compareTo(UIObjectValue)==0)
		    		{
		    			return targetValue.trim();
		    		}
	        	}
        	}
    	}
    	return null;
    }
    
	/*********************************************************************************************
	 * public static Document getXMLPage
	 * Returns a DOM corresponding to File passed 
	 * 
	 * @param fileToParse
	 * @return Document
	 * @throws Exception
	 *********************************************************************************************/
	
	public Document getXMLPage(File fileToParse) throws Exception 
	{
		Document document = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		document = builder.parse(fileToParse);
		return document;
	}

	public void waitTime(int seconds) throws InterruptedException{
		
		int milliSeconds = seconds * 1000 ; 
		Thread.sleep(milliSeconds);	
	
	}
}
