package com.ProductFinder.dataStructure;

import java.io.Serializable;

/**
 * Contains all the information about any one item in the system.
 * This is the item used by the client. it may be serialized and
 * passed to the client.  The server version has other special variables
 * @author adam knox
 *
 */
public class ClientItem implements Serializable {	
	private static final long serialVersionUID = 1L;
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
	public ClientItem (String name, String department, String brand, long serial, String description, double price, long isle, long shelf, double xPosition, double yPosition) {
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
}
