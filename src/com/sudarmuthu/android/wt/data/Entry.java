/**
 * 
 */
package com.sudarmuthu.android.wt.data;

import java.util.Date;

/**
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public class Entry {
	private int id;
	private int typeId;
	private Date date;
	private int daySeq;
	private String value;
	
	/**
	 * Constructor with id
	 * 
	 * @param id
	 * @param typeId
	 * @param date
	 * @param daySeq
	 * @param value
	 */
	public Entry (int id, int typeId, Date date, int daySeq, String value) {
		this.id = id;
		this.typeId = typeId;
		this.date = date;
		this.daySeq = daySeq;
		this.value = value;
	}

	/**
	 * Constructor without id
	 * 
	 * @param typeId
	 * @param date
	 * @param daySeq
	 * @param value
	 */
	public Entry (int typeId, Date date, int daySeq, String value) {
		this(0, typeId, date, daySeq, value);
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
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the daySeq
	 */
	public int getDaySeq() {
		return daySeq;
	}

	/**
	 * @param daySeq the daySeq to set
	 */
	public void setDaySeq(int daySeq) {
		this.daySeq = daySeq;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}