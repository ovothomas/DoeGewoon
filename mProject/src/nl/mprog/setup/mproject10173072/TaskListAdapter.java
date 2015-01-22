package nl.mprog.setup.mproject10173072;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter {
	Context context;
	
	public static final String TAG = "ListTaskAdapter";
	
	private List<Task> mItems;
	private LayoutInflater mInflater;
	
	public TaskListAdapter(Context context, List<Task> listTasks) {
		this.setItems(listTasks);
		this.mInflater = LayoutInflater.from(context);
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

		//look for the view to populate with data
		TextView tvTitle = (TextView)convertView.findViewById(R.id.task_titleTextView);
		Task currentItem = getItem(position);
		// Populate the data into the template view using the data object
		tvTitle.setText(currentItem.getTaskTitle());
		
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



	 

		