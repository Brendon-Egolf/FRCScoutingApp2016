package com.petoskeypaladins.frcscoutingapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SelectionLists extends Fragment {
    private ListGroupAdapter adapter;
    private ArrayList<String> lists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_selection_lists, container, false);
        lists = new ArrayList<>(Arrays.asList("Alliance Selection 1", "Alliance Selection 2"));
        ListView groupList = (ListView) view.findViewById(R.id.group_list);
        adapter = new ListGroupAdapter(getContext(), android.R.layout.simple_list_item_1,
                R.layout.list_header,
                lists);
        groupList.setAdapter(adapter);

        ImageButton addList = (ImageButton) view.findViewById(R.id.add_list);
        addList.setOutlineProvider(new ViewOutlineProvider() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void getOutline(View view, Outline outline) {
                int diameter = getResources().getDimensionPixelSize(R.dimen.diameter);
                outline.setOval(0, 0, diameter, diameter);
            }
        });
        addList.setClipToOutline(true);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.new_list, null);
                alertBuilder.setView(view);
                alertBuilder.setTitle("New List");
                final AlertDialog alertDialog = alertBuilder.create();
//                Toast.makeText(getContext(), "cool", Toast.LENGTH_SHORT).show();
                final EditText title = (EditText) view.findViewById(R.id.new_list_title);
                title.setSingleLine(true);
                title.setHorizontallyScrolling(false);
                title.setImeActionLabel("Done", 0);

                title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == 0) {
                            if (title.getText().length() > 0) {
                                lists.add(title.getText().toString());
                                adapter.notifyDataSetChanged();
                                alertDialog.cancel();
                            }
                        } else {
                            Toast.makeText(getContext(), "Fill out the textbox first", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                alertDialog.show();
            }
        });

        return view;
    }

}
