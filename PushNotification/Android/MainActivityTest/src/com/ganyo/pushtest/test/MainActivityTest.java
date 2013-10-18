package com.ganyo.pushtest.test;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.test.ActivityInstrumentationTestCase2;
import android.app.Activity;
import com.bitbar.recorder.extensions.ExtSolo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 * The class is to auto-accept incoming push notifications, and verify them
 * @author: tlee
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<Activity> {

	private static final String LAUNCHER_ACTIVITY_CLASSNAME = "com.ganyo.pushtest.MainActivity";
	private static Class<?> launchActivityClass;
	private GlobalData data ;
	private long startTime;
    
	static {
		try {
			launchActivityClass = Class.forName(LAUNCHER_ACTIVITY_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	private ExtSolo solo; 
	@SuppressWarnings("unchecked")
	public MainActivityTest() {
		super((Class<Activity>) launchActivityClass);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass()
				.getCanonicalName(), getName());
		@SuppressWarnings("unused")
		GlobalData data = new GlobalData();
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		solo.tearDown();
		super.tearDown();
	}

	@SuppressWarnings("static-access")
	public void testPushNotification() throws Exception {

		List<String> returnMessageList=new ArrayList<String>();
		int count=0;
		boolean flag=false;
		String returnData;
		
		try {
			solo.logInfoInLogcat(data.tag, "Start push notification test at mobile side");
			solo.setActivityOrientation(ExtSolo.PORTRAIT);
			//solo.setActivityOrientation(ExtSolo.LANDSCAPE);
			solo.sleep(2000);
			
			while (!solo.waitForButtonById(data.sendButton_id, 1000) && solo.waitForActivity(data.main_activity, 1000)){
				Button b=solo.getButton("OK");
				if (b!=null)
					solo.clickOnButton("OK");
				else
					solo.goBack();
			}
			
			solo.logInfoInLogcat(data.tag, "Found Send button and MainActivity");
			
			startTime=start();
			
			while (stop()-startTime<=150000){
				
				if (solo.searchButton("OK", 1, true) && solo.waitForButton("OK", 1000)){	
					solo.clickOnButton("OK");
					waitTime(3);
					count++;
					if (count==data.send){
						returnMessageList=findText(solo, data.msg);
						if (returnMessageList.size()>=1){
							Iterator<String> i = returnMessageList.iterator();
							if(i.hasNext()){
								returnData=i.next();
								if (findMsg(returnData.toString())==data.send){
									solo.logInfoInLogcat(data.tag, "Received 2 pushed notifications correctly: "+returnData);
								}else{
									Exception e = new Exception("Received incorrect number of push notification(s):" + returnData);
						            throw e;
								}	 									 
							}
							solo.logInfoInLogcat(data.tag, "Done with push notification test at mobile side");
							flag=true;
							break;
						}else{
							Exception e = new Exception("Received 0 push notifications");
			                throw e;
						}
					}		
				}
			}
			
		}catch (Exception e){
			solo.fail("Fail at testPushNotification",e);
			if(GlobalData.snap) { solo.takeScreenshot("testPushNotification-"+getCurrentTime()); }
	        if (GlobalData.debug) solo.logInfoInLogcat(GlobalData.tag, "Error: Unexpected exception caught in testPushNotification: " + e.getMessage());
			throw e;
		}
		
		if (flag==false)
			solo.logInfoInLogcat(data.tag, "Error: The push notification failed on mobile side, received "+count+" pushed notification(s)");
	}
	
	private List<String> findText(ExtSolo solo, String text) throws Exception{
		
		List<View> views = new ArrayList<View>(); 
		int i = 0; 
		TextView textView=null;
		List<String> messageList=new ArrayList<String>();
		
		try{
			
			if(solo.waitForText("Hello world!", 5000) ) {
				views=solo.getViews();				
				if (views != null && !views.isEmpty()){
					for (i=0;i<views.size();i++){						
						if (views.get(i).getClass().getName().contains("TextView")){
					        textView=(TextView) views.get(i);
					        assertNotNull("The received push notifications cannot be null", textView.getText());
					        if (textView.getText().toString().contains(text)){
					        	messageList.add(textView.getText().toString());
					        }
						}
					}
				}
			}
        
	    }catch(Exception e){
	    	if(GlobalData.snap) { solo.takeScreenshot("findText-"+getCurrentTime()); }
	        if (GlobalData.debug) solo.logInfoInLogcat(GlobalData.tag, "Error: Unexpected exception caught in findText: " + e.getMessage());
	        throw e;
	    }
			
		return messageList;		
	}
    
	private long start() {
        startTime = System.currentTimeMillis();
        return startTime;
    }
	
	private long stop() {
         return System.currentTimeMillis();         
    }
	
	private int findMsg(String returnedData){
		
	    Pattern p = Pattern.compile(GlobalData.msg);
	    Matcher m = p.matcher(returnedData);
	    int count = 0;
	    while (m.find()){
	        count +=1;
	    }
	    return count;
	}
	
	private String getCurrentTime(){
		
		String systemLog_logging_time="";
		long timeStamp = java.lang.System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultDate = new Date(timeStamp);
        systemLog_logging_time=sdf.format(resultDate);
        systemLog_logging_time=systemLog_logging_time.replaceAll("[^a-zA-Z0-9]", "-");
        
        return systemLog_logging_time;
	}
	
    private void waitTime(int seconds) throws InterruptedException{
		int milliSeconds = seconds * 1000 ; 
		Thread.sleep(milliSeconds);	
	}
}
