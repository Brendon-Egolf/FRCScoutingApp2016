package com.petoskeypaladins.frcscoutingapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DefenseRecommender extends Fragment {
    private static final int POSITIONS = 6;

    public DefenseRecommender() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_data_view, container, false);
        LinearLayout teamContainer = (LinearLayout) view.findViewById(R.id.team_container);
        ArrayList<TeamPositionView> teamPositionViews = new ArrayList<>();
        String alliance;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 1; i <= POSITIONS; i++) {
            if (i <= 3) {
                alliance = "Red ";
                teamPositionViews.add(new TeamPositionView(getContext(), alliance + i));
            } else {
                alliance = "Blue ";
                teamPositionViews.add(new TeamPositionView(getContext(), alliance + (i - 3)));
            }
            teamPositionViews.get(i - 1).setLayoutParams(layoutParams);
            teamContainer.addView(teamPositionViews.get(i - 1));
        }



        return view;
    }
}
