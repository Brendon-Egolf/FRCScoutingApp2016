package com.petoskeypaladins.frcscoutingapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class SelectionLists extends Fragment {
    private static ListGroupAdapter adapter;
    private ArrayList<String> lists;
    private BufferedWriter writer;
    private final String FILENAME = "selection-lists";
    private static File file;
    private static boolean loaded;

    public SelectionLists() {
        file = new File("/storage/emulated/0/" + FILENAME + ".csv");
        loaded = false;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_selection_lists, container, false);
        lists = new ArrayList<>(Arrays.asList("Alliance Selection 1",
                "Alliance Selection 2",
                "Breaching",
                "Ball Volume",
                "Defense",
                "Cheesecake"));
        ListView groupList = (ListView) view.findViewById(R.id.group_list);
        adapter = new ListGroupAdapter(getContext(), android.R.layout.simple_list_item_1,
                R.layout.list_header,
                lists);
        groupList.setAdapter(adapter);

//        ImageButton addList = (ImageButton) view.findViewById(R.id.add_list);
//        addList.setOutlineProvider(new ViewOutlineProvider() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void getOutline(View view, Outline outline) {
//                int diameter = getResources().getDimensionPixelSize(R.dimen.diameter);
//                outline.setOval(0, 0, diameter, diameter);
//            }
//        });
//        addList.setClipToOutline(true);
//        addList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
//                View view = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.new_list, null);
//                alertBuilder.setView(view);
//                alertBuilder.setTitle("New List");
//                final AlertDialog alertDialog = alertBuilder.create();
////                Toast.makeText(getContext(), "cool", Toast.LENGTH_SHORT).show();
//                final EditText title = (EditText) view.findViewById(R.id.new_list_title);
//                title.setSingleLine(true);
//                title.setHorizontallyScrolling(false);
//                title.setImeActionLabel("Done", 0);
//
//                title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if (actionId == 0) {
//                            if (title.getText().length() > 0) {
//                                lists.add(title.getText().toString());
//                                adapter.notifyDataSetChanged();
//                                alertDialog.cancel();
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "Fill out the textbox first", Toast.LENGTH_SHORT).show();
//                        }
//                        return true;
//                    }
//                });
//
//                alertDialog.show();
//            }
//        });

        return view;
    }

    public static void load() {
        BufferedReader reader;
        loaded = true;

        try {
            reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line;
            int lineNumber = 0;
            String[] teams;
            while ((line = reader.readLine()) != null) {
                teams = line.split(",");
                if (teams.length > 0) {
                    for (String team : teams) {
                        adapter.clearList(lineNumber);
                        if (team.length() > 0) {
                            adapter.addTeam(lineNumber, team);
                        }
                    }
                    lineNumber++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        ArrayList<String> subList;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

            for (int i = 0; i < lists.size(); i++) {
                subList = adapter.getSubList(i);

                for (int j = 0; j < subList.size(); j++) {
                    if (j < subList.size() - 1) {
                        write(subList.get(j));
                    } else {
                        writer.write(subList.get(j));
                    }
                }
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String data) {
        try {
            writer.write(data + ",");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "IO error", Toast.LENGTH_SHORT).show();
        }
    }

    public void collapseAll() {
        adapter.setCollapsed(true);
    }

    public void unlockCollapse() {
        adapter.setCollapsed(false);
    }
}
