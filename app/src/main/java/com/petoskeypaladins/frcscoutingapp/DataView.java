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
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;


public class DataView extends Fragment {
    JSONArray teamJson;

    List<String> paramGroups;
    List<String> paramList;
    Map<String, List<String>> paramCollection;
    ExpandableListView expListView;
    ArrayAdapter<String> sortAdapter;
    FilterArrayAdapter filterAdapter;
    DragSortListView sortList;
    ListView filterList;
    ArrayList<String> sortParameters = new ArrayList();
    ArrayList<String> filterParameters = new ArrayList();
    HashMap<String, Integer> parameterKeys;
    ArrayList<JSONObject> teamList;
    TeamArrayAdapter teamAdapter;
    ListView teamListView;

    private DragSortListView.DropListener onSortDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                String item = sortAdapter.getItem(from);
                sortAdapter.remove(item);
                sortAdapter.insert(item, to);
                refreshSort();
            }
        }
    };

    private DragSortListView.RemoveListener onSortRemove = new DragSortListView.RemoveListener()
    {
        @Override
        public void remove(int which)
        {
            sortAdapter.remove(sortAdapter.getItem(which));
            refreshSort();
        }
    };

    public DataView() {
        initializeParameterKeys();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_data_view, container, false);
        teamList = new ArrayList<>();

        loadTeamList();

        teamListView = (ListView) view.findViewById(R.id.list);
        teamAdapter = new TeamArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                R.layout.team_list_view,
                teamList);
        teamListView.setAdapter(teamAdapter);

        teamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        final RelativeLayout filterDropdown = (RelativeLayout) view.findViewById(R.id.filter);
        final LinearLayout sortFactors = (LinearLayout) view.findViewById(R.id.sort_factors);
        final ImageView sortArrow = (ImageView) view.findViewById(R.id.sort_drop);
        final LinearLayout filterFactors = (LinearLayout) view.findViewById(R.id.filter_factors);
        final ImageView filterArrow = (ImageView) view.findViewById(R.id.filter_drop);

        sortDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortFactors.getVisibility() == (View.GONE)) {
                    sortFactors.setVisibility(View.VISIBLE);
                    sortArrow.setImageDrawable(getResources().getDrawable(R.drawable.dropup_arrow));
                } else {
                    sortFactors.setVisibility(View.GONE);
                    sortArrow.setImageDrawable(getResources().getDrawable(R.drawable.dropdown_arrow));
                }
                filterFactors.setVisibility(View.GONE);
                filterArrow.setImageDrawable(getResources().getDrawable(R.drawable.dropdown_arrow));
            }
        });


        filterDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterFactors.getVisibility() == (View.GONE)) {
                    filterFactors.setVisibility(View.VISIBLE);
                    filterArrow.setImageDrawable(getResources().getDrawable(R.drawable.dropup_arrow));
                } else {
                    filterFactors.setVisibility(View.GONE);
                    filterArrow.setImageDrawable(getResources().getDrawable(R.drawable.dropdown_arrow));
                }
                sortFactors.setVisibility(View.GONE);
                sortArrow.setImageDrawable(getResources().getDrawable(R.drawable.dropdown_arrow));
            }
        });

        final TextView teamCount = (TextView) view.findViewById(R.id.team_count);

        teamCount.setText(Integer.toString(teamList.size()) + " teams");

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

        filterAdapter = new FilterArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                R.layout.filter_view,
                filterParameters);

        filterList.setAdapter(filterAdapter);

        Button filterRefresh = (Button) view.findViewById(R.id.filter_refresh);

        filterRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFilter();
                teamCount.setText(teamList.size() + " teams");
            }
        });

        return view;
    }

    public void loadTeamList() {
        try {
            teamJson = new JSONArray(loadJSONFromAsset());
            teamList = new ArrayList<>();
            for (int i = 0; i < teamJson.length(); i++) {
                teamList.add((JSONObject) teamJson.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream inputStream = getContext().getAssets().open("FRC-2016cur-event.json");
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
                if (!groupName.equals("General")) {
                    sortParameters.add(groupName + " " + childName);
                } else {
                    sortParameters.add(childName);
                }
//                Toast.makeText(getContext(), groupName + " " + childName, Toast.LENGTH_SHORT).show();
                sortAdapter.notifyDataSetChanged();
                refreshSort();
                alertDialog.cancel();
                return true;
            }
        });


        alertDialog.show();
    }

    public void displayFilterChooser() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.param_list, null);
        alertBuilder.setView(view);
        alertBuilder.setTitle("Filter Parameters");
        final AlertDialog alertDialog = alertBuilder.create();

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) view.findViewById(R.id.list_view);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                getActivity(), paramGroups, paramCollection);
        expListView.setAdapter(expListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String groupName = (String) expListAdapter.getGroup(groupPosition);
                final String childName = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                if (!groupName.equals("General")) {
                    filterParameters.add(groupName + " " + childName);
                } else {
                    filterParameters.add(childName);
                }
//                Toast.makeText(getContext(), groupName + " " + childName, Toast.LENGTH_SHORT).show();
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

    public void initializeParameterKeys() {
        parameterKeys = new HashMap<>();
        parameterKeys.put("Auton percentage", 0);
        parameterKeys.put("Auton Low Bar", 1);
        parameterKeys.put("Auton Portcullis", 2);
        parameterKeys.put("Auton Cheval de Frise", 3);
        parameterKeys.put("Auton Moat", 4);
        parameterKeys.put("Auton Ramparts", 5);
        parameterKeys.put("Auton Drawbridge", 6);
        parameterKeys.put("Auton Sally Port", 7);
        parameterKeys.put("Auton Rock Wall", 8);
        parameterKeys.put("Auton Rough Terrain", 9);
        parameterKeys.put("Auton attempts shot percentage", 10);
        parameterKeys.put("Auton high goal percentage", 11);
        parameterKeys.put("Auton low goal percentage", 12);
        parameterKeys.put("Teleop Low Bar", 13);
        parameterKeys.put("Teleop Portcullis", 14);
        parameterKeys.put("Teleop Cheval de Frise", 15);
        parameterKeys.put("Teleop Moat", 16);
        parameterKeys.put("Teleop Ramparts", 17);
        parameterKeys.put("Teleop Drawbridge", 18);
        parameterKeys.put("Teleop Sally Port", 19);
        parameterKeys.put("Teleop Rock Wall", 20);
        parameterKeys.put("Teleop Rough Terrain", 21);
        parameterKeys.put("Teleop high goal percentage", 22);
        parameterKeys.put("Teleop low goal percentage", 23);
        parameterKeys.put("Average shots per round", 24);
        parameterKeys.put("Total shots", 25);
        parameterKeys.put("Challenge percentage", 26);
        parameterKeys.put("Scale percentage", 27);
        parameterKeys.put("Average Score", 28);

    }

    public void refreshSort() {
        int switched = 1;
        JSONObject team;
        JSONObject teamAbove;
        JSONObject temp;

        while (switched > 0) {
            switched = 0;
            for (int i = 1; i < teamList.size(); i++) {
                team = teamList.get(i);
                teamAbove = teamList.get(i - 1);

                if (sortParameters.size() > 0) {
                    if (getTeamStat(team, sortParameters.get(0)) > getTeamStat(teamAbove, sortParameters.get(0))) {
                        teamList.set(i - 1, team);
                        teamList.set(i, teamAbove);
                        switched++;
                    }
                } else {
                    if (getTeamNumber(team) < getTeamNumber(teamAbove)) {
                        teamList.set(i - 1, team);
                        teamList.set(i, teamAbove);
                        switched++;
                    }
                }
            }
        }

        String previousParameter;
        String currentParameter;

        for (int i = 1; i < sortParameters.size(); i++) {
            switched = 1;
            previousParameter = sortParameters.get(i - 1);
            currentParameter = sortParameters.get(i);

            while (switched > 0) {
                switched = 0;

                for (int j = 1; j < teamList.size(); j++) {
                    team = teamList.get(j);
                    teamAbove = teamList.get(j - 1);

                    if (getTeamStat(team, previousParameter) == getTeamStat(teamAbove, previousParameter)) {
                        if (getTeamStat(team, currentParameter) > getTeamStat(teamAbove, currentParameter)) {
                            teamList.set(j - 1, team);
                            teamList.set(j, teamAbove);
                            switched++;
                        }
                    }
                }
            }
        }
        teamAdapter.notifyDataSetChanged();
    }

    public double getTeamStat(JSONObject team, String key) {
        try {
            String stringValue = loadTeamStats(Integer.toString(team.getInt("team_number")))[parameterKeys.get(key)];
            double value;
            if (stringValue.contains("%")) {
                value = Double.parseDouble(stringValue.substring(0, stringValue.length() - 1));
            } else {
                value = Double.parseDouble(stringValue);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getTeamNumber(JSONObject team) {
        try {
            return team.getInt("team_number");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void refreshFilter() {
        JSONObject team;
        String currentStat;

        loadTeamList();

        for (int i = 0; i < filterParameters.size(); i++) {
            currentStat = filterParameters.get(i);
            for (int j = teamList.size() - 1; j > -1; j--) {
                team = teamList.get(j);
                if (getTeamStat(team, currentStat) < filterAdapter.getValue(i)) {
                    teamList.remove(j);
                }
            }
        }
        teamAdapter = new TeamArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                R.layout.filter_view,
                teamList);

        teamListView.setAdapter(teamAdapter);
        refreshSort();
    }
}
