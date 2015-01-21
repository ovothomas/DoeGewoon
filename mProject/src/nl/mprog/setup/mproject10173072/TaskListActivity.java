package nl.mprog.setup.mproject10173072;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class TaskListActivity extends ListActivity{
	public final String TAG = "TaskListActivity";
	private Button mTaskButton;
	private List<Task> mListTasks;
	private TaskDAO mTaskDAO;
	private TaskListAdapter mAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		//configuring the actionbar
		getActionBar().setTitle(R.string.task_title);
		
		
		
		mTaskDAO = new TaskDAO(this);
		mListTasks = mTaskDAO.getAllTasks();
		
		// retrieving the tasks in Taskstorage to display
		// and setting custom made adapter to populate the listview
		
		TaskListAdapter adapter = new TaskListAdapter(this, mListTasks);
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
	
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
	
		Task task = ((TaskListAdapter)getListAdapter()).getItem(position);
		Log.d(TAG, "clickedItem : " + task.getTaskTitle());
		
		//Start TaskpagerActivity and putExtra Id of the particular fragment
		Intent i = new Intent(this, TaskPagerActivity.class);
		// tell TaskFragment which task to display by making the TaskId an Intent extra
		i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
		startActivity(i);
		
	}
	 
	@Override
	public void onResume(){
		super.onResume();
		((TaskListAdapter)getListAdapter()).notifyDataSetChanged();
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
		//TaskStorage.get(getBaseContext()).addTask(task);
		Intent i = new Intent(getBaseContext(), TaskPagerActivity.class);
		i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
		startActivityForResult(i, 0);
	}

	public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
		Task task = mAdapter.getItem(position);
		Log.d(TAG, "longClickdItem : " + task.getTaskTitle());
		showDeleteDialogConfirmation(task);
		return true;
	}
	
	private void showDeleteDialogConfirmation(final Task task){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("DELETE");
		alertDialogBuilder.setMessage("Sure you want to delete \"" + task.getTaskTitle());
		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (mTaskDAO != null){
					mTaskDAO.deleteTask(task);
					mListTasks.remove(task);
					((TaskListAdapter)getListAdapter()).setItems(mListTasks);
					((TaskListAdapter)getListAdapter()).notifyDataSetChanged();
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
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	this.getMenuInflater().inflate(R.menu.task_list_item, menu);
	} 
}
