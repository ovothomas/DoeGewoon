package nl.mprog.setup.mproject10173072;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListActivity extends ListActivity {
	private ArrayList<Task> mTasks;
	private Button mTaskButton;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		//configuring the actionbar
		getActionBar().setTitle(R.string.task_title);
		
		// retrieving the tasks in Taskstorage to display
		// and setting custom made adapter to populate the listview
		mTasks = TaskStorage.get(getBaseContext()).getTasks();
		TaskAdapter adapter = new TaskAdapter(this, mTasks);
		setListAdapter(adapter);
		
		mTaskButton = (Button)findViewById(R.id.button_add_task);
		mTaskButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createTask();
			}
		});
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
	
		Task task = ((TaskAdapter)getListAdapter()).getItem(position);
		
		//Start TaskpagerActivity and putExtra Id of the particular fragment
		Intent i = new Intent(this, TaskPagerActivity.class);
		// tell TaskFragment which task to display by making the TaskId an Intent extra
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
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_task_row, parent, false);
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
	 * Notify the listadapter when particular TaskFragment is
	 * configured
	 */
	@Override
	public void onResume(){
		super.onResume();
		((TaskAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add_new_task) {
			createTask();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// create Task function
	public void createTask(){
		Task task = new Task();
		TaskStorage.get(getBaseContext()).addTask(task);
		Intent i = new Intent(getBaseContext(), TaskPagerActivity.class);
		i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
		startActivityForResult(i, 0);
	}
}
