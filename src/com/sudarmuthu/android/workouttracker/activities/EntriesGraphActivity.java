/**
 * Graph Activity
 */
package com.sudarmuthu.android.workouttracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.sudarmuthu.android.workouttracker.R;
import com.sudarmuthu.android.workouttracker.app.WorkoutTrackerApp;
import com.sudarmuthu.android.workouttracker.app.WorkoutTrackerApp.GroupBy;
import com.sudarmuthu.android.workouttracker.data.DBUtil;
import com.sudarmuthu.android.workouttracker.graph.EntryGraphHandler;

/**
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public class EntriesGraphActivity extends Activity {
	private static final String TAG = "GraphActivity";

	private WorkoutTrackerApp mApp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_graph);        
        WebView wv = (WebView) findViewById(R.id.wv1);
        
        Bundle bundle = getIntent().getExtras();
        Log.d(TAG, "Got type id: " + bundle.getInt("typeId"));
    
        mApp = (WorkoutTrackerApp) getApplication();
        EntryGraphHandler entryGraphHandler = new EntryGraphHandler (wv, DBUtil.fetchType(this, bundle.getInt("typeId")), this, mApp);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(entryGraphHandler, "testhandler");
        wv.loadUrl("file:///android_asset/flot/entry_graph.html");
        
        Log.d(TAG, "Web view initialized");
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
}