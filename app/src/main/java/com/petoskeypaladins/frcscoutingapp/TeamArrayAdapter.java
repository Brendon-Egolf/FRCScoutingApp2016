package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by begolf on 2/25/16.
 */
public class TeamArrayAdapter extends ArrayAdapter<JSONObject> {
    private ArrayList<JSONObject> teamList;

    public TeamArrayAdapter(Context context, int resource, int resourceId, ArrayList<JSONObject> teamList) {
        super(context, resource, resourceId, teamList);
        this.teamList = teamList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.team_list_view, parent, false);
        TextView teamNumber = (TextView) view.findViewById(R.id.team_number);
        TextView teamName = (TextView) view.findViewById(R.id.team_name);
        TextView teamLocation = (TextView) view.findViewById(R.id.team_location);
        try {
            teamNumber.setText(Integer.toString(teamList.get(position).getInt("team_number")));
            teamName.setText(teamList.get(position).getString("nickname"));
            teamLocation.setText(teamList.get(position).getString("location"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public int getCount(){
        return teamList.size();
    }
}
