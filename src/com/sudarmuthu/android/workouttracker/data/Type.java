/**
 * 
 */
package com.sudarmuthu.android.workouttracker.data;

import java.util.Date;

/**
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public class Type {
	private int id;
	private String name;
	private Date createdOn;
	
	/**
	 * Constructor with three parameters
	 * 
	 * @param id
	 * @param name
	 * @param createdOn
	 */
	public Type(int id, String name, Date createdOn) {
		this.id = id;
		this.name = name;
		this.createdOn = createdOn;
	}
	
	/**
	 * Constructor with two parameters
	 * 
	 * @param name
	 * @param createdOn
	 */
	public Type (String name, Date createdOn) {
		this(0, name, createdOn);
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
}