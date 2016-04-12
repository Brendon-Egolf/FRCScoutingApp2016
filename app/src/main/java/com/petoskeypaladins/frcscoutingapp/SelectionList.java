package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by begolf123 on 4/10/16.
 */
public class SelectionList {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> teams;
    private DragSortListView teamList;

    public SelectionList(Context context, View listView, ArrayList<String> teams) {
        this.teams = teams;

        teamList = (DragSortListView) listView;
        adapter = new ArrayAdapter<String>(context,
                R.layout.sort_view, R.id.sort_value, teams);
        teamList.setAdapter(adapter);
        teamList.setDropListener(onSortDrop);
        teamList.setRemoveListener(onSortRemove);


        DragSortController sortController = new DragSortController(teamList);
        sortController.setDragHandleId(R.id.reorder);
        sortController.setRemoveEnabled(true);
        sortController.setClickRemoveId(R.id.remove);
        sortController.setSortEnabled(true);
        sortController.setDragInitMode(DragSortController.ON_DOWN);
        sortController.setRemoveMode(DragSortController.CLICK_REMOVE);

        teamList.setFloatViewManager(sortController);
        teamList.setOnTouchListener(sortController);
        teamList.setDragEnabled(true);
    }

    private DragSortListView.DropListener onSortDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                String item = adapter.getItem(from);
                adapter.remove(item);
                adapter.insert(item, to);
            }
        }
    };

    private DragSortListView.RemoveListener onSortRemove = new DragSortListView.RemoveListener()
    {
        @Override
        public void remove(int which) {
            adapter.remove(adapter.getItem(which));
        }
    };

    public void addTeam(String team) {
        teams.add(team);
        adapter.notifyDataSetChanged();
    }

    public void clearTeams() {
        for (int i = 0; i < teams.size(); i++) {
            teams.remove(i);
        }
        adapter.notifyDataSetChanged();
    }

    public ArrayList<String> getTeams() {
        return teams;
    }
}
