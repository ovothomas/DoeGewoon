package nl.mprog.setup.mproject10173072;
/*
 * Adapter to hold information
 * on how many task were completed
 * on a particular day
 */

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskStatsListAdapter extends BaseAdapter {
	Context context;
	Calendar calendar = Calendar.getInstance(); 
	
	//variables
	private List<Date> mItems;
	private LayoutInflater mInflater;
	
	public TaskStatsListAdapter(Context context, List<Date> listDates) {
		this.setItems(listDates);
		this.mInflater = LayoutInflater.from(context);
	}

	public List<Date> getItems() {
		return mItems;
	}

	public void setItems(List<Date> items) {
		mItems = items;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			convertView = mInflater.inflate(R.layout.activity_task_stats_row, parent, false);	
		}
		
		// get item position
		Date currentItem = getItem(position);
		//look for the view to populate with data
		TextView tvDate = (TextView)convertView.findViewById(R.id.task_stats_date);
		// Populate the data into the template view using the data object
		tvDate.setText(DateFormat.format("EEEE, MMM dd, yyyy",currentItem.getTaskDate()) + "  :  " + String.valueOf(currentItem.getCount()));
		
		return convertView;
	}

	@Override
	public Date getItem(int position) {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
	}
	
	public int getCount() {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
