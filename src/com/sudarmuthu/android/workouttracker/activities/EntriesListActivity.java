/**
 * 
 */
package com.sudarmuthu.android.workouttracker.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.sudarmuthu.android.workouttracker.R;
import com.sudarmuthu.android.workouttracker.app.WorkoutTrackerApp;
import com.sudarmuthu.android.workouttracker.app.WorkoutTrackerApp.DialogStatus;
import com.sudarmuthu.android.workouttracker.app.WorkoutTrackerApp.GroupBy;
import com.sudarmuthu.android.workouttracker.data.DBUtil;
import com.sudarmuthu.android.workouttracker.data.Entry;
import com.sudarmuthu.android.workouttracker.data.Type;

/**
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public class EntriesListActivity extends ListActivity {
    
	private static final String TAG = "ShowEntries";
	
	private LayoutInflater mInflater;
    private List<Entry> mEntries;
    private Type mType;
	private int mYear;
	private int mMonth;
	private int mDay;
	private Button entryDate;
	private Button entryTime;
	private int mHour;
	private int mMinute;
	private int mSecond;
	private View mAddEntryDialogLayout;
	private Context mContext;
	private ArrayAdapter<Entry> mArrayAdapter;
	private WorkoutTrackerApp mApp;
	
	private static final int DIALOG_ADD_ENTRY = 0;
	private static final int DIALOG_DATE_PICKER = 2;
	private static final int DIALOG_TIME_PICKER = 3;

	/**
	 * View Holder class
	 * 
	 * @author "Sudar Muthu (sudarm@)"
	 *
	 */
	static class ViewHolder {
		TextView date;
		TextView daySeq;
		TextView value;
	}
	
	//the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = 
		new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDisplay();
				resetMaxDaySeq();				
			}
		};

	//the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
	    new TimePickerDialog.OnTimeSetListener() {
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            mHour = hourOfDay;
	            mMinute = minute;
	            updateDisplay();
	        }
	    };
	    	
	    
	/**
	 * Full Array Adapter
	 *     
	 * @author "Sudar Muthu (sudarm@)"
	 *
	 */
	private class GroupByNoneAdapter extends ArrayAdapter<Entry>{
		/**
		 * @param context
		 * @param textViewResourceId
		 * @param objects
		 */
		public GroupByNoneAdapter(Context context, int textViewResourceId, List<Entry> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.entry_list_item,
						parent, false);

				holder = new ViewHolder();
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.daySeq = (TextView) convertView
						.findViewById(R.id.day_seq);
				holder.value = (TextView) convertView.findViewById(R.id.value);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Entry entry = (Entry) mEntries.get(position);

			holder.date.setText(DBUtil.dateToString(entry.getDate(), "yyyy-MM-dd HH:mm:ss"));
			holder.daySeq.setText("" + entry.getDaySeq());
			holder.value.setText(entry.getValue());

			return convertView;
		}
	}

	// Group by Date Array Adpater
	private class GroupByDateAdapter extends ArrayAdapter<Entry> {
		/**
		 * @param context
		 * @param textViewResourceId
		 * @param objects
		 */
		public GroupByDateAdapter(Context context, int textViewResourceId, List<Entry> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.entry_list_item, parent, false);

				holder = new ViewHolder();
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.daySeq = (TextView) convertView.findViewById(R.id.day_seq);
				holder.value = (TextView) convertView.findViewById(R.id.value);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Entry entry = (Entry) mEntries.get(position);

			holder.date.setText(DBUtil.dateToString(entry.getDate(), "yyyy-MM-dd"));
			holder.daySeq.setText("-");
			holder.value.setText(entry.getValue());

			return convertView;
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_list);
		
		mApp = (WorkoutTrackerApp) getApplication();		
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = this;

        Bundle bundle = getIntent().getExtras();
        Log.v(TAG, "Got id: " + bundle.getInt("typeId"));
        mType = DBUtil.fetchType(mContext, bundle.getInt("typeId"));
        
        switch (mApp.getCurrentGroupBy()) {
		case NONE:
			mEntries = DBUtil.fetchEntries(mContext, bundle.getInt("typeId"));
			mArrayAdapter = new GroupByNoneAdapter(mContext, R.layout.entry_list_item, mEntries); 
			break;

		case DATE:
			mEntries = DBUtil.fetchEntriesByDate(mContext, bundle.getInt("typeId"));
			mArrayAdapter = new GroupByDateAdapter(mContext, R.layout.entry_list_item, mEntries); 
			break;
		}
        
        setListAdapter(mArrayAdapter);
        
        setDateAndtime();
        
        //dialog box layout
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		mAddEntryDialogLayout = inflater.inflate(R.layout.add_entry_dialog,
		                               (ViewGroup) findViewById(R.id.layout_root));
        
		inializeTimeControls();
		
		//register for context menu
		registerForContextMenu(getListView());		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.entry_context_menu, menu);
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch (item.getItemId()) {
		case R.id.edit_entry:
			editEntry((int) info.id);
			return true;
		case R.id.delete_entry:
			deleteEntry((int) info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 * Edit Entry when context menu is clicked
	 * 
	 * @param id
	 */
	private void editEntry(int position) {
		entryTime.setTag(mEntries.get(position));
		
		mApp.setCurrrentDialogStatus(DialogStatus.EDIT);
		
		showDialog(DIALOG_ADD_ENTRY);
	}

	/**
	 * Delete Entry when context menu is clicked
	 * 
	 * @param position
	 */
	private void deleteEntry(int position) {
		Entry entry = mEntries.get(position);
		DBUtil.deleteEntry(mContext, entry.getId());
		
 	    Toast.makeText(mContext, mContext.getResources().getString(R.string.entry_deleted), Toast.LENGTH_SHORT).show();		
		
		mArrayAdapter.remove(entry);
		mArrayAdapter.notifyDataSetChanged();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.entries_menu, menu);
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
		case R.id.add_entry:
			mApp.setCurrrentDialogStatus(DialogStatus.ADD);
			showDialog(DIALOG_ADD_ENTRY);
			break;

		case R.id.group_entry_by_date:
			
			if (mApp.getCurrentGroupBy() != GroupBy.DATE) {
				
				mEntries = DBUtil.fetchEntriesByDate(mContext, mType.getId());
				mArrayAdapter = new GroupByDateAdapter(this, R.layout.entry_list_item, mEntries);
				
				setListAdapter(mArrayAdapter);
				mArrayAdapter.notifyDataSetInvalidated();
				
				mApp.setCurrentGroupBy(GroupBy.DATE);
			}
			
			break;
			
		case R.id.group_entry_by_none:
			
			if (mApp.getCurrentGroupBy() != GroupBy.NONE) {
				
				mEntries = DBUtil.fetchEntries(mContext, mType.getId());
				mArrayAdapter = new GroupByNoneAdapter(this, R.layout.entry_list_item, mEntries);
				
				setListAdapter(mArrayAdapter);
				mArrayAdapter.notifyDataSetInvalidated();
				
				mApp.setCurrentGroupBy(GroupBy.NONE);
			}
			
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
		AlertDialog.Builder builder;
		
		switch (id) {
		
		case DIALOG_ADD_ENTRY:
			//build the dialog
			builder = new AlertDialog.Builder(mContext);
			builder.setView(mAddEntryDialogLayout);
			builder.setMessage("")
		       .setCancelable(false)
		       .setPositiveButton("Add", null)
		       .setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
			
			dialog = builder.create();
			
			break;
			
		case DIALOG_DATE_PICKER:
			// Date picker
			dialog = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
			break;
			
		case DIALOG_TIME_PICKER:
			// Time Picker
			dialog = new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
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
		
		EditText entryValue = (EditText) mAddEntryDialogLayout.findViewById(R.id.entry_value);
		
		switch (id) {
		case DIALOG_ADD_ENTRY:
			
			AlertDialog alertDialog = (AlertDialog) dialog;
			
			switch (mApp.getCurrrentDialogStatus()) {
			case ADD:
			
				alertDialog.setMessage(this.getString(R.string.add, mType.getName()));
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(R.string.add), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //Insert the new data into db
			        	   EditText entryValue = (EditText) mAddEntryDialogLayout.findViewById(R.id.entry_value);
			        	   int maxDaySeq = Integer.parseInt((String) entryDate.getTag());
			        	   Entry entry = new Entry(mType.getId(), getEntryDate(), maxDaySeq + 1, entryValue.getText().toString());
			        	   
			        	   DBUtil.insertEntry(mContext, entry);
			        	   Toast.makeText(mContext, mContext.getResources().getString(R.string.entry_saved), Toast.LENGTH_SHORT).show();
			        	   
			        	   mArrayAdapter.add(entry);
			        	   mArrayAdapter.notifyDataSetChanged();
			           }

			       });
				
				setDateAndtime();
				updateDisplay();
				resetMaxDaySeq();

				entryValue.setText("");

				break;
				
			case EDIT:
				
				alertDialog.setMessage(this.getString(R.string.edit, mType.getName()));
				
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(R.string.edit), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //Update the data into db
							EditText entryValue = (EditText) mAddEntryDialogLayout.findViewById(R.id.entry_value);
							
							Entry entryToEdit = (Entry) entryTime.getTag();
							entryToEdit.setDate(getEntryDate());
							entryToEdit.setValue(entryValue.getText().toString());
							
							DBUtil.updateEntry(mContext, entryToEdit);
							Toast.makeText(mContext, mContext.getResources().getString(R.string.entry_edited), Toast.LENGTH_SHORT).show();		
							mArrayAdapter.notifyDataSetChanged();
			           }

			       });
				
				Entry entryToEdit = (Entry) entryTime.getTag();
				entryDate.setTag(Integer.toString(entryToEdit.getDaySeq()));
				
				entryValue.setText(entryToEdit.getValue());
				
				setDateAndtime(entryToEdit.getDate());
				updateDisplay();
				break;
				
			case DEFAULT:
				
				break;
			}
			
			mApp.setCurrrentDialogStatus(DialogStatus.DEFAULT);			
			break;
			
		case DIALOG_DATE_PICKER:
			// Date picker
			DatePickerDialog datePicker = (DatePickerDialog) dialog;
			datePicker.updateDate(mYear, mMonth, mDay);
			
			break;
			
		case DIALOG_TIME_PICKER:
			// Time Picker
			TimePickerDialog timePicker = (TimePickerDialog) dialog;
			timePicker.updateTime(mHour, mMinute);
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Update the Date and Time buttons
	 */
	private void updateDisplay() {
		entryDate.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));

		entryTime.setText(new StringBuilder().append(pad(mHour)).append(":")
				.append(pad(mMinute)));
	}


	/**
	 * Reset max day seq 
	 */
	private void resetMaxDaySeq() {
		// set the max day seq
		int maxDaySeq = DBUtil.getMaxDaySeq(mContext, mYear + "-" + pad(mMonth + 1) + "-" + pad(mDay), mType.getId());
		entryDate.setTag(Integer.toString(maxDaySeq));
	}

	/**
	 * InializeTimeControls
	 */
	private void inializeTimeControls() {
		//set Date
		entryDate = (Button) mAddEntryDialogLayout.findViewById(R.id.entry_date);
		entryDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_DATE_PICKER);
			}
		});
		
		// set time
		entryTime = (Button) mAddEntryDialogLayout.findViewById(R.id.entry_time);
		entryTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_TIME_PICKER);
			}
		});
	}

	/**
	 * Pad the time display
	 * 
	 * @param c
	 * @return
	 */
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	/**
	 * Get the entry date from other values
	 * 
	 * @return
	 */
	private Date getEntryDate() {
		SimpleDateFormat iso8601Format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = iso8601Format.parse(mYear + "-" + (mMonth + 1) + "-" + mDay
					+ " " + mHour + ":" + mMinute + ":" + mSecond);
		} catch (ParseException e) {
			Log.e(TAG, "Parsing ISO8601 datetime failed", e);
		}
		return date;
	}

	/**
	 * Set the current date and time
	 */
	private void setDateAndtime() {
		// get the current date and time
        final Calendar c = Calendar.getInstance();
        setDateAndTimeHelper(c);
	}
	
	/**
	 * Set teh passed date
	 * @param date
	 */
	private void setDateAndtime(Date date) {
		// get the date and time
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        
		setDateAndTimeHelper(c);
	}

	/**
	 * Initalize all member variables
	 * 
	 * @param c
	 */
	private void setDateAndTimeHelper(final Calendar c) {
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);
	}
	
}