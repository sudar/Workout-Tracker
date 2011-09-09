/**
 * Activity to display stats 
 */
package com.sudarmuthu.android.wt.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sudarmuthu.android.wt.R;
import com.sudarmuthu.android.wt.data.DBUtil;
import com.sudarmuthu.android.wt.data.Entry;

/**
 * Activity class to handle stats
 * 
 * @author "Sudar Muthu (http://sudarmuthu.com)"
 *
 */
public class EntriesStatsActivity extends Activity {

	// for debugging
	private static boolean D = true;
	private static String TAG = "WT - EntriesStatsActivity";
	
	private List<Entry> mEntries;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_stats);
		
        Bundle bundle = getIntent().getExtras();
        int typeId = bundle.getInt("typeId");
        
        if (D) Log.d(TAG, "Got type id: " + typeId);

		mEntries = DBUtil.fetchEntries(this, typeId, null);
		Entry firstEntry = mEntries.get(0);
		
		int count = mEntries.size();
		float sum = 0;
		float average = 0;

		float min = Float.parseFloat(firstEntry.getValue());
		float max = Float.parseFloat(firstEntry.getValue());

		// calculate
		for (Entry entry : mEntries) {
			float value = Float.parseFloat(entry.getValue());
			sum += value;
			
			if (value < min) {
				min = value;
			}
			
			if (value > max) {
				max = value;
			}
		}
		
		average = sum / count;
		
		// populate the values
		TextView statsCount = (TextView) findViewById(R.id.statsCount);
		statsCount.setText("" + count);

		TextView statsSum = (TextView) findViewById(R.id.statsSum);
		statsSum.setText("" + sum);
		
		TextView statsAverage = (TextView) findViewById(R.id.statsAverage);
		statsAverage.setText("" + average);
		
		TextView valueFrom = (TextView) findViewById(R.id.valueFrom);
		valueFrom.setText("" + min);
		
		TextView valueTo = (TextView) findViewById(R.id.valueTo);
		valueTo.setText("" + max);
		
	}
}