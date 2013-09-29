package com.apigee.sample.books.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.Log;
import com.apigee.sdk.ApigeeClient;
import com.apigee.sdk.data.client.DataClient;
import com.apigee.sdk.data.client.entities.Entity;
import com.apigee.sdk.data.client.response.ApiResponse;

public class ApigeeDataService{

	ApigeeClient apigeeClient = null;
    
    private ApigeeClient getApigeeClient(Context appContext) {
    	
    	if (apigeeClient == null) {
    		
    		 String APPNAME = "sandbox";
    		    
    		// Dit
    		String ORGNAME = "indit"; // <-- Put your username here!!!
    	    String APPURL = "http://apigee-internal-prod.jupiter.apigee.net";
    	    
    	    // Production
    	    //String ORGNAME = "tlee";
    	    //String APPURL = "http://api.usergrid.com";
    	    try {
    	    	apigeeClient = new ApigeeClient(ORGNAME,APPNAME,APPURL, appContext);
    	    } catch (Exception e){
    			Log.e("Error", "Apegee is not available");
    			Log.e("Error", "Exception in getApigeeClient"+e.getMessage());
    		}
    	}
    	
    	return apigeeClient;
    }
    
    private DataClient getDataClient(Context appContext) {
    	
    	ApigeeClient apigeeClient = getApigeeClient(appContext);
    	
		if (apigeeClient != null) {
			return apigeeClient.getDataClient();
		}
		
		return null;
	}
    
    /**
     * Get list of books from apigee server
     * @param appCotext
     * @return
     */
    public List<String> getBookList(Context appCotext) {
		
		List<String> titles = new ArrayList<String>();
		
    	try{
    		
	        DataClient client = getDataClient(appCotext);
	        if (client != null) {
	        	ApiResponse response = client.getEntities("books", "select *");
	        	if (response != null) {
	        		List<Entity> books = response.getEntities();
	                
	        		for (int j = 0; j < books.size(); j++) {
	        			Entity book = books.get(j);
	        			String bookTitle = book.getStringProperty("title");
	        			titles.add(bookTitle);
	        		}
	        		
	        	} else {
	            	Log.e("Error", "Error to get book title list.");	            	
	        	}
	        }
	        
    	}catch(Exception e){
    		Log.e("Error", "Error to get Apigee client.");
    		Log.e("Error", "Exception in getBookList: "+e.getMessage());
    	}
        
        return titles;
	}
    
    /**
     * Get single book from server
     * @param bookTitle
     */
	public Entity getBookByTitle(String bookTitle, Context appCotext) {
		Entity book = null;
		try {
			DataClient client = getDataClient(appCotext);
			if (client != null) {
				ApiResponse response = client.getEntities("books", "select * where title='" + bookTitle + "'");
	        	if (response != null) {
	        		List<Entity> books = response.getEntities();
	                
	        		if (books!=null && books.size()>0){
	        			book = books.get(0);
	        		}	
	        		
	        	} else {
	            	Log.e("Error", "Error to get book title list.");	            	
	        	}
			}
		}catch(Exception e){
    		Log.e("Error", "Error to get Apigee client.");
    		Log.e("Error", "Exception in getBookList: "+e.getMessage());
    	}
		
		return book;
	}
}
