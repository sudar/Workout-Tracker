/**
 * Graph Activity
 */
package com.sudarmuthu.android.wt.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.sudarmuthu.android.wt.R;
import com.sudarmuthu.android.wt.app.WorkoutTrackerApp;
import com.sudarmuthu.android.wt.app.WorkoutTrackerApp.GroupBy;
import com.sudarmuthu.android.wt.data.DBUtil;
import com.sudarmuthu.android.wt.graph.EntryGraphHandler;

/**
 * @author "Sudar Muthu (http://sudarmuthu.com)"
 *
 */
public class EntriesGraphActivity extends Activity {
	private static final String TAG = "GraphActivity";

	private WorkoutTrackerApp mApp;
	private EntryGraphHandler mEntryGraphHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_graph);        
        WebView wv = (WebView) findViewById(R.id.wv1);
        
        Bundle bundle = getIntent().getExtras();
        Log.d(TAG, "Got type id: " + bundle.getInt("typeId"));
    
        mApp = (WorkoutTrackerApp) getApplication();
        mEntryGraphHandler = new EntryGraphHandler (wv, DBUtil.fetchType(this, bundle.getInt("typeId")), this, mApp);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(mEntryGraphHandler, "testhandler");
        wv.loadUrl("file:///android_asset/flot/entry_graph.html");
        
        Log.d(TAG, "Web view initialized");
	}
	
	/**
	 * When the activity is resumed
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mEntryGraphHandler.loadGraph();
	}

	/**
	 * When the back button is pressed
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		mApp.setCurrentGroupBy(GroupBy.NONE);
		super.onBackPressed();
	}	
	
	/**
	 * Create options menu
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.entries_graph_menu, menu);
		return true;
	}

	/**
	 * When a Option menu item is selected
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.group_entry_by_date:

			mApp.setCurrentGroupBy(GroupBy.DATE);
			break;

		case R.id.group_entry_by_max:

			mApp.setCurrentGroupBy(GroupBy.MAX);
			break;

		case R.id.group_entry_by_none:

			mApp.setCurrentGroupBy(GroupBy.NONE);
			break;
		default:
			break;
		}
		
		mEntryGraphHandler.loadGraph();
		return super.onOptionsItemSelected(item);
	}
	
}