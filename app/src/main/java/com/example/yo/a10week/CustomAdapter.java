package com.example.yo.a10week;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yo on 2017-05-10.
 */

public class CustomAdapter extends BaseAdapter{

    private ArrayList<Data> data = new ArrayList<Data>();

    public CustomAdapter(ArrayList<Data> data){
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.urllist, parent, false);
        }

        TextView textView = (TextView)convertView.findViewById(R.id.textView);

        textView.setText("<" + data.get(position).getSitename() +"> " + data.get(position).getUrl());

        return convertView;
    }

    public void addItem(String sitename, String url){
        Data item = new Data(sitename,url);
        data.add(item);
        this.notifyDataSetChanged();
    }

    public boolean checkitem(String sitename, String url){
        for(Data one : data){
            if (one.getUrl().equals(url) || one.getSitename().equals(sitename)) {
                return true;
            }
        }
        return false;
    }
    public void getremove(int position){
        data.remove(position);
        this.notifyDataSetChanged();
    }
}
