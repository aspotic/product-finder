package com.ProductFinder.Servlets;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.ProductFinder.JDO.PMF;
import com.ProductFinder.dataStructure.Department;
import com.ProductFinder.dataStructure.Item;
import com.ProductFinder.dataStructure.Store;

/**
 * Gives access to all departments on the server
 * @author adam knox
 * 
 * PARAMETERS
 * op createDepartment:op,storeID, name, description
 * op modifyDepartment:op, storeID, oldName, newName, newDescription
 * op removeDepartment:op, storeID, name
 * op getDepartments:op, storeID
 *
 */

@SuppressWarnings("serial")
public class AccessDepartment extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) { 
		if ("createDepartment".equals(request.getParameter("op"))) {
			//Declare Variables
			String storeID = "";
		    String departmentName = "";
		    String departmentDescription = "";
		    
		    //Get the department parameters
		    try {
		    	storeID = request.getParameter("storeID");
		    	departmentName = request.getParameter("name");
		    	departmentDescription = request.getParameter("description");
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
		    
		    //Check if the department already exists
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        Store store = (Store) pm.getObjectById(Store.class, Integer.parseInt(storeID));
		    
	        if (!store.getDepartments().contains(departmentName)) {
	    	    //Create the department since it doesn't exist
	    	    Department department = new Department(departmentName, departmentDescription);
		        
			    //Persistently store the department
		        pm.makePersistent(department);
		        
		        //Add the department to the store
		        store.addDepartment(departmentName);
		        
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
				
		        
			//The department exists, so don't create it
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
		} else if ("modifyDepartment".equals(request.getParameter("op"))) {
			//Declare Variables
			String storeID = "";
		    String oldDepartmentName = "";
		    String newDepartmentName = "";
		    String newDepartmentDescription = "";
		    
		    //Get the department parameters
		    try {
		    	storeID = request.getParameter("storeID");
		    	oldDepartmentName = request.getParameter("oldName");
		    	newDepartmentName = request.getParameter("newName");
		    	newDepartmentDescription = request.getParameter("newDescription");
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
		    
		    //Ensure the Department Exists and the new department name doesn't exist yet
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        Store store = (Store) pm.getObjectById(Store.class, Integer.parseInt(storeID));	        

	        if ((store.getDepartments().contains(oldDepartmentName)) && (!store.getDepartments().contains(newDepartmentName))) {	
	        	//Rename the department in the Store list
	        	store.modifyDepartment(oldDepartmentName, newDepartmentName);
	        	
	        	//Update the actual department
	        	Department department = (Department) pm.getObjectById(Department.class, oldDepartmentName);	 
	        	Department newDepartment = new Department(newDepartmentName, newDepartmentDescription, department.getItems());
	        	pm.deletePersistent(department);
	        	pm.makePersistent(newDepartment);

	        	//Update all items contained within the department
	        	for (String itemName : newDepartment.getItems()) {
		        	Item item = (Item) pm.getObjectById(Item.class, itemName);	 
	        		item.department = newDepartmentName;
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
				
			//The department doesn't exist or the new one already exists, so don't modify
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
		} else if ("removeDepartment".equals(request.getParameter("op"))) {
			//Declare Variables
			String storeID = "";
		    String departmentName = "";
		    
		    //Get the department parameters
		    try {
		    	storeID = request.getParameter("storeID");
		    	departmentName = request.getParameter("name");
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
		    
		    //Check if the department exists
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        Store store = (Store) pm.getObjectById(Store.class, Integer.parseInt(storeID));
		    
	        if (store.getDepartments().contains(departmentName)) {
	        	Department department = (Department) pm.getObjectById(Department.class, departmentName);	 
	        	
	        	//Remove all items under this department name
	        	for (String itemName : department.getItems()) {
		        	Item item = (Item) pm.getObjectById(Item.class, itemName);	 
	        		pm.deletePersistent(item);
	        	}
	        	
	        	//Remove the actual department
	        	pm.deletePersistent(department);
	        	
	        	//Remove reference in Store
	        	store.removeDepartment(departmentName);
	        	
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
				
		        
			//The department doesn't exist, so don't remove it
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
	 * Gets the user the list of the departments in the store
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Only send the data if the proper command is given
		if ("getDepartments".equals(request.getParameter("op"))) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
		
			//Serialize a list of the store's departments and send it to the client
			ObjectOutputStream responseObject = new ObjectOutputStream(response.getOutputStream());
			responseObject.writeObject(pm.getObjectById(Store.class, Integer.parseInt(request.getParameter("storeID"))).getDepartments());
			responseObject.flush();
			responseObject.close();
		}
	}
}