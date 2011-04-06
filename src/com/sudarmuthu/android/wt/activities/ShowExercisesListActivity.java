package com.sudarmuthu.android.wt.activities;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.sudarmuthu.android.wt.R;
import com.sudarmuthu.android.wt.app.WorkoutTrackerApp;
import com.sudarmuthu.android.wt.app.WorkoutTrackerApp.DialogStatus;
import com.sudarmuthu.android.wt.data.DBUtil;
import com.sudarmuthu.android.wt.data.Exercise;

public class ShowExercisesListActivity extends ListActivity {
	
	private static final String TAG = "Exercises";

	private List<Exercise> mExercises;
	private LayoutInflater mInflater;
	private ArrayAdapter<Exercise> mArrayAdapter;
	private View mAddExerciseDialogLayout;
	private Context mContext;
	private EditText exerciseName;
	
	private static final int DIALOG_ADD_EXERCISE = 0;

	 // package scope, since it is accessed in inner classes
	WorkoutTrackerApp mApp;
	
	/** 
	 * Called when the activity is first created. 
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.type_list);

		mApp = (WorkoutTrackerApp) getApplication();
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mExercises = DBUtil.fetchAllExercises(this);

		mArrayAdapter = new ArrayAdapter<Exercise>(this, R.layout.type_list_item, mExercises) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row;

				if (null == convertView) {
					row = mInflater.inflate(R.layout.type_list_item, null);
				} else {
					row = convertView;
				}

				Exercise type = (Exercise) mExercises.get(position);
				TextView tv = (TextView) row.findViewById(android.R.id.text1);
				tv.setText(type.getName());

				return row;
			}

		};
		setListAdapter(mArrayAdapter);
		
        //dialog box layout
		mContext = this;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		mAddExerciseDialogLayout = inflater.inflate(R.layout.add_type_dialog, (ViewGroup) findViewById(R.id.type_layout_root));

		if (mExercises.size() == 0) {
			// if there are no exercises initially, then show the add type dialog
			showDialog(DIALOG_ADD_EXERCISE);			
		}
		
		exerciseName = (EditText) mAddExerciseDialogLayout.findViewById(R.id.type_name);
		//register for context menu
		registerForContextMenu(getListView());		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Exercise type = mExercises.get(position);
		Log.v(TAG, "Clicked " + type.getName() + "Id: " + type.getId());

		Intent intent = new Intent(this.getApplicationContext(),
				TabWidget.class);
		intent.putExtra("typeId", type.getId());
		startActivity(intent);
	}

	/**
	 * When the context menu is created
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.exercise_context_menu, menu);
	}

	
	/**
	 * When a context menu item is selected
	 *  
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch (item.getItemId()) {
		
		case R.id.edit_exercise:
			//Edit Exercise name
			mApp.setCurrentExerciseDialogStatus(DialogStatus.EDIT);			
			editExercise((int) info.id);
			return true;
		case R.id.delete_exercise:
			//Delete Exercise and all its entries
			deleteExercise((int) info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.types_menu, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_exercise:
			mApp.setCurrentExerciseDialogStatus(DialogStatus.ADD);
			showDialog(DIALOG_ADD_EXERCISE);
			break;

		case R.id.About:
			//TODO Add about dialog here
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DIALOG_ADD_EXERCISE:
			AlertDialog.Builder builder;
			
			//build the dialog
			builder = new AlertDialog.Builder(mContext);
			builder.setView(mAddExerciseDialogLayout);
			builder.setMessage(this.getString(R.string.add_exercise_title))
		       .setCancelable(false)
		       .setPositiveButton(this.getString(R.string.add, this.getString(R.string.exercise)), null)
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
			
			dialog = builder.create();

			break;

		default:
			dialog = null;
			break;
		}
		return dialog;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		AlertDialog alertDialog = (AlertDialog) dialog;
		Button positiveButton = null;
		
		switch (id) {
		case DIALOG_ADD_EXERCISE:

			switch (mApp.getCurrentExerciseDialogStatus()) {
			case DEFAULT:
			case ADD:

				alertDialog.setMessage(this.getString(R.string.add_exercise_title));
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(R.string.add, this.getString(R.string.exercise)), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //Insert the new data into db
			        	   EditText typeValue = (EditText) mAddExerciseDialogLayout.findViewById(R.id.type_name);

			        	   String typeName = typeValue.getText().toString();
			        	   Exercise newExercise = DBUtil.insertExercise(mContext, typeName);
			        	   
			        	   Toast.makeText(mContext, mContext.getResources().getString(R.string.exercise_saved), Toast.LENGTH_SHORT).show();
			        	   
			        	   mArrayAdapter.add(newExercise);
			        	   mArrayAdapter.notifyDataSetChanged();
			           }
			       });
				
				positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
				positiveButton.setText(this.getString(R.string.add, this.getString(R.string.exercise)));
				positiveButton.invalidate();
				
				exerciseName.setText("");
				
				break;
				
			case EDIT:
				alertDialog.setMessage(this.getString(R.string.edit, this.getString(R.string.exercise)));
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(R.string.edit_button), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //Update the data into db
							Exercise exerciseToEdit = (Exercise) exerciseName.getTag(); 
							exerciseToEdit.setName(exerciseName.getText().toString());
							
							DBUtil.updateExercise(mContext, exerciseToEdit);
							Toast.makeText(mContext, mContext.getResources().getString(R.string.exercise_saved), Toast.LENGTH_SHORT).show();		
							mArrayAdapter.notifyDataSetChanged();
			           }

			       });
				
				Exercise exerciseToEdit = (Exercise) exerciseName.getTag();
				exerciseName.setText(exerciseToEdit.getName());
				
				positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
				positiveButton.setText(this.getString(R.string.edit_button));
				positiveButton.invalidate();
				
				break;
				}
		default:
			break;
		}
	}
	
	/**
	 * Edit Exercise name
	 * 
	 * @param id
	 */
	private void editExercise(int position) {
		exerciseName.setTag(mExercises.get(position));
		showDialog(DIALOG_ADD_EXERCISE);
	}
	
	/**
	 * Delete an Exercise
	 * 
	 * @param position
	 */
	private void deleteExercise(int position) {
		Exercise exercise = mExercises.get(position);
		DBUtil.deleteExercise(mContext, exercise.getId());
		
 	    Toast.makeText(mContext, mContext.getResources().getString(R.string.exercise_deleted), Toast.LENGTH_SHORT).show();		
		
		mArrayAdapter.remove(exercise);
		mArrayAdapter.notifyDataSetChanged();
		
	}
}