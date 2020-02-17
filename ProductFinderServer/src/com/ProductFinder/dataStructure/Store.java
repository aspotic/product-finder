package com.ProductFinder.dataStructure;

import java.util.LinkedList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


/**
 * Contains a list of all the department and brands names contained within this store
 * @author adam knox
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Store {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	private LinkedList<String> departments;	//A list of departments within the store
	private LinkedList<String> brands;	//A list of the brands sold in the store
    
	
	
	/**
	 * Constructor. Creates the department list
	 */
	public Store () {
		departments = new LinkedList<String> ();
	}

	
	
	/**
	 * 
	 * @return the store's unique ID
	 */
	public Long getID() {
		return id;
	}
	

	
	/**
	 * 
	 * @return a list of all the departments in the store
	 */
	public LinkedList<String> getDepartments() {
		return departments;
	}
	
	/**
	 * adds the given department to the store
	 * @param name the name of the department to add
	 */
	public void addDepartment(String name) {
		departments.add(name);
	}

	/**
	 * renames one of the store's departments
	 * @param oldName the department's current name
	 * @param newName the name to give to the department
	 */
	public void modifyDepartment(String oldName, String newName) {
		departments.remove(oldName);
		departments.add(newName);
	}

	/**
	 * removes the given department from the store
	 * @param name the name of the department to remove
	 */
	public void removeDepartment(String name) {
		departments.remove(name);
	}
	


	/**
	 * 
	 * @return a list of all the brands in the store
	 */
	public LinkedList<String> getBrands() {
		return brands;
	}
	
	/**
	 * adds the given brand to the store
	 * @param name the name of the brand to add
	 */
	public void addBrand(String name) {
		brands.add(name);
	}

	/**
	 * renames a store brand
	 * @param oldName the brand's current name
	 * @param newName the name to give to the brand
	 */
	public void modifyBrand(String oldName, String newName) {
		brands.remove(oldName);
		brands.add(newName);
	}

	/**
	 * removes the given brand from the store
	 * @param name the name of the brand to remove
	 */
	public void removeBrand(String name) {
		brands.remove(name);
	}
}
