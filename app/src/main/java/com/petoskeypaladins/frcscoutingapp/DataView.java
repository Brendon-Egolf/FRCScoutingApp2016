package com.petoskeypaladins.frcscoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class DataView extends Fragment {
    JSONArray teamJSON;

    public DataView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_data_view, container, false);
        final ArrayList<JSONObject> teamList = new ArrayList<>();
        try {
            teamJSON = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < teamJSON.length(); i++) {
                teamList.add((JSONObject) teamJSON.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(new TeamArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                R.layout.team_list_view,
                teamList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), TeamData.class);
                try {
                    intent.putExtra("team-name", teamList.get(position).getString("nickname"));
                    intent.putExtra("team-number", Integer.toString(teamList.get(position).getInt("team_number")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        return view;
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream inputStream = getContext().getAssets().open("FRC-standish-event.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
