package com.example.leesha.alarmapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlarmDBHelper extends SQLiteOpenHelper{
    public  static  final  String DBName = "alarmDB.db";
    public  static  final  String DBTable = "alarmDetails";
    public  static  final  String ID = "id";
    public  static  final  String HourColumn = "HourCol";
    public  static  final  String MinColumn = "MinCol";
    //public  static  final  String DateColumn= "DateCol";
    public  static  final  String AlarmToneColumn = "Tone";
    public  static  final String  SwitchColumn = "SwitchCol";
    public  static  final  String DayColumn = "day";
    public  static  final  String MonthColumn = "month";
    public  static  final  String YearColumn = "year";

    public AlarmDBHelper(Context context) {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table"+DBTable+"(id INTEGER PRIMARY KEY,day INTEGER,month INTEGER,year INTEGER,HourCol INTEGER,MinCol INTEGER,Tone TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+DBTable);
        onCreate(db);
    }
    private AlarmModel populateModel(Cursor c) {
        AlarmModel model = new AlarmModel();
        model.id = c.getLong(c.getColumnIndex(AlarmDBHelper.ID));
        model.timeHour = c.getInt(c.getColumnIndex(AlarmDBHelper.HourColumn));
        model.timeMinute = c.getInt(c.getColumnIndex(AlarmDBHelper.MinColumn));
        model._day = c.getInt(c.getColumnIndex(AlarmDBHelper.DayColumn));
        model._month = c.getInt(c.getColumnIndex(AlarmDBHelper.MonthColumn));
        model._year = c.getInt(c.getColumnIndex(AlarmDBHelper.YearColumn));
        model.isEnabled = c.getInt(c.getColumnIndex(AlarmDBHelper.SwitchColumn)) == 0 ? false : true;
        model.alarmTone = c.getString(c.getColumnIndex(AlarmDBHelper.AlarmToneColumn)) != "" ? Uri.parse(c.getString(c.getColumnIndex(AlarmDBHelper.AlarmToneColumn))) : null;

        return model;
    }

    private ContentValues populateContent(AlarmModel model) {
        ContentValues values = new ContentValues();
        values.put(AlarmDBHelper.DayColumn, model._day);
        values.put(AlarmDBHelper.MonthColumn, model._month);
        values.put(AlarmDBHelper.YearColumn, model._year);
        values.put(AlarmDBHelper.HourColumn, model.timeHour);
        values.put(AlarmDBHelper.MinColumn, model.timeMinute);
        values.put(AlarmDBHelper.SwitchColumn, model.isEnabled);
        values.put(AlarmDBHelper.AlarmToneColumn, model.alarmTone != null ? model.alarmTone.toString() : "");

        return values;
    }

    public long createAlarm(AlarmModel model) {
        ContentValues values = populateContent(model);
        return getWritableDatabase().insert(DBTable, null, values);
    }

    public long updateAlarm(AlarmModel model) {
        ContentValues values = populateContent(model);
        return getWritableDatabase().update(DBTable, values, ID + " = ?", new String[] { String.valueOf(model.id) });
    }

    public AlarmModel getAlarm(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + DBTable + " WHERE " + ID + " = " + id;

        Cursor c = db.rawQuery(select, null);

        if (c.moveToNext()) {
            return populateModel(c);
        }

        return null;
    }

    public List<AlarmModel> getAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + DBTable;

        Cursor c = db.rawQuery(select, null);

        List<AlarmModel> alarmList = new ArrayList<>();

        while (c.moveToNext()) {
            alarmList.add(populateModel(c));
        }

        if (!alarmList.isEmpty()) {
            return alarmList;
        }

        return null;
    }

    public int deleteAlarm(long id) {
        return getWritableDatabase().delete(DBTable, ID + " = ?", new String[] { String.valueOf(id) });
    }
}

