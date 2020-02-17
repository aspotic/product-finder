package com.ProductFinder.dataStructure;

import java.util.LinkedList;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;


/**
 * Contains the name and description of a brand along with all the items that exist in that brand
 * if a brand is renamed, all items in the brand have their brand field updated
 * if a brand is removed, all items in the brand are removed as well
 * @author adam knox
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Brand {
    @PrimaryKey
	private String name;
	private String description;
	private LinkedList<String> items;
	
	
    
	/**
	 * Creates a new brand
	 * @param name the brand's name
	 * @param description the brand's description
	 */
	public Brand (String name, String description) {
		this.name = name;
		this.setDescription(description);
		items = new LinkedList<String>();
	}
	
	/**
	 * Creates a brand that has items in it already
	 * @param name the brand's name
	 * @param description the brand's description
	 * @param items a linked list of all the items under this brand
	 */
	public Brand (String name, String description, LinkedList<String> items) {
		this.name = name;
		this.setDescription(description);
		this.items = items;
	}
	
	
	
	/**
	 * 
	 * @return a list of all the items under this brand
	 */
	public LinkedList<String> getItems() {
		return items;
	}
	
	/**
	 * adds the given item to the brand
	 * @param name the name to assign to the brand
	 */
	public void addItem(String name) {
		items.add(name);
	}

	/**
	 * renames an item
	 * @param oldName the item to rename
	 * @param newName the item's new name
	 */
	public void modifyItem(String oldName, String newName) {
		items.remove(oldName);
		items.add(newName);
	}

	/**
	 * removes the given item
	 * @param name the name of the item to remove from under the brand
	 */
	public void removeItem(String name) {
		items.remove(name);
	}



	/**
	 * 
	 * @return the name of the brand
	 */
	public String getName() {
		return name;
	}

	
	
	/**
	 * 
	 * @return the description of the brand
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * sets the brand's description
	 * @param description the new brand description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
