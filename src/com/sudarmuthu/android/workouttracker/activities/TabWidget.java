/**
 * 
 */
package com.sudarmuthu.android.workouttracker.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.sudarmuthu.android.workouttracker.R;

/**
 * @author "Sudar Muthu (sudarm@)"
 *
 */
public class TabWidget extends TabActivity {

	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
        Bundle bundle = getIntent().getExtras();
        int typeId = bundle.getInt("typeId");
	    intent = new Intent().setClass(this, EntriesListActivity.class).putExtra("typeId", typeId);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    // Entry list tab
	    spec = tabHost.newTabSpec("lists").setIndicator("Lists",
				res.getDrawable(R.drawable.ic_tab_list)).setContent(intent);
	    tabHost.addTab(spec);

	    // Group by Date tab
	    intent = new Intent().setClass(this, EntriesDateActivity.class).putExtra("typeId", typeId);
		spec = tabHost.newTabSpec("date").setIndicator("By Date",
				res.getDrawable(R.drawable.ic_tab_date)).setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, EntriesGraphActivity.class).putExtra("typeId", typeId);
	    spec = tabHost.newTabSpec("graph").setIndicator("Graph",
	    		res.getDrawable(R.drawable.ic_tab_list))
	    		.setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, EntriesStatsActivity.class).putExtra("typeId", typeId);
	    spec = tabHost.newTabSpec("stats").setIndicator("Stats",
	                      res.getDrawable(R.drawable.ic_tab_list))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
		
	}

}
