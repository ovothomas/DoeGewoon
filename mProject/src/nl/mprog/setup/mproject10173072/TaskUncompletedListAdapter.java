package nl.mprog.setup.mproject10173072;
/*
 * TaskListAdapter holds the list 
 * of tasks to be populated in a
 * listview
 */

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskUncompletedListAdapter extends BaseAdapter {
	Context context;
	Calendar calendar = Calendar.getInstance(); 
	public static final String TAG = "ListTaskAdapter";
	
	// member variables
	private List<Task> mItems;
	private LayoutInflater mInflater;
	private TaskDataBase mDatabase;
	
	public TaskUncompletedListAdapter(Context context, List<Task> listTasks, TaskDataBase database) {
		this.setItems(listTasks);
		this.mInflater = LayoutInflater.from(context);
		this.mDatabase = database;
	}

	@Override
	public int getCount() {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
	}

	public Task getItem(int position) {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
	}

	public Long getItemId(Long position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			convertView = mInflater.inflate(R.layout.activity_task_row, parent, false);	
		}
		
		// get item position
		final Task currentItem = getItem(position);
		
		//look for the view to populate with data
		TextView tvTitle = (TextView)convertView.findViewById(R.id.task_titleTextView);
		TextView tvDate = (TextView)convertView.findViewById(R.id.task_dateTextView);
		final CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.task_completed_checkbox);
		
		// Populate the data into the template view using the data object
		tvTitle.setText(currentItem.getTaskTitle());
		tvDate.setText(DateFormat.format("EEEE, MMM dd, yyyy", currentItem.getTaskDate()));
		checkBox.setChecked(currentItem.getCompleted() == 1 ? true : false);
		checkBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentItem.setCompleted(checkBox.isChecked() == true ? 1 : 0);
				mDatabase.updateTaskById(currentItem.getId(), currentItem.getTaskTitle(), currentItem.getTaskDetails()
						, currentItem.getTaskDate(), currentItem.getCompleted());
				if (checkBox.isChecked() == true) {
					mItems.remove(currentItem);
					notifyDataSetChanged();
				}		
			}
		});
		
		return convertView;
	}
	
	public List<Task> getItems() {
		return mItems;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setItems(List<Task> mItems) {
		this.mItems = mItems;
	}
}		