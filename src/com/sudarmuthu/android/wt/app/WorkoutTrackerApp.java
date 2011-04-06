/**
 * 
 */
package com.sudarmuthu.android.wt.app;

import android.app.Application;

/**
 * Application object for the app 
 * 
 * @author "Sudar Muthu (http://sudarmuthu.com)"
 *
 */
public class WorkoutTrackerApp extends Application {

	//Entry status
	private DialogStatus currrentDialogStatus = DialogStatus.DEFAULT;
	private GroupBy currentGroupBy = GroupBy.NONE; 
	private SortBy currentSortBy = SortBy.DATE_ASC;
	
	//Exercise status
	private DialogStatus currentExerciseDialogStatus = DialogStatus.DEFAULT;
	
	/**
	 * Current dialog status
	 * 
	 * @author "Sudar Muthu (http://sudarmuthu.com)"
	 *
	 */
	public enum DialogStatus {
		DEFAULT, ADD, EDIT;
	}

	/**
	 * Current group by status
	 * 
	 * @author "Sudar Muthu (http://sudarmuthu.com)"
	 *
	 */
	public enum GroupBy {
		NONE, DATE, MAX;
	}
	
	/**
	 * Current Sort by status
	 * 
	 * @author "Sudar Muthu (http://sudarmuthu.com)"
	 *
	 */
	public enum SortBy {
		DATE_ASC, DATE_DESC, VALUE_ASC, VALUE_DESC;
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

	/**
	 * @param currentGroupBy the currentGroupBy to set
	 */
	public void setCurrentGroupBy(GroupBy currentGroupBy) {
		this.currentGroupBy = currentGroupBy;
	}

	/**
	 * @return the currentGroupBy
	 */
	public GroupBy getCurrentGroupBy() {
		return currentGroupBy;
	}

	/**
	 * @return the currentSortBy
	 */
	public SortBy getCurrentSortBy() {
		return currentSortBy;
	}

	/**
	 * @param currentSortBy the currentSortBy to set
	 */
	public void setCurrentSortBy(SortBy currentSortBy) {
		this.currentSortBy = currentSortBy;
	}

	/**
	 * @param currentExerciseDialogStatus the currentExerciseDialogStatus to set
	 */
	public void setCurrentExerciseDialogStatus(DialogStatus currentExerciseDialogStatus) {
		this.currentExerciseDialogStatus = currentExerciseDialogStatus;
	}

	/**
	 * @return the currentExerciseDialogStatus
	 */
	public DialogStatus getCurrentExerciseDialogStatus() {
		return currentExerciseDialogStatus;
	}

}
