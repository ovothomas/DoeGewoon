package nl.mprog.setup.mproject10173072;

import java.util.UUID;

import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class TaskFragment extends Fragment implements OnDateSetListener {
	
	public static final String EXTRA_TASK_ID =
			"nl.mprog.setup.mproject10173072.task_id";
	
	//member variable for Task
	private Task mTask;
	
	//variables 
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
		
	private EditText mTaskTitle;
	private EditText mTaskDetails;
	private Button mTaskDateButton;
	private CheckBox mTaskCompletedCheckBox;
	private Button mTaskTimeButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UUID taskId = (UUID)getArguments().getSerializable(EXTRA_TASK_ID);
		mTask = TaskStorage.get(getActivity()).getTask(taskId);
		
	}
	
	// Inflate the layout view
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_task, parent, false);
		
		// Wiring up EditText to respond to user input
		mTaskDetails = (EditText)view.findViewById(R.id.task_details);
		mTaskDetails.setText(mTask.getTaskDetails());
		mTaskDetails.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				mTask.setTaskDetails(s.toString());
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		//Wiring up the edittext to respond to user input
		mTaskTitle = (EditText)view.findViewById(R.id.task_title);
		mTaskTitle.setText(mTask.getTaskTitle());
		mTaskTitle.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				mTask.setTaskTitle(s.toString());
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		//Setting and wiring completed checkbox
		mTaskCompletedCheckBox = (CheckBox)view.findViewById(R.id.task_completed);
		mTaskCompletedCheckBox.setChecked(mTask.isCompleted());
		mTaskCompletedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				mTask.setCompleted(isChecked);
			}
		});
		
		//Setting and wiring the date button to show date in custom format
		mTaskDateButton = (Button)view.findViewById(R.id.task_date);
		mTaskDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy",mTask.getTaskDate()));
		mTaskDateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerFragment datepicker = new DatePickerFragment(TaskFragment.this, mYear, mMonth, mDay);
				datepicker.show(getActivity().getSupportFragmentManager(), "");
			}});
		
		//setting up timeButton to show the time the task was made
		mTaskTimeButton = (Button)view.findViewById(R.id.task_time);
		mTaskTimeButton.setText(DateFormat.format("hh:mm", mTask.getTaskDate()));
		mTaskTimeButton.setEnabled(false);
		
		return view;
	}
	
	public static TaskFragment newInstance(UUID taskId){
		
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TASK_ID, taskId);
		
		TaskFragment fragment = new TaskFragment();
		fragment.setArguments(args);
		
		return fragment;
		
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		 
		
	}
}
