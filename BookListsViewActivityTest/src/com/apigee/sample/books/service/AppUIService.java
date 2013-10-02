package com.apigee.sample.books.service;

import com.apigee.sample.books.data.GlobalData;
import static org.junit.Assert.assertNotNull;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.bitbar.recorder.extensions.ExtSolo;


@SuppressLint("SimpleDateFormat")
public class AppUIService  {
	
	/**
	 * Test add book and check result from UI
	 * @param solo
	 * @throws Exception
	 */
	public String addNewBook(ExtSolo solo) throws Exception {
		
		Random generator = new Random();
		int  n = generator.nextInt(9998) + 1;
		
		String title = "Test title " + n;
		String author ="Test author";
		
		try {
			
			//Click the Add book button 			
			solo.clickOnMenuItem("Add Book", true);
			
			if (solo.waitForActivity(GlobalData.newBookActivity_activity, 2000)){

					if (solo.waitForEditTextById(GlobalData.title_id, 2000) && solo.waitForEditTextById(GlobalData.author_id, 2000) ){
						
						//Type in title and author
						solo.enterText((EditText) solo.findViewById(GlobalData.title_id),title);
						solo.enterText((EditText) solo.findViewById(GlobalData.author_id), author);
						
					}else{
						
						Exception e = new Exception("title id or author id is not available");
	                    throw e;
					}
			}else{
				
				Exception e = new Exception("'"+GlobalData.newBookActivity_activity+"' is not avilable, the UI cannot be displayed");
                throw e;
			}
			
			if(solo.waitForButtonById(GlobalData.create_id, 2000)){
				//Tap create button
				solo.clickOnButton((Button) solo.findViewById(GlobalData.create_id));
				solo.waitForActivity(GlobalData.booksListView_activity);
				solo.sleep(3000);
			}else{
				Exception e = new Exception("the create button is not available");
                throw e;
			}
			
		} catch (Exception e) {
			solo.fail("com.apigee.sample.books.test.BooksListViewActivityTest.testAddNewBook_scr_fail",e);
			throw e;
			
		}
		
		return title;
	}
	
	/**
	 * Get list of book titles from UI
	 * @param solo
	 * @return
	 * @throws Exception
	 */

	public List<String> getBookListFromUI(ExtSolo solo) throws Exception {
		
		List<View> views = new ArrayList<View>(); 
		int i = 0; 
		TextView textView=null;
		ListView group=null;
		List<String> titleList=new ArrayList<String>();
		
		try{
			
			if(solo.waitForText("Test title", 10000) && solo.waitForActivity(GlobalData.booksListView_activity,10000) ) {
				//Get all the views 
				views=solo.getViews();
				
				if (views != null && !views.isEmpty()){
					//Loop through each view
					for (i=0;i<views.size();i++){
						
						if (views.get(i).getId()!=-1 && views.get(i).getClass().getName().contains("ListView")){
	
							group=(ListView) views.get(i);	
							//Loop through ListView to find all the titles
							for (i=0;i<=group.getChildCount();i++){
								
								if (group.getChildAt(i)!=null){
									
					                if (group.getChildAt(i).getClass().getName().contains("TextView")){
					                	
					                	textView=(TextView) group.getChildAt(i);
					                	assertNotNull("The return book cannot be null", textView.getText());
					                	titleList.add(textView.getText().toString());
					          
					                }
								}
							}
							
							break;
						}
					}
				}
			}
        
	    }catch(Exception e){
	    	if(GlobalData.snap) { solo.takeScreenshot("checkSearchResult-"+  getCurrentTime()); }
	        if (GlobalData.debug) solo.logInfoInLogcat(GlobalData.tag, "Unexpected exception caught in getBookListFromUI: " + e.getMessage());
	        throw e;
	    }
		
		return  titleList;
	}

	public static String getCurrentTime(){
		
		String systemLog_logging_time="";
		long timeStamp = java.lang.System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultDate = new Date(timeStamp);
        systemLog_logging_time=sdf.format(resultDate);
        systemLog_logging_time=systemLog_logging_time.replaceAll("[^a-zA-Z0-9]", "-");
        
        return systemLog_logging_time;
	}
}
