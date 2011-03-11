/**
 * 
 */
package com.sudarmuthu.android.wt.data;

import android.provider.BaseColumns;

/**
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public interface DBConstants extends BaseColumns {
	//Types Table
	public static final String TYPES_TABLE_NAME = "types";
	
	// Columns in the Types table 
	public static final String TYPES_NAME = "name"; 
	public static final String TYPES_CREATED_ON = "created_on"; 

	
	//Entry table	
	public static final String ENTRY_TABLE_NAME = "entry";
	
	//columns in the entry table
	public static final String ENTRY_TYPE_ID = "type_id";	
	public static final String ENTRY_DATE = "date";
	public static final String ENTRY_DAY_SEQ = "day_seq";	
	public static final String ENTRY_VALUE = "value";	
}