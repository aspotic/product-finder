package com.ProductFinder.Servlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.ProductFinder.JDO.PMF;
import com.ProductFinder.dataStructure.Brand;
import com.ProductFinder.dataStructure.ClientItem;
import com.ProductFinder.dataStructure.Department;
import com.ProductFinder.dataStructure.Item;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * Ohms law, induced EMF
 * More emphasis on Magnetostatics (chapter 5 +)
 */

/**
 * Gives access to all items on the server
 * @author adam knox
 * 
 * PARAMETERS
 * op createItem:op,storeID, name
 * op modifyItem:op, storeID, oldName, UserItem object
 * op removeItem:op, storeID, name
 * op getItems:op, storeID, returnType, at least one of: name, brand, department, MaxPrice
 *
 */

@SuppressWarnings("serial")
public class AccessItem extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException { 
		if ("createItem".equals(request.getParameter("op"))) {
		    //Get the Item object
			ClientItem clientItem = null;
			try {
				ObjectInputStream fromClient;
				fromClient = new ObjectInputStream(request.getInputStream());
				clientItem = (ClientItem) fromClient.readObject();
				fromClient.close();
			} catch (Exception e) {
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e1) {}
			}

			try {
				//Create a JDO Version of the item
				Item item = new Item(clientItem);
				
		        //Add the item to persistent storage
		        PersistenceManager pm = PMF.get().getPersistenceManager();
	        	pm.makePersistent(item);
	
	        	//Add the item to the appropriate department
		        Department department = (Department) pm.getObjectById(Department.class, item.department);
		        if (!department.getItems().contains(item.name)) {
		        	department.addItem(item.name);
				}
		        
	        	//Add the item to the appropriate brand
		        Brand brand = (Brand) pm.getObjectById(Brand.class, item.brand);
		        if (!brand.getItems().contains(item.name)) {
		        	brand.addItem(item.name);
		        }
		        
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
				} catch (IOException e1) {}
			} catch (Exception e) {
				//Notify the user the creation failed
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e1) {}
			}
			
			
			
		} else if ("modifyItem".equals(request.getParameter("op"))) {
		    //Get the old item name
			String oldName = null;	
			if ((oldName = request.getParameter("oldName")) != null) {
		        PersistenceManager pm = PMF.get().getPersistenceManager();
		        
		        //Get the updated Item
				ClientItem clientItem = null;
				try {
					ObjectInputStream fromClient;
					fromClient = new ObjectInputStream(request.getInputStream());
					clientItem = (ClientItem) fromClient.readObject();
					fromClient.close();
				} catch (Exception e) {
					try {
						ObjectOutputStream toClient;
					    response.setContentType("application/octet-stream");
						toClient = new ObjectOutputStream(response.getOutputStream());
						toClient.writeObject(false);
						toClient.flush();
						toClient.close();
					} catch (IOException e1) {}
				}
				
				//Create the new item
				Item newItem = new Item(clientItem);
				
				//Get the item currently in persistence
		        Item oldItem = (Item) pm.getObjectById(Item.class, oldName);
				
		        //Get the department and brand lists
		        Department department = (Department) pm.getObjectById(Department.class, oldItem.department);
		        Brand brand = (Brand) pm.getObjectById(Brand.class, oldItem.brand);
		        
				

				//Update department and brand lists if the name changed and it didn't change to a name that already exists
				if (!newItem.name.equals(oldName)) {
					//If the new name already exists, then fail
					if (department.getItems().contains(newItem.name)) {
						//Notify the user the item change failed
						try {
							ObjectOutputStream toClient;
						    response.setContentType("application/octet-stream");
							toClient = new ObjectOutputStream(response.getOutputStream());
							toClient.writeObject(false);
							toClient.flush();
							toClient.close();
						} catch (IOException e1) {}
						return;
					}
					
					department.removeItem(oldName);
					department.addItem(newItem.name);
					
					brand.removeItem(oldName);
					brand.addItem(newItem.name);
				}
				
				//Update brand lists if the brand changed
				if (!newItem.brand.equals(oldItem.brand)) {
					//Remove from existing brand
					brand.removeItem(oldName);
					
					//Add to new brand
			        Brand newBrand = (Brand) pm.getObjectById(Brand.class, newItem.brand);
			        if (!newBrand.getItems().contains(newItem.name)) {
			        	newBrand.addItem(newItem.name);
			        }
				}

				//Update department lists if the department changed
				if (!newItem.department.equals(oldItem.department)) {
					//Remove from existing department
					department.removeItem(oldName);
					
					//Add to new department
					Department newDepartment = (Department) pm.getObjectById(Department.class, newItem.department);
			        if (!newDepartment.getItems().contains(newItem.name)) {
			        	newDepartment.addItem(newItem.name);
			        }
				}

				//Replace the current item with the updated item
				pm.deletePersistent(oldItem);
				pm.makePersistent(newItem);
				
				//Do Changes
				pm.close();

				//Notify the user the item change succeeded
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(true);
					toClient.flush();
					toClient.close();
				} catch (IOException e1) {}
			} else {
				//Notify the user the item change failed
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e1) {}
			}
			
			
		} else if ("removeItem".equals(request.getParameter("op"))) {
		    //Get the name of the item to remove
			String name = null;	
			if ((name = request.getParameter("name")) != null) {
		        PersistenceManager pm = PMF.get().getPersistenceManager();

				//Remove the item and store its brand and department
		        Item item = (Item) pm.getObjectById(Item.class, name);
				String departmentName = item.department;
				String brandName = item.brand;
	        	pm.deletePersistent(item);
				
				//Remove the item from department
		        Department department = (Department) pm.getObjectById(Department.class, departmentName);
		        if (department.getItems().contains(name)) {
		        	department.removeItem(name);
				}
	
	        	//Add the item to the appropriate brand
		        Brand brand = (Brand) pm.getObjectById(Brand.class, brandName);
		        if (brand.getItems().contains(name)) {
		        	brand.removeItem(name);
		        }

		        //Do the changes
		        pm.close();

				//Notify the user the item removal succeeded
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(true);
					toClient.flush();
					toClient.close();
				} catch (IOException e1) {}
			} else {
				//Notify the user the item removal failed
				try {
					ObjectOutputStream toClient;
				    response.setContentType("application/octet-stream");
					toClient = new ObjectOutputStream(response.getOutputStream());
					toClient.writeObject(false);
					toClient.flush();
					toClient.close();
				} catch (IOException e1) {}
			}
		}
	}
	
	
	
	/**
	 * Gets the user the list of the items in the store that fit to the given parameters
	 * Parameters
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Only send the data if the proper command is given
		if ("getItems".equals(request.getParameter("op"))) {
			//Create the item list
			LinkedList<ClientItem> items = new LinkedList<ClientItem>();
			
			//Set the return type
			String returnType = null;
			if ((returnType = request.getParameter("returnType")) == null) {
				returnType = "";
			}
			
			//Get parameters
			//Set the department
	    	String DepartmentVal;
	    	if ((DepartmentVal = request.getParameter("department")) != null) {
		    	if (DepartmentVal.length() <= 0) {
		    		DepartmentVal = null;
		    	}
	    	}
	    	
	    	//Set the brand
	    	String BrandVal;
	    	if ((BrandVal = request.getParameter("brand")) != null) {
		    	if (BrandVal.length() <= 0) {
		    		BrandVal = null;
		    	}
	    	}
			
			//Set the price limit
			Double MaxPrice = null;
			try {
				MaxPrice = Double.parseDouble(request.getParameter("MaxPrice"));
			} catch (Exception e) {
				MaxPrice = null;
			}
			
			
			//If a name is given, then just get the item with that name.  If it doesn't exist, then use the other approach

			//Create the query
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			//Parse and return the results using the proper output
			Entity singleItem;
			uniqueResult:
			try {
				Key key = KeyFactory.createKey("Item", request.getParameter("name"));
				singleItem = datastore.get(key);
			
				if (singleItem != null) {
					//Check that the other conditions specified are also met

			    	//Check the department
			    	if (DepartmentVal != null) {
						if (!singleItem.getProperty("department").equals(DepartmentVal)) {
							break uniqueResult;
						}
			    	}
			    	
			    	//Set the brand
			    	if (BrandVal != null) {
						if (!singleItem.getProperty("brand").equals(BrandVal)) {
							break uniqueResult;
						}
			    	}
					
					//Set the price limit
			    	if (MaxPrice != null) {
						if (Double.parseDouble((String) singleItem.getProperty("price")) > MaxPrice) {
							break uniqueResult;
						}
			    	}
					
			    	
					if (returnType.equals("html")) {
						String resultString = "Name: " + singleItem.getKey().getName() + "; ";
						for (String var : singleItem.getProperties().keySet()) {
							try {
								resultString += var + ": " + singleItem.getProperty(var) + " ";
							} catch (Exception e) {}
						}
						
						//Reply with an html page
						response.setContentType("text/html");
					    PrintWriter out = response.getWriter();
					    out.println("<html>");
					    out.println("<head><title>Product Finder</title></head>");
					    out.println("<body>");
						out.println(resultString);
					    out.println("</body></html>");
					    out.close();
						
					} else {
						//Convert the entity to a ClientItem
						ClientItem item = new ClientItem(singleItem.getKey().getName(),
								(String) singleItem.getProperty("department"),
								(String) singleItem.getProperty("brand"),
								(Long) singleItem.getProperty("serial"),
								(String) singleItem.getProperty("description"),
								(Double) singleItem.getProperty("price"),
								(Long) singleItem.getProperty("isle"),
								(Long) singleItem.getProperty("shelf"),
								(Double) singleItem.getProperty("xPosition"),
								(Double) singleItem.getProperty("yPosition"));
						item.store = (Long) singleItem.getProperty("store");
						items.add(item);
						
						//Return the object
						ObjectOutputStream responseObject = new ObjectOutputStream(response.getOutputStream());
						responseObject.writeObject(items);
						responseObject.flush();
						responseObject.close();	
					}
					
					return;
				}
			} catch (Exception e) {}

				

			//Return the list of best matches
			//Setup the Item query
			Query query = new Query("Item");
			
			//Set the store
			query.addFilter("store", Query.FilterOperator.EQUAL, Integer.parseInt(request.getParameter("storeID")));

	    	//Set the department
	    	if (DepartmentVal != null) {
				query.addFilter("department", Query.FilterOperator.EQUAL, DepartmentVal);
	    	}
	    	
	    	//Set the brand
	    	if (BrandVal != null) {
				query.addFilter("brand", Query.FilterOperator.EQUAL, BrandVal);
	    	}
			
			//Set the price limit
	    	if (MaxPrice != null) {
				query.addFilter("price", Query.FilterOperator.LESS_THAN_OR_EQUAL, MaxPrice);
	    	}

			//Parse and return the results using the proper output
			PreparedQuery pq = datastore.prepare(query);
			
			if (returnType.equals("html")) {
				LinkedList<String> stringItems = new LinkedList<String>();
				for (Entity result : pq.asIterable()) {
					String resultString = "Name: " + result.getKey().getName() + "; ";
					for (String field : result.getProperties().keySet()) {
						try {
							resultString += field + ": " + result.getProperty(field) + " ";
						} catch (Exception e) {}
					}
					stringItems.add(resultString);
				}
				
				//Reply with an html page
				response.setContentType("text/html");
			    PrintWriter out = response.getWriter();
			    out.println("<html>");
			    out.println("<head><title>Product Finder</title></head>");
			    out.println("<body>");
			    for (String item : stringItems) {
				    out.println(item + "<br />");
			    }
			    out.println("</body></html>");
			    out.close();
				
			} else {
				
				//Form a linked list of client items from the prepared query of entities
				for (Entity result : pq.asIterable()) {
					ClientItem item = new ClientItem(result.getKey().getName(),
							(String) result.getProperty("department"),
							(String) result.getProperty("brand"),
							(Long) result.getProperty("serial"),
							(String) result.getProperty("description"),
							(Double) result.getProperty("price"),
							(Long) result.getProperty("isle"),
							(Long) result.getProperty("shelf"),
							(Double) result.getProperty("xPosition"),
							(Double) result.getProperty("yPosition"));
					item.store = (Long) result.getProperty("store");
					items.add(item);
				}
				
				//Return the filtered list as an object
				ObjectOutputStream responseObject = new ObjectOutputStream(response.getOutputStream());
				responseObject.writeObject(items);
				responseObject.flush();
				responseObject.close();	
			}
			
		}
	}
}