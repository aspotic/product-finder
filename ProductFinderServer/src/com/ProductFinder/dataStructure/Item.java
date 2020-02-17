package com.ProductFinder.dataStructure;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * Contains all the information about any one item in the system
 * @author adam knox
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Item implements Serializable {	
	private static final long serialVersionUID = 1L;
    @PrimaryKey
	public String name;
	public long store;
	public String department;
	public String brand;
	public long serial;
	public String description;
	public double xPosition;
	public double yPosition;
	public long isle;
	public long shelf;
	public double price;

	
	/**
	 * Creates an item
	 * @param name the name of the item
	 * @param serial the serial(bar code) of the item
	 * @param description a description of the item
	 * @param location the item's location in the store
	 * @param price the price of the item
	 */
	public Item (String name, String department, String brand, long serial, String description, double price, long isle, long shelf, double xPosition, double yPosition) {
		this.name = name;
		this.department = department;
		this.brand = brand;
		this.serial = serial;
		this.description = description;
		this.price = price;
		this.isle = isle;
		this.shelf = shelf;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	/**
	 * Creates an item
	 * @param item an item to create a jdo instance of
	 */
	public Item (ClientItem item) {
		this.name = item.name;
		this.department = item.department;
		this.store = item.store;
		this.brand = item.brand;
		this.serial = item.serial;
		this.description = item.description;
		this.price = item.price;
		this.isle = item.isle;
		this.shelf = item.shelf;
		this.xPosition = item.xPosition;
		this.yPosition = item.yPosition;
	}
}
