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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sudarmuthu.android.wt.R;
import com.sudarmuthu.android.wt.data.DBUtil;
import com.sudarmuthu.android.wt.data.Type;

public class ShowTypesListActivity extends ListActivity {

	private List<Type> types;
	private LayoutInflater mInflater;
	private ArrayAdapter<Type> mArrayAdapter;
	private View mAddtypeDialogLayout;
	private Context mContext;
	
	private static final String TAG = "Types";

	private static final int DIALOG_ADD_TYPE = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.type_list);

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//		 DBUtil.insertType(this, "Push Up");

		types = DBUtil.fetchAllTypes(this);

		mArrayAdapter = new ArrayAdapter<Type>(this, R.layout.type_list_item, types) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row;

				if (null == convertView) {
					row = mInflater.inflate(R.layout.type_list_item, null);
				} else {
					row = convertView;
				}

				Type type = (Type) types.get(position);
				TextView tv = (TextView) row.findViewById(android.R.id.text1);
				tv.setText(type.getName());

				return row;
			}

		};
		setListAdapter(mArrayAdapter);
		
        //dialog box layout
		mContext = this;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		mAddtypeDialogLayout = inflater.inflate(R.layout.add_type_dialog, (ViewGroup) findViewById(R.id.type_layout_root));

		if (types.size() == 0) {
			// if there are no types initially, then show the add type dialog
			showDialog(DIALOG_ADD_TYPE);			
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Type type = types.get(position);
		Log.v(TAG, "Clicked " + type.getName() + "Id: " + type.getId());

		Intent intent = new Intent(this.getApplicationContext(),
				TabWidget.class);
		intent.putExtra("typeId", type.getId());
		startActivity(intent);
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
			showDialog(DIALOG_ADD_TYPE);
			break;

		case R.id.About:
			
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
		case DIALOG_ADD_TYPE:
			AlertDialog.Builder builder;
			
			//build the dialog
			builder = new AlertDialog.Builder(mContext);
			builder.setView(mAddtypeDialogLayout);
			builder.setMessage(this.getString(R.string.add_type_title))
		       .setCancelable(false)
		       .setPositiveButton(this.getString(R.string.add_type), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //Insert the new data into db
		        	   EditText typeValue = (EditText) mAddtypeDialogLayout.findViewById(R.id.type_name);

		        	   String typeName = typeValue.getText().toString();
		        	   Type newType = DBUtil.insertType(mContext, typeName);
		        	   
		        	   Toast.makeText(mContext, mContext.getResources().getString(R.string.type_saved), Toast.LENGTH_SHORT).show();
		        	   
		        	   mArrayAdapter.add(newType);
		        	   mArrayAdapter.notifyDataSetChanged();
		           }

		       })
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
		switch (id) {
		case DIALOG_ADD_TYPE:
			EditText typeValue = (EditText) mAddtypeDialogLayout
					.findViewById(R.id.type_name);
			typeValue.setText("");
			break;

		default:
			break;
		}
	}
}