package nl.mprog.setup.mproject10173072;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment {
	public static final String EXTRA_TIME = "nl.mprog.setup.mproject10173072.time";
	
	private Date mTime;
	private int mHour, mMinute;
	
	public static TimePickerFragment newInstance(long l){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, l);
		
		//Creating timpickerfragment
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	private void sendResult(int resultCode){
		if (getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_TIME, mTime);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		// get the time
		mTime = (Date)getArguments().getSerializable(EXTRA_TIME);
		
		TimePickerDialog timepicker = new TimePickerDialog(getActivity(), new
				OnTimeSetListener(){
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						// TODO Auto-generated method stub
						mTime = new GregorianCalendar(0, 0, 0, hourOfDay, minute).getTime();
						sendResult(Activity.RESULT_OK);
					}
		}, mHour, mMinute,false);
		return timepicker;
	}
}
