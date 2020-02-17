package com.ProductFinder.network;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import com.ProductFinder.dataStructure.ClientItem;
import com.ProductFinder.run.StoreID;

/**
 * the functions used for accessing the server.  mainly here for simplifying casts
 * @author adam knox
 *
 */
public class getData {
	private static final String storeID = String.valueOf(StoreID.getID());
	private static final String serverLocation = StoreID.getServerlocation();

	
	
    /**
     * get a list of the store's departments from the server
     * @return a list of the store's current departments
     */
	@SuppressWarnings("unchecked")
	public static LinkedList<String> getDepartments() {
		return (LinkedList<String>) GET("AccessDepartment?storeID=" + storeID + "&op=getDepartments");
	}
	
	
	
    /**
     * get a list of the item's limited by the request parameters
     * @param parameters each filter to use. should be of form <name>=<value>&<name2>=<value2>...
     * @return a list of the store's item's
     */
	@SuppressWarnings("unchecked")
	public static LinkedList<ClientItem> getItemResults(String parameters) {
		return (LinkedList<ClientItem>) GET("AccessItem?storeID=" + storeID + "&op=getItems&" + parameters);
	}
    
	
    
    /**
     * get a list of the brands in the selected department from the server
     * @param selectedDepartment the department to get the brands for
     * @return a list of the brands in the selected department
     */
	@SuppressWarnings("unchecked")
	public static LinkedList<String> getBrands() {
		return (LinkedList<String>) GET("AccessBrand?storeID=" + storeID + "&op=getBrands");
	}
	
	
	
	/**
	 * Gets an object from the server using the servlet named 'command'
	 * @param command the name of the servlet to use
	 * @return the object returned by the server
	 */
	private static Object GET(String command) {
		Object response = null;
		try {
			//Setup URL for a GET command
			URL url;
			url = new URL(serverLocation + command);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			//Send GET command
			urlConnection.getInputStream();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			
			//Read the server's response
			try {
				ObjectInputStream resultStream = null;
				resultStream = new ObjectInputStream(in);
				response = (Object) resultStream.readObject();
				resultStream.close();
			} catch (Exception e) {
				return null;
			}
			
			in.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			return null;
		}
		return response;
	}
}