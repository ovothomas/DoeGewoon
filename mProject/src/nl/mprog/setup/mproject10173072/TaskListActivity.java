package nl.mprog.setup.mproject10173072;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemClickListener;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemLongClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TaskListActivity extends Activity implements OnItemLongClickListener, OnItemClickListener, android.widget.AdapterView.OnItemLongClickListener, android.widget.AdapterView.OnItemClickListener{
	public final String TAG = "TaskListActivity";
	private List<Task> mListTasks;
	private TaskDAO mDatabase;
	private TaskListAdapter mAdapter;
	private ListView mListView;
	public static final int REQUEST_CODE_ADD_TASK = 40;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		//configuring the actionbar
		getActionBar().setTitle(R.string.task_title);
		//Initialize view
		initViews();
		mDatabase= new TaskDAO(this);
		mListTasks = mDatabase.getAllTasks();
		mAdapter = new TaskListAdapter(this, mListTasks);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
}
	
	private void initViews(){
		this.mListView = (ListView)findViewById(R.id.list_task);
		this.mListView.setOnItemLongClickListener(this);
		this.mListView.setOnItemClickListener(this);
	}
	 
	@Override
	public void onResume(){
		super.onResume();
		mListTasks = mDatabase.getAllTasks();
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
		//Task task = new Task();
		//TaskStorage.get(getBaseContext()).addTask(task);
		Intent i = new Intent(this, TaskActivity.class);
		startActivity(i);
	}

	private void showDeleteDialogConfirmation(final Task task){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("DELETE");
		alertDialogBuilder.setMessage("Sure you want to delete?  \"" + task.getTaskTitle());
		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (mDatabase != null){
					mDatabase.deleteTask(task);
					mListTasks.remove(task);
					mAdapter.setItems(mListTasks);
					mAdapter.notifyDataSetChanged();
				}
				dialog.dismiss();
				Toast.makeText(TaskListActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
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
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
	}


	@Override
	public void onItemClick(AdapterViewCompat<?> parent,View view, int position,
			long id) {
		Task task = mAdapter.getItem(position);
		Log.d(TAG, "clickedItem : " + task.getTaskTitle());
		//Start TaskpagerActivity and putExtra Id of the particular fragment
		Intent i = new Intent(this, TaskPagerActivity.class);
		// tell TaskFragment which task to display by making the TaskId an Intent extra
		i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
		startActivity(i);
		finish();
		
	}

	@Override
	public boolean onItemLongClick(AdapterViewCompat<?> parent, View view,
			int position, long id) {
		Task task = mAdapter.getItem(position);
		Log.d(TAG, "longClickdItem : " + task.getTaskTitle());
		showDeleteDialogConfirmation(task);
		return true;
	}


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
