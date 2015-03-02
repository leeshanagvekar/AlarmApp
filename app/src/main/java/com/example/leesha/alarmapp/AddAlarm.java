package com.example.leesha.alarmapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class AddAlarm extends ActionBarActivity implements View.OnClickListener{

    Button setAlarm;
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    private AlarmModel alarmModel;

    static final int TIME_DIALOG_ID = 1;;
    private TextView time;
    private String strDate;
    private int hour;
    private int minute;

    static final int DATE_DIALOG_ID = 0;
    private TextView date;
    private int year, month, day;
    private TextView toneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.add_alarm);
        getActionBar().setTitle("Create New Alarm");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        time = (TextView) findViewById(R.id.timeTextView);
        date = (TextView) findViewById(R.id.daytTextView);
        setAlarm = (Button) findViewById(R.id.setAlarm);
        toneTextView = (TextView) findViewById(R.id.alarm_label_tone_selection);

        displayCurrentTimeAndDate();

        long id = getIntent().getExtras().getLong("id");

        if (id == -1) {
            alarmModel = new AlarmModel();
        } else {
            alarmModel = dbHelper.getAlarm(id);
            toneTextView.setText(RingtoneManager.getRingtone(this, alarmModel.alarmTone).getTitle(this));
        }
        final LinearLayout ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
        ringToneContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                startActivityForResult(intent , 1);
            }
        });
        setAlarm.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: {
                    alarmModel.alarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    toneTextView.setText(RingtoneManager.getRingtone(this, alarmModel.alarmTone).getTitle(this));
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
    public void displayCurrentTimeAndDate(){

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);
        // set current time into output TextView
        updateTime(hour, minute);

        // Get current date by calender
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        month = month + 1;
        day   = c.get(Calendar.DAY_OF_MONTH);

        // Show current date
        date.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month).append("-").append(day).append("-")
                .append(year).append(" "));
        //strDate = date.getText().toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void updateModelFromLayout() {
        alarmModel.timeMinute = minute;
        alarmModel.timeHour = hour;
        alarmModel._day = day;
        alarmModel._month = month;
        alarmModel._year = year;
    }

    public void setTime(View view) {
        createDialog(TIME_DIALOG_ID).show();
    }

    public void setDate(View view) {
        createDialog(DATE_DIALOG_ID).show();
    }

    protected Dialog createDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);
            case DATE_DIALOG_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;

            updateTime(hour,minute);

        }

    };
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            month = month + 1;
            day   = selectedDay;

            // Show selected date
            date.setText(new StringBuilder().append(month)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
            strDate = date.getText().toString();
        }
    };
    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

       /* String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";*/


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).toString();

        //time.setText(aTime);
    }

    @Override
    public void onClick(View v) {

        updateModelFromLayout();

        AlarmManagerHelper.cancelAlarms(this);

        if (alarmModel.id < 0) {
            dbHelper.createAlarm(alarmModel);
        } else {
            dbHelper.updateAlarm(alarmModel);
        }

        AlarmManagerHelper.setAlarms(this);

        setResult(RESULT_OK);
        finish();
    }
}
