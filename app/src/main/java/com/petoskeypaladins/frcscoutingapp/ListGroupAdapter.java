package com.petoskeypaladins.frcscoutingapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;

/**
 * Created by begolf on 4/6/16.
 */
public class ListGroupAdapter extends ArrayAdapter<String> {
    private ArrayList<String> groupList;
    private ArrayList<ArrayList<String>> teamLists;
    private ArrayList<SelectionList> selectionLists;
    private boolean collapsed;

    public ListGroupAdapter(Context context, int resource, int resourceId, ArrayList<String> groupList) {
        super(context, resource, resourceId, groupList);
        this.groupList = groupList;
        teamLists = new ArrayList<>();
        selectionLists = new ArrayList<>();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int visibility;
        try {
            visibility = convertView.findViewById(R.id.sub_list).getVisibility();
        } catch (NullPointerException e) {
            visibility = View.GONE;
        }
        if (collapsed)
            visibility = View.GONE;
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_header, parent, false);
        final DragSortListView selectionListView = (DragSortListView) view.findViewById(R.id.sub_list);
        selectionListView.setVisibility(visibility);
        TextView groupTitle = (TextView) view.findViewById(R.id.list_title);
        final ImageView dropArrow = (ImageView) view.findViewById(R.id.drop_arrow);
        ImageView addTeam = (ImageView) view.findViewById(R.id.add_team);
        groupTitle.setText(groupList.get(position));
        RelativeLayout titleBar = (RelativeLayout) view.findViewById(R.id.group_title);

        if (teamLists.size() <= position) {
            teamLists.add(new ArrayList<String>());
        }

        if (visibility == View.VISIBLE) {
            dropArrow.setImageDrawable(getContext().getDrawable(R.drawable.dropup_arrow));
        }

        selectionLists.add(new SelectionList(getContext(), selectionListView, teamLists.get(position)));

        titleBar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (selectionListView.getVisibility() == View.GONE) {
                    selectionListView.setVisibility(View.VISIBLE);
                    dropArrow.setImageDrawable(getContext().getDrawable(R.drawable.dropup_arrow));
                }
                else {
                    selectionListView.setVisibility(View.GONE);
                    dropArrow.setImageDrawable(getContext().getDrawable(R.drawable.dropdown_arrow));
                }
            }
        });

//        ImageView deleteList = (ImageView) view.findViewById(R.id.delete_list);
//        deleteList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                groupList.remove(position);
//                notifyDataSetChanged();
//            }
//        });

        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                View view = inflater.inflate(R.layout.new_team, null);
                alertBuilder.setView(view);
                alertBuilder.setTitle("New Team");
                final AlertDialog alertDialog = alertBuilder.create();
//                Toast.makeText(getContext(), "cool", Toast.LENGTH_SHORT).show();
                final EditText teamNumber = (EditText) view.findViewById(R.id.new_team_number);
                teamNumber.setSingleLine(true);
                teamNumber.setHorizontallyScrolling(false);
                teamNumber.setImeActionLabel("Done", 0);

                teamNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == 0) {
                            if (teamNumber.getText().length() > 0) {
                                selectionLists.get(position).addTeam(teamNumber.getText().toString());
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

    @Override
    public int getCount(){
        return groupList.size();
    }

    public ArrayList<String> getSubList(int position) {
        return selectionLists.get(position).getTeams();
    }

    public void addTeam(int position, String team) {
        selectionLists.get(position).addTeam(team);
    }

    public void clearList(int position) {
        selectionLists.get(position).clearTeams();
    }

    public boolean getReady() {
        return selectionLists.size() == getCount();
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }
}
