package com.example.leesha.alarmapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

public class MyAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List list;

    public MyAdapter(Activity activity, List list) {
        this.activity = activity;
        this.list = list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        convertView = inflater.inflate(R.layout.list_item, null);


        return null;
    }
}
