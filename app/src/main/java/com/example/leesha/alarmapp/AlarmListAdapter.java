package com.example.leesha.alarmapp;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AlarmListAdapter extends BaseAdapter {

	private Context mContext;
	private List<AlarmModel> mAlarms;

	public AlarmListAdapter(Context context, List<AlarmModel> alarms) {
		mContext = context;
		mAlarms = alarms;
	}
	
	public void setAlarms(List<AlarmModel> alarms) {
		mAlarms = alarms;
	}
	
	@Override
	public int getCount() {
		if (mAlarms != null) {
			return mAlarms.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mAlarms != null) {
			return mAlarms.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (mAlarms != null) {
			return mAlarms.get(position).id;
		}
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.list_item, parent, false);
		}
		
		AlarmModel model = (AlarmModel) getItem(position);
		
		TextView txtTime = (TextView) view.findViewById(R.id.timeTextView);
		txtTime.setText(String.format("%02d : %02d", model.timeHour, model.timeMinute));
		
		Switch btnToggle = (Switch) view.findViewById(R.id.switch1);
		btnToggle.setChecked(model.isEnabled);
		btnToggle.setTag(Long.valueOf(model.id));
		btnToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				((MainActivity) mContext).setAlarmEnabled(((Long) buttonView.getTag()).longValue(), isChecked);
			}
		});
		
		view.setTag(Long.valueOf(model.id));
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				((MainActivity) mContext).startAlarmDetailsActivity(((Long) view.getTag()).longValue());
			}
		});
		
		/*view.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				((MainActivity) mContext).deleteAlarm(((Long) view.getTag()).longValue());
				return true;
			}
		});*/
		
		return view;
	}
	
	private void updateTextColor(TextView view, boolean isOn) {
		if (isOn) {
			view.setTextColor(Color.GREEN);
		} else {
			view.setTextColor(Color.BLACK);
		}
	}

}
