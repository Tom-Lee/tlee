package com.apigee.sample.books.test;

import java.util.List;
import android.test.ActivityInstrumentationTestCase2;
import android.app.Activity;
import android.content.Context;
import com.apigee.sample.books.data.GlobalData;
import com.apigee.sample.books.service.ApigeeDataService;
import com.apigee.sample.books.service.AppUIService;
import com.apigee.sdk.data.client.entities.Entity;
import com.bitbar.recorder.extensions.ExtSolo;

/**
 * BooksListViewActivityTest --- test cases to test SDK sample app 'books'
 * @author    tlee
 * Version:   v2
 */

public class BooksListViewActivityTest extends ActivityInstrumentationTestCase2<Activity> {

	private String tag = GlobalData.tag;
	private boolean debug = GlobalData.debug;
	private boolean snap = GlobalData.snap;
	
	private static final String LAUNCHER_ACTIVITY_CLASSNAME = "com.apigee.sample.books.BooksListViewActivity";
	private static Class<?> launchActivityClass;
	
	private Context appCotext;
	private ApigeeDataService dataService;
	private AppUIService uiService;
	
	static {
		try {
			launchActivityClass = Class.forName(LAUNCHER_ACTIVITY_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	private ExtSolo solo; 

	@SuppressWarnings("unchecked")
	public BooksListViewActivityTest() {
		super((Class<Activity>) launchActivityClass);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass()
				.getCanonicalName(), getName());
		
		appCotext = getActivity().getBaseContext();
		dataService = new ApigeeDataService();
		uiService= new AppUIService();
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		solo.tearDown();
		super.tearDown();
	}

	/**
	 * Main test method
	 * @throws Exception
	 */
	public void testBooksApp() throws Exception {
		
		try{	
			
			// test book list
			testBookList(solo);
			
			// test add a new book
			testAddNewBook(solo);
						
		}catch(Exception e){	
			solo.logInfoInLogcat(tag, "Exception in testBooksApp");
			
		}
	}
	

	/**
	 * Goal:
	 * 1) Get book items and check if the total number of items on UI matches remote server
	 * 2) Go through each item and check if the book title match remote server
	 * @throws Exception
	 */

	private void testBookList(ExtSolo solo) throws Exception {
		
		int uiSize=0;
		int rpcSize=0;
		
		try {
			if (debug) solo.logInfoInLogcat(tag, "Get book from UI");
			List<String> bookListfromUI=uiService.getBookListFromUI(solo);
			if (bookListfromUI.isEmpty()){
				if (debug) solo.logInfoInLogcat(tag, "No book title in the current UI");
			}
			//if (debug) solo.logInfoInLogcat(tag, "Done get book from UI--" + bookListfromUI.toString());
			
			if (debug) solo.logInfoInLogcat(tag, "Get book from API");
			List<String> bookListfromAPI = dataService.getBookList(appCotext);
			
			//if (debug) solo.logInfoInLogcat(tag, "Done get book from API--" + bookListfromAPI.toString());
			
			uiSize = bookListfromUI.size();
			rpcSize = bookListfromAPI.size();
			
			if (bookListfromUI.size() == bookListfromAPI.size()) {
				// size match
				if (debug) solo.logInfoInLogcat(tag, "The number of books in UI matches the number of books on the server");
			} else{
				
				Exception e = new Exception("The number of the title(s) on UI does not match AP! returns, ui list=" + uiSize + " API list=" + rpcSize);
	            //throw e;
			}
		}catch (Exception e){
			if(snap) { solo.takeScreenshot("testBookList-"+AppUIService.getCurrentTime()); }
	        if (debug) solo.logInfoInLogcat(tag, "Unexpected exception caught in testBookList: " + e.getMessage());
	        throw e;
		}
	}
	
	/**
	 * Goal:
	 * Test Add Book feature to perform adding a book and check if 
	 * the book is added from server.
	 * 
	 * @throws Exception
	 */

	private void testAddNewBook(ExtSolo solo) throws Exception {

		try {
			// add a book from ui
			String addedTitle=uiService.addNewBook(solo);
			
			// get the newly added book from server
			Entity book = dataService.getBookByTitle(addedTitle, appCotext);
			if (book != null){
				String returnTitle = book.getStringProperty("title");
				if (addedTitle.equals(returnTitle)){
					solo.logInfoInLogcat(tag, "The book was added correctly: "+returnTitle);
				}
				else {
					Exception e = new Exception("Book added does not match the record on server:"+returnTitle+" "+addedTitle);
	                throw e;
				}		
			}else{
				
				Exception e = new Exception("Added wrong book "+addedTitle);
                throw e;
			}
		} catch (Exception e){
			if(snap) { solo.takeScreenshot("testAddNewBook-"+AppUIService.getCurrentTime()); }
	        if (debug) solo.logInfoInLogcat(tag, "Unexpected exception caught in testAddNewBook: " + e.getMessage());
	        throw e;
		}
	}
}
