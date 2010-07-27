/**
 * 
 */
package com.sudarmuthu.android.workouttracker.app;

import android.app.Application;

/**
 * Application object for the app 
 * 
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public class WorkoutTrackerApp extends Application {

	private DialogStatus currrentDialogStatus = DialogStatus.DEFAULT;
	
	public enum DialogStatus {
		DEFAULT, ADD, EDIT;
	}
	
	/**
	 * Default Contstructor
	 */
	public WorkoutTrackerApp() {
		super();
	}

	/**
	 * @param currrentDialogStatus the currrentDialogStatus to set
	 */
	public void setCurrrentDialogStatus(DialogStatus currrentDialogStatus) {
		this.currrentDialogStatus = currrentDialogStatus;
	}

	/**
	 * @return the currrentDialogStatus
	 */
	public DialogStatus getCurrrentDialogStatus() {
		return currrentDialogStatus;
	}

}
