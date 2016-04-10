package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by begolf on 4/6/16.
 */
public class ListGroupAdapter extends ArrayAdapter<String> {
    private ArrayList<String> groupList;
    private Map<String, List<String>> teamLists;

    public ListGroupAdapter(Context context, int resource, int resourceId, ArrayList<String> groupList, Map<String, List<String>> teamLists) {
        super(context, resource, resourceId, groupList);
        this.groupList = groupList;
        this.teamLists = teamLists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sort_view, parent, false);
        TextView sortTitle = (TextView) view.findViewById(R.id.sort_value);
        sortTitle.setText(groupList.get(position));


        return view;
    }

    @Override
    public int getCount(){
        return groupList.size();
    }
}
