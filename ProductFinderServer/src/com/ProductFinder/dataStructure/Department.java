package com.ProductFinder.dataStructure;

import java.util.LinkedList;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;


/**
 * Contains the name and description of a department along with 
 * all the items that exist in that department
 * @author adam knox
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Department {
    @PrimaryKey
	private String name;
	private String description;
	private LinkedList<String> items;
	
	
	
	/**
	 * Creates an empty department
	 * @param name the department's name
	 * @param description the department's description
	 */
	public Department (String name, String description) {
		this.name = name;
		this.setDescription(description);
		items = new LinkedList<String>();
	}
	
	/**
	 * Creates a department that already contains items
	 * @param name the department name
	 * @param description the department's description
	 * @param items a linked list of all the items under this department
	 */
	public Department (String name, String description, LinkedList<String> items) {
		this.name = name;
		this.setDescription(description);
		this.items = items;
	}
	
	
	
	/**
	 * 
	 * @return a list of the items under this department
	 */
	public LinkedList<String> getItems() {
		return items;
	}
	
	/**
	 * adds the given item to the department
	 * @param name the name of the item to add to the department
	 */
	public void addItem(String name) {
		items.add(name);
	}
	
	/**
	 * renames an item in the department
	 * @param oldName the item's current name
	 * @param newName the new name you want to give the item
	 */
	public void modifyItem(String oldName, String newName) {
		items.remove(oldName);
		items.add(newName);
	}

	/**
	 * removes the given item from the department
	 * @param name the name of the item to remove from the department
	 */
	public void removeItem(String name) {
		items.remove(name);
	}



	/**
	 * 
	 * @return the department name
	 */
	public String getName() {
		return name;
	}



	/**
	 * 
	 * @return the description of the department
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the department's description
	 * @param description the department's new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
