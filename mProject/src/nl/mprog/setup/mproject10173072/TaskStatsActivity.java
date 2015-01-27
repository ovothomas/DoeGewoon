package nl.mprog.setup.mproject10173072;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class TaskStatsActivity extends Activity {
	private List<Date> mListDates;
	private StatsListAdapter mAdapter;
	private ListView mListView;
	private TaskDataBase mDatabase;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_stats);
		
		this.mListView = (ListView)findViewById(R.id.list_stats);
		
		mDatabase = new TaskDataBase(this);
		mListDates = mDatabase.getCountCompleted();
		mAdapter = new StatsListAdapter(this, mListDates);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	
}
