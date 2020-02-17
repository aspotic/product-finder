package com.ProductFinder.Servlets;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.ProductFinder.JDO.PMF;
import com.ProductFinder.dataStructure.Brand;
import com.ProductFinder.dataStructure.Item;
import com.ProductFinder.dataStructure.Store;

/**
 * Gives access to all brands on the server
 * @author adam knox
 * 
 * PARAMETERS
 * op createBrand:op,storeID, name, description
 * op modifyBrand:op, storeID, oldName, newName, newDescription
 * op removeBrand:op, storeID, name
 * op getBrands:op, storeID
 *
 */

@SuppressWarnings("serial")
public class AccessBrand extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) { 
		if ("createBrand".equals(request.getParameter("op"))) {
			//Declare Variables
			String storeID = "";
		    String brandName = "";
		    String brandDescription = "";
		    
		    //Get the brand parameters
		    try {
		    	storeID = request.getParameter("storeID");
		    	brandName = request.getParameter("name");
		    	brandDescription = request.getParameter("description");
		    } catch(Exception e) {
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e1) {}
		    }
		    
		    //Check if the brand already exists
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        Store store = (Store) pm.getObjectById(Store.class, Integer.parseInt(storeID));
		    
	        if (!store.getBrands().contains(brandName)) {
	    	    //Create the brand since it doesn't exist
	    	    Brand brand = new Brand(brandName, brandDescription);
		        
			    //Persistently store the brand
		        pm.makePersistent(brand);
		        
		        //Add the brand to the store
		        store.addBrand(brandName);
		        
		        //Do the changes
		        pm.close();
		        
		        //Notify the user the creation was successful
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(true);
					toClient.flush();
					toClient.close();
				} catch (IOException e) {}
				
		        
			//The brand exists, so don't create it
	        } else {
	        	
		        //Notify the user the creation failed
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e) {}
	        }
		} else if ("modifyBrand".equals(request.getParameter("op"))) {
			//Declare Variables
			String storeID = "";
		    String oldBrandName = "";
		    String newBrandName = "";
		    String newBrandDescription = "";
		    
		    //Get the brand parameters
		    try {
		    	storeID = request.getParameter("storeID");
		    	oldBrandName = request.getParameter("oldName");
		    	newBrandName = request.getParameter("newName");
		    	newBrandDescription = request.getParameter("newDescription");
		    } catch(Exception e) {
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e1) {}
		    }
		    
		    //Ensure the Brand Exists and the new brand name doesn't exist yet
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        Store store = (Store) pm.getObjectById(Store.class, Integer.parseInt(storeID));	        

	        if ((store.getBrands().contains(oldBrandName)) && (!store.getBrands().contains(newBrandName))) {	
	        	//Rename the brand in the Store list
	        	store.modifyBrand(oldBrandName, newBrandName);
	        	
	        	//Update the actual brand
	        	Brand brand = (Brand) pm.getObjectById(Brand.class, oldBrandName);	 
	        	Brand newBrand = new Brand(newBrandName, newBrandDescription, brand.getItems());
	        	pm.deletePersistent(brand);
	        	pm.makePersistent(newBrand);

	        	//Update all items contained within the brand
	        	for (String itemName : newBrand.getItems()) {
		        	Item item = (Item) pm.getObjectById(Item.class, itemName);	 
	        		item.brand = newBrandName;
	        	}

		        //Do the changes
		        pm.close();
		        
		        //Notify the user the modification was successful
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(true);
					toClient.flush();
					toClient.close();
				} catch (IOException e) {}
				
			//The brand doesn't exist or the new one already exists, so don't modify
	        } else {
		        //Notify the user the modification failed
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e) {}
	        }
		} else if ("removeBrand".equals(request.getParameter("op"))) {
			//Declare Variables
			String storeID = "";
		    String brandName = "";
		    
		    //Get the brand parameters
		    try {
		    	storeID = request.getParameter("storeID");
		    	brandName = request.getParameter("name");
		    } catch(Exception e) {
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e1) {}
		    }
		    
		    //Check if the brand exists
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        Store store = (Store) pm.getObjectById(Store.class, Integer.parseInt(storeID));
		    
	        if (store.getBrands().contains(brandName)) {
	        	Brand brand = (Brand) pm.getObjectById(Brand.class, brandName);	 
	        	
	        	//Remove all items under this brand name
	        	for (String itemName : brand.getItems()) {
		        	Item item = (Item) pm.getObjectById(Item.class, itemName);	 
	        		pm.deletePersistent(item);
	        	}
	        	
	        	//Remove the actual brand
	        	pm.deletePersistent(brand);
	        	
	        	//Remove reference in Store
	        	store.removeBrand(brandName);
	        	
		        //Do the changes
		        pm.close();
		        
		        //Notify the user the removal was successful
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(true);
					toClient.flush();
					toClient.close();
				} catch (IOException e) {}
				
		        
			//The brand doesn't exist, so don't remove it
	        } else {
	        	
		        //Notify the user the creation failed
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e) {}
	        }
		}
	}
	
	
	
	/**
	 * Gets the user the list of the brands in the store
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Only send the data if the proper command is given
		if ("getBrands".equals(request.getParameter("op"))) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
		
			//Create a list of the store's brands
			ObjectOutputStream responseObject = new ObjectOutputStream(response.getOutputStream());
			responseObject.writeObject(pm.getObjectById(Store.class, Integer.parseInt(request.getParameter("storeID"))).getBrands());
			responseObject.flush();
			responseObject.close();
		}
	}
}