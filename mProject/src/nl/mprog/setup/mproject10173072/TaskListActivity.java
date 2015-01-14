package nl.mprog.setup.mproject10173072;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListActivity extends ListActivity {
	private ArrayList<Task> mTasks;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//configuring the actionbar
		getActionBar().setTitle(R.string.task_title);
		
		// retrieving the tasks in Taskstorage to display
		// and setting custom made adapter to populate the listview
		mTasks = TaskStorage.get(getBaseContext()).getTasks();
		TaskAdapter adapter = new TaskAdapter(this, mTasks);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
	
		Task task = ((TaskAdapter)getListAdapter()).getItem(position);
		
		//Start TaskpagerActivity and putExtra Id of the particular fragment
		Intent i = new Intent(this, TaskPagerActivity.class);
		i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
		startActivity(i);
		
	}
	
	// Creating a custom adapter to populate the listview
	private class TaskAdapter extends ArrayAdapter<Task>{
		
		public TaskAdapter(Context context, ArrayList<Task> tasks){
			
			super(context, 0, tasks);
		}
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		//Get the data item for this position
		Task task = getItem(position);
		//Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_task_list, parent, false);
		}
		
		//look for the view to populate with data
		TextView tvTitle = (TextView)convertView.findViewById(R.id.task_titleTextView);
		TextView tvDate = (TextView)convertView.findViewById(R.id.task_dateTextView);
		CheckBox tvCompleted = (CheckBox)convertView.findViewById(R.id.task_completed_checkbox);
		
		// Populate the data into the template view using the data object
		tvTitle.setText(task.getTaskTitle());
		tvDate.setText(DateFormat.format("EEEE, MMM dd, yyyy",task.getTaskDate()));
		tvCompleted.setChecked(task.isCompleted());
		
		
		return convertView;
		
		}
	}
	
	/*
	 * Notify the listadapter when particular crimeFragment is
	 * configured
	 */
	@Override
	public void onResume(){
		super.onResume();
		((TaskAdapter)getListAdapter()).notifyDataSetChanged();
	}
}
