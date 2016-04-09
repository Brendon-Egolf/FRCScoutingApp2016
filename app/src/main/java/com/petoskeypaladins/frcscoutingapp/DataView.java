package com.petoskeypaladins.frcscoutingapp;

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
import android.widget.Toast;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;


public class DataView extends Fragment {
    JSONArray teamJSON;

    List<String> paramGroups;
    List<String> paramList;
    Map<String, List<String>> paramCollection;
    ExpandableListView expListView;
    ArrayAdapter<String> sortAdapter;
    ArrayAdapter<String> filterAdapter;
    DragSortListView sortList;
    ListView filterList;
    ArrayList<String> sortParameters = new ArrayList();
    ArrayList<String> filterParameters = new ArrayList();

    private DragSortListView.DropListener onSortDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                String item = sortAdapter.getItem(from);
                sortAdapter.remove(item);
                sortAdapter.insert(item, to);
            }
        }
    };

    private DragSortListView.RemoveListener onSortRemove = new DragSortListView.RemoveListener()
    {
        @Override
        public void remove(int which)
        {
            sortAdapter.remove(sortAdapter.getItem(which));
        }
    };

    public DataView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_data_view, container, false);
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
                    intent.putExtra("team-name",
                            teamList.get(position).getString("nickname"));
                    intent.putExtra("team-number",
                            Integer.toString(teamList.get(position).getInt("team_number")));
                    intent.putExtra("team-stats",
                            loadTeamStats(Integer.toString(teamList.get(position).getInt("team_number"))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        final RelativeLayout sortDropdown = (RelativeLayout) view.findViewById(R.id.sort);

        sortDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout sortFactors = (LinearLayout) view.findViewById(R.id.sort_factors);
                final ImageView dropDown = (ImageView) view.findViewById(R.id.sort_drop);
                if (sortFactors.getVisibility() == (View.GONE)) {
                    sortFactors.setVisibility(View.VISIBLE);
                    dropDown.setImageDrawable(getResources().getDrawable(R.drawable.dropup_arrow));
                } else {
                    sortFactors.setVisibility(View.GONE);
                    dropDown.setImageDrawable(getResources().getDrawable(R.drawable.dropdown_arrow));
                }
            }
        });

        final RelativeLayout filterDropdown = (RelativeLayout) view.findViewById(R.id.filter);

        filterDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout sortFactors = (LinearLayout) view.findViewById(R.id.filter_factors);
                final ImageView dropDown = (ImageView) view.findViewById(R.id.filter_drop);
                if (sortFactors.getVisibility() == (View.GONE)) {
                    sortFactors.setVisibility(View.VISIBLE);
                    dropDown.setImageDrawable(getResources().getDrawable(R.drawable.dropup_arrow));
                } else {
                    sortFactors.setVisibility(View.GONE);
                    dropDown.setImageDrawable(getResources().getDrawable(R.drawable.dropdown_arrow));
                }
            }
        });

        final Button addSort = (Button) view.findViewById(R.id.add_sort);

        addSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySortChooser();
            }
        });

        sortList = (DragSortListView) view.findViewById(R.id.sort_list);
        sortAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.sort_view, R.id.sort_value, sortParameters);
        sortList.setAdapter(sortAdapter);
        sortList.setDropListener(onSortDrop);
        sortList.setRemoveListener(onSortRemove);


        DragSortController sortController = new DragSortController(sortList);
        sortController.setDragHandleId(R.id.reorder);
        sortController.setRemoveEnabled(true);
        sortController.setClickRemoveId(R.id.remove);
        sortController.setSortEnabled(true);
        sortController.setDragInitMode(1);
        sortController.setRemoveMode(DragSortController.CLICK_REMOVE);

        sortList.setFloatViewManager(sortController);
        sortList.setOnTouchListener(sortController);
        sortList.setDragEnabled(true);

        final Button addFilter = (Button) view.findViewById(R.id.add_filter);

        addFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilterChooser();
            }
        });

        filterList = (ListView) view.findViewById(R.id.filter_list);

        return view;
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream inputStream = getContext().getAssets().open("FRC-2016milsu-event.json");
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

    public String[] loadTeamStats(String teamNumber) {
        String[] teamData =  new String[29];
        File file = new File("/storage/emulated/0/scouting/" + teamNumber + ".csv");
        BufferedReader reader;

        try {
            if (file.exists()) {
                reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                String line;

                ArrayList<String[]> teamMatchData = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    teamMatchData.add(line.split(","));
                }
                final int CAN_AUTON = 1,
                        AUTON_DEFENSE = 2,
                        CAN_AUTON_SHOOT = 3,
                        AUTON_SHOOT_TYPE = 4,
                        AUTON_MADE_SHOT = 5;
                final int[] DEFENSE_TYPE = {6, 8, 10, 12, 14};
                final int[] DEFENSE_AMOUNT = {7, 9, 11, 13, 15};
                final int HIGH_GOAL_SHOOT_PERCENT = 16,
                        LOW_GOAL_SHOOT_PERCENT = 17,
                        SHOTS_PER_ROUND = 18,
                        DID_CHALLENGE = 19,
                        DID_SCALE = 20,
                        SCORE = 21;
                int autonDoneCount = 0,
                        autonNotDoneCount = 0;
                String[] defenseNames = getResources().getStringArray(R.array.defenses);
                int[] autonDefenseCount = new int[defenseNames.length];
                int autonShootCount = 0,
                        autonNoShootCount = 0,
                        autonHighGoalMadeCount = 0,
                        autonLowGoalMadeCount = 0,
                        autonHighGoalMissedCount = 0,
                        autonLowGoalMissedCount = 0;
                int[] teleopDefenseCount = new int[defenseNames.length];
                float teleopHighGoalTotal = 0,
                        teleopLowGoalTotal = 0;
                int challengeCount = 0,
                        noChallengeCount = 0,
                        scaleCount = 0,
                        noScaleCount = 0,
                        totalShots  = 0;
                int totalScore = 0;

                for (String[] data : teamMatchData) {
                    try {
                        Integer.parseInt(data[data.length - 1]);
                        if (Boolean.parseBoolean(data[CAN_AUTON])) {
                            autonDoneCount++;
                        } else {
                            autonNotDoneCount++;
                        }
                        if (!(data[AUTON_DEFENSE].equals("None"))) {
                            for (int i = 0; i < autonDefenseCount.length; i++) {
                                if (data[AUTON_DEFENSE].equals(defenseNames[i])) {
                                    autonDefenseCount[i]++;
                                }
                            }
                        }
                        if (Boolean.parseBoolean(data[CAN_AUTON_SHOOT])) {
                            if (data[AUTON_SHOOT_TYPE].equals("Low Goal") && Boolean.parseBoolean(data[AUTON_MADE_SHOT])) {
                                autonLowGoalMadeCount++;
                            } else if (data[AUTON_SHOOT_TYPE].equals("Low Goal")) {
                                autonLowGoalMissedCount++;
                            }
                            if (data[AUTON_SHOOT_TYPE].equals("High Goal") && Boolean.parseBoolean(data[AUTON_MADE_SHOT])) {
                                autonHighGoalMadeCount++;
                            } else if (data[AUTON_SHOOT_TYPE].equals("High Goal")) {
                                autonHighGoalMissedCount++;
                            }
                            autonShootCount++;
                        } else {
                            autonNoShootCount++;
                        }
                        for (int i = 0; i < teleopDefenseCount.length; i++) {
                            for (int j = 0; j < DEFENSE_TYPE.length; j++) {
                                if (data[DEFENSE_TYPE[j]].equals(defenseNames[i])) {
                                    teleopDefenseCount[i] += Integer.parseInt(data[DEFENSE_AMOUNT[j]]);
                                }
                            }
                        }
                        teleopHighGoalTotal += Float.parseFloat(data[HIGH_GOAL_SHOOT_PERCENT]);
                        teleopLowGoalTotal += Float.parseFloat(data[LOW_GOAL_SHOOT_PERCENT]);
                        totalShots += Integer.parseInt(data[SHOTS_PER_ROUND]);
                        if (Boolean.parseBoolean(data[DID_CHALLENGE])) {
                            challengeCount++;
                        } else {
                            noChallengeCount++;
                        }
                        if (Boolean.parseBoolean(data[DID_SCALE])) {
                            scaleCount++;
                        } else {
                            noScaleCount++;
                        }
                        totalScore += Integer.parseInt(data[SCORE]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                final int CAN_AUTON_PERCENT = 0,
                        AUTON_LOW_BAR_COUNT = 1,
                        AUTON_ROUGH_TERRAIN_COUNT = 9,
                        CAN_AUTON_SHOOT_PERCENT = 10,
                        AUTON_HIGH_GOAL_PERCENT = 11,
                        AUTON_LOW_GOAL_PERCENT = 12,
                        TELEOP_LOW_BAR_COUNT = 13,
                        TELEOP_ROUGH_TERRAIN_COUNT = 21,
                        TELEOP_LOW_GOAL_PERCENT = 23,
                        TELEOP_HIGH_GOAL_PERCENT = 22,
                        AVERAGE_SHOTS_PER_ROUND = 24,
                        TOTAL_SHOTS = 25,
                        CHALLENGE_PERCENT = 26,
                        SCALE_PERCENT = 27,
                        SCORE_AVERAGE = 28;
                final int MATCHES = teamMatchData.size();

                teamData[CAN_AUTON_PERCENT] = getPercent(autonDoneCount, autonNotDoneCount + autonDoneCount);
                for (int i = AUTON_LOW_BAR_COUNT; i <= AUTON_ROUGH_TERRAIN_COUNT; i++)
                    teamData[i] = Integer.toString(autonDefenseCount[i - AUTON_LOW_BAR_COUNT]);
                
                teamData[CAN_AUTON_SHOOT_PERCENT] = getPercent(autonShootCount, autonNoShootCount + autonShootCount);
                teamData[AUTON_LOW_GOAL_PERCENT] = getPercent(autonLowGoalMadeCount, autonLowGoalMissedCount + autonLowGoalMadeCount);
                teamData[AUTON_HIGH_GOAL_PERCENT] = getPercent(autonHighGoalMadeCount, autonHighGoalMissedCount + autonHighGoalMadeCount);
                for (int i = TELEOP_LOW_BAR_COUNT; i <= TELEOP_ROUGH_TERRAIN_COUNT; i++) {
                    teamData[i] = Integer.toString(teleopDefenseCount[i - TELEOP_LOW_BAR_COUNT]);
                }
                teamData[TELEOP_LOW_GOAL_PERCENT] = getPercent(teleopLowGoalTotal, MATCHES);
                teamData[TELEOP_HIGH_GOAL_PERCENT] = getPercent(teleopHighGoalTotal, MATCHES);
                teamData[AVERAGE_SHOTS_PER_ROUND] = getAverage(totalShots, MATCHES);
                teamData[TOTAL_SHOTS] = Integer.toString(totalShots);
                teamData[CHALLENGE_PERCENT] = getPercent(challengeCount, challengeCount + noChallengeCount);
                teamData[SCALE_PERCENT] = getPercent(scaleCount, scaleCount + noScaleCount);
                teamData[SCORE_AVERAGE] = Integer.toString(totalScore / MATCHES);
            } else {
                teamData[0] = "no-data";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return teamData;
    }

    public void displaySortChooser() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        View popup = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.param_list, null);
        alertBuilder.setView(popup);
        alertBuilder.setTitle("Sort Parameters");
        final AlertDialog alertDialog = alertBuilder.create();

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) popup.findViewById(R.id.list_view);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                getActivity(), paramGroups, paramCollection);
        expListView.setAdapter(expListAdapter);
        
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String groupName = (String) expListAdapter.getGroup(groupPosition);
                final String childName = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                sortParameters.add(groupName + " " + childName);
                Toast.makeText(getContext(), groupName + " " + childName, Toast.LENGTH_SHORT).show();
                sortAdapter.notifyDataSetChanged();
                alertDialog.cancel();
                return true;
            }
        });


        alertDialog.show();
    }

    public void displayFilterChooser() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        View popup = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.param_list, null);
        alertBuilder.setView(popup);
        alertBuilder.setTitle("Filter Parameters");
        final AlertDialog alertDialog = alertBuilder.create();

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) popup.findViewById(R.id.list_view);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                getActivity(), paramGroups, paramCollection);
        expListView.setAdapter(expListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String groupName = (String) expListAdapter.getGroup(groupPosition);
                final String childName = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                filterParameters.add(groupName + " " + childName);
                Toast.makeText(getContext(), groupName + " " + childName, Toast.LENGTH_SHORT).show();
                filterAdapter.notifyDataSetChanged();
                alertDialog.cancel();
                return true;
            }
        });


        alertDialog.show();
    }

    private void createGroupList() {
        paramGroups = new ArrayList<String>();
        for (String paramGroup : getResources().getStringArray(R.array.param_types)) {
            paramGroups.add(paramGroup);
        }
    }

    private void createCollection() {
        // preparing laptops collection(child)
        String[] autonParams = getResources().getStringArray(R.array.auton_params);
        String[] teleopParams = getResources().getStringArray(R.array.teleop_params);
        String[] generalParams = getResources().getStringArray(R.array.general_params);

        paramCollection = new LinkedHashMap<String, List<String>>();

        for (String paramGroup : paramGroups) {
            if (paramGroup.equals("Auton")) {
                loadChild(autonParams);
            } else if (paramGroup.equals("Teleop"))
                loadChild(teleopParams);
            else
                loadChild(generalParams);

            paramCollection.put(paramGroup, paramList);
        }
    }

    private void loadChild(String[] params) {
        paramList = new ArrayList<String>();
        for (String param : params)
            paramList.add(param);
    }

    public String getPercent(float num, float total) {
        return Integer.toString((int) Math.round(num * 100.0 / total)) + "%";
    }

    public String getAverage(float num, float total) {
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        return decimalFormat.format(num / total);
    }

    public void alert(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
