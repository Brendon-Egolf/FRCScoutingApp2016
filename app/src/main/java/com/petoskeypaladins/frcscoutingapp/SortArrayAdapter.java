package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by begolf on 4/6/16.
 */
public class SortArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> sortList;

    public SortArrayAdapter(Context context, int resource, int resourceId, ArrayList<String> sortList) {
        super(context, resource, resourceId, sortList);
        this.sortList = sortList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sort_view, parent, false);
        TextView sortTitle = (TextView) view.findViewById(R.id.sort_value);
        sortTitle.setText(sortList.get(position));


        return view;
    }

    @Override
    public int getCount(){
        return sortList.size();
    }
}
