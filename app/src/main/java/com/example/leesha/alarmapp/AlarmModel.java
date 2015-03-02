package com.example.leesha.alarmapp;

import android.net.Uri;

public class AlarmModel {

	public long id = -1;
	public int timeHour;
	public int timeMinute;
    public int _day,_month,_year;
	//public String date;
	public Uri alarmTone;
    public boolean isEnabled;
	
	public AlarmModel() {
	}
}
