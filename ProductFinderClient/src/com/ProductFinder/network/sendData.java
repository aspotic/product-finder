package com.ProductFinder.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;

import com.ProductFinder.dataStructure.ClientItem;
import com.ProductFinder.run.StoreID;

/**
 * Sends data to the server
 * @author adam knox
 *
 */
public class sendData {
	private static final long storeID = StoreID.getID();
	private static final String serverLocation = StoreID.getServerlocation();

	
	
	/**
	 * Sends a new item to the server for storage
	 * @param item the item to send
	 * @return true if storage was successful, false if it wasn't
	 */
	public static boolean sendNewItem(ClientItem item) {
		item.store = storeID;
		Boolean result = (Boolean) POST(item, "AccessItem?op=createItem");
		
		if (result == null) {
			return false;
		} else if (!result) {
			return false;
		} else {
			return true;
		}
	}

	
	
	/**
	 * Sends an item to the server to replace an existing item
	 * @param item the item to replace the existing one
	 * @param oldName the name of the existing item
	 * @return true if replacement was successful, false if it wasn't
	 */
	public static boolean sendModifiedItem(ClientItem item, String oldName) {
		Boolean result = (Boolean) POST(item, "AccessItem?op=modifyItem&oldName=" + Uri.encode(oldName));
		
		if (result == null) {
			return false;
		} else if (!result) {
			return false;
		} else {
			return true;
		}
	}
	
	
	
	/**
	 * removes the given item from the server
	 * @param itemName the name of the item to remove
	 * @return true if it was removed, false if it wasn't
	 */
	public static boolean removeItem(String itemName) {
		Boolean result = (Boolean) POST(null, "AccessItem?op=removeItem&name=" + Uri.encode(itemName));
		
		if (result == null) {
			return false;
		} else if (!result) {
			return false;
		} else {
			return true;
		}
	}
	
	

	/**
	 * Sends the passed in object to the server, and returns the object response
	 * @param data the object to send to the server
	 * @param command the name of the servlet to use
	 * @return the object to get from the server
	 */
	private static Object POST(Object data, String command) {
		Object response = null;
		
		try {
			//Setup URL for a POST command
			HttpURLConnection urlConn =  (HttpURLConnection) new URL(serverLocation + command).openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setDefaultUseCaches(false);
			urlConn.setRequestProperty("Content-Type", "application/octet-stream");

			//Send POST command
			ObjectOutputStream toServer = new ObjectOutputStream(urlConn.getOutputStream());
			toServer.writeObject(data);
			toServer.flush();

			//Receive Server's Response
			ObjectInputStream resultStream = null;
			try {
				resultStream = new ObjectInputStream(urlConn.getInputStream());
				response = (Object) resultStream.readObject();
				resultStream.close();
			} catch (Exception e) {
				response = null;
			}
			
			toServer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
}



