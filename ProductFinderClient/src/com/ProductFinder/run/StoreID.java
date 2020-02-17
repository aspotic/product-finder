package com.ProductFinder.run;

/**
 * The ID of the store and server currently in use
 * @author adam knox
 */
public class StoreID {
	private static long storeID = 6003;
	//private static final String serverLocation = "http://10.0.2.2:8888/";
	private static final String serverLocation = "http://cmpt436project.appspot.com/";
	
	public static long getID() {
		return storeID;
	}

	public static String getServerlocation() {
		return serverLocation;
	}
}
