package nl.mprog.setup.mproject10173072;
/*
 * Activity to show a list
 * of tasks in the List array
 */
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TaskUncompletedListActivity extends Activity implements android.widget.AdapterView.OnItemLongClickListener, android.widget.AdapterView.OnItemClickListener{
	public final String TAG = "TaskListActivity";
	private List<Task> mListTasks;
	private TaskDataBase mDatabase;
	private TaskUncompletedListAdapter mAdapter;
	private ListView mListView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		//configuring the actionbar
		getActionBar().setTitle(R.string.task_title);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 164, 9, 9)));
		
		//Initialize view
		initViews();
		
		// open database and set the listadapter to display tasks in it
		mDatabase= new TaskDataBase(this);
		mListTasks = mDatabase.getUncompletedTask();
		mAdapter = new TaskUncompletedListAdapter(this, mListTasks, mDatabase);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	
	// method to initialize the views
	private void initViews(){
		this.mListView = (ListView)findViewById(R.id.list_task);
		this.mListView.setOnItemLongClickListener(this);
		this.mListView.setOnItemClickListener(this);
	}
	 
	@Override
	public void onResume(){
		super.onResume();
		mListTasks = mDatabase.getUncompletedTask();
		mAdapter.setItems(mListTasks);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// enabling the actionbar to add tasks to the lists
		int id = item.getItemId();
		if (id == R.id.action_add_new_task) {
			createTask();
			return true;
		} else if (id == R.id.see_stats){
			Intent intent = new Intent(this, TaskStatsActivity.class);
			startActivity(intent);
			return true;
		} else if (id == R.id.see_completed){
			Intent intent = new Intent(this, TaskCompletedListActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	// create Task function
	public void createTask(){
		Intent i = new Intent(this, TaskActivity.class);
		startActivity(i);
	}
	
	// method to delete task from the list using a dailog
	private void showDeleteDialogConfirmation(final Task task){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("DELETE");
		alertDialogBuilder.setMessage("Sure you want to delete?  \"" + task.getTaskTitle());
		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// delete task
				if (mDatabase != null){
					mDatabase.deleteTask(task);
					mListTasks.remove(task);
					mAdapter.setItems(mListTasks);
					mAdapter.notifyDataSetChanged();
				}
				dialog.dismiss();
				Toast.makeText(TaskUncompletedListActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
			}
		});
		
		// set neutral button OK
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Dismiss the dialog
                dialog.dismiss();
			}
		});
        
        // create and show dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}

	// show the dialog for the user to delete the task
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
		int position, long id) {
		Task task = mAdapter.getItem(position);
		Log.d(TAG, "longClickdItem : " + task.getTaskTitle());
		showDeleteDialogConfirmation(task);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Task task = mAdapter.getItem(position);
		Log.d(TAG, "clickedItem : " + task.getTaskTitle());
		
		//Start TaskpagerActivity and putExtra Id of the particular fragment
		Intent i = new Intent(this, TaskPagerActivity.class);
		
		// tell TaskFragment which task to display by making the TaskId an Intent extra
		i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
		startActivity(i);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mDatabase.close();
	}
}
