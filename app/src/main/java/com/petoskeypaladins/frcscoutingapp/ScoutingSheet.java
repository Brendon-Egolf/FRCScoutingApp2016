package com.petoskeypaladins.frcscoutingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class ScoutingSheet extends Fragment {
    private int highGoalMadeCount, highGoalMissedCount, lowGoalMadeCount, lowGoalMissedCount;




    public ScoutingSheet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        final View view = inflater.inflate(R.layout.fragment_scouting_sheet, container, false);
        final EditText roundNumber = (EditText)view.findViewById(R.id.round_number);
        final EditText teamNumber = (EditText) view.findViewById(R.id.team_number);

        final Button submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(view);
            }
        });


        roundNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    teamNumber.requestFocus();

                }

                return false;
            }
        });


//        final ScrollView scroll = (ScrollView)view.findViewById(R.id.scrollView);
//        scroll.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (roundNumber.hasFocus()) {
//                    roundNumber.clearFocus();
//                }
//                if (teamNumber.hasFocus()) {
//                    teamNumber.clearFocus();
//                }
//                return false;
//            }
//        });

        final Spinner autonDefenseSpinner = (Spinner) view.findViewById(R.id.auton_defense);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.defenses, android.R.layout.simple_spinner_dropdown_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        autonDefenseSpinner.setAdapter(arrayAdapter);

        final CheckBox canAuton = (CheckBox) view.findViewById(R.id.can_auton);
        final CheckBox canAutonShoot = (CheckBox) view.findViewById(R.id.can_auton_shoot);
        final RadioGroup autonShootType = (RadioGroup) view.findViewById(R.id.auton_shoot_type);

        canAuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canAuton.isChecked()) {
                    autonDefenseSpinner.setVisibility(View.VISIBLE);
                    canAutonShoot.setVisibility(View.VISIBLE);
                } else {
                    autonDefenseSpinner.setVisibility(View.GONE);
                    canAutonShoot.setVisibility(View.GONE);
                    autonShootType.setVisibility(View.GONE);

                    autonDefenseSpinner.setSelection(0);
                    canAutonShoot.setChecked(false);
                    autonShootType.clearCheck();
                }
            }
        });

        canAutonShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canAutonShoot.isChecked()) {
                    autonShootType.setVisibility(View.VISIBLE);
                } else {
                    autonShootType.setVisibility(View.GONE);
                    autonShootType.clearCheck();
                }
            }
        });


        final LinearLayout defenseList = (LinearLayout)view.findViewById(R.id.defense_list);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final int DEFENSES = 5;

        for (int i = 0; i < DEFENSES; i++) {
            defenseList.addView(new Defense(getContext(), (i == 0)));
        }

        final CheckBox canCapture = (CheckBox) view.findViewById(R.id.can_capture);
        final CheckBox canClimb = (CheckBox) view.findViewById(R.id.can_climb);

        canCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canCapture.isChecked()) {
                    canClimb.setVisibility(View.VISIBLE);
                } else {
                    canClimb.setVisibility(View.GONE);
                    canClimb.setChecked(false);
                }
            }
        });

        final Button highGoalMadeAdd = (Button) view.findViewById(R.id.high_goal_made_add);
        final TextView highGoalMadeNumber = (TextView) view.findViewById(R.id.high_goal_made_number);
        final Button highGoalMadeSubtract = (Button) view.findViewById(R.id.high_goal_made_subtract);
        final Button highGoalMissedAdd = (Button) view.findViewById(R.id.high_goal_missed_add);
        final TextView highGoalMissedNumber = (TextView) view.findViewById(R.id.high_goal_missed_number);
        final Button highGoalMissedSubtract = (Button) view.findViewById(R.id.high_goal_missed_subtract);
        final Button lowGoalMadeAdd = (Button) view.findViewById(R.id.low_goal_made_add);
        final TextView lowGoalMadeNumber = (TextView) view.findViewById(R.id.low_goal_made_number);
        final Button lowGoalMadeSubtract = (Button) view.findViewById(R.id.low_goal_made_subtract);
        final Button lowGoalMissedAdd = (Button) view.findViewById(R.id.low_goal_missed_add);
        final TextView lowGoalMissedNumber = (TextView) view.findViewById(R.id.low_goal_missed_number);
        final Button lowGoalMissedSubtract = (Button) view.findViewById(R.id.low_goal_missed_subtract);

        highGoalMadeCount = 0;
        highGoalMadeNumber.setText(Integer.toString(highGoalMadeCount));
        highGoalMissedCount = 0;
        highGoalMissedNumber.setText(Integer.toString(highGoalMissedCount));
        lowGoalMadeCount = 0;
        lowGoalMadeNumber.setText(Integer.toString(lowGoalMadeCount));
        lowGoalMissedCount = 0;
        lowGoalMissedNumber.setText(Integer.toString(lowGoalMissedCount));


        highGoalMadeSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highGoalMadeCount--;
                highGoalMadeNumber.setText(Integer.toString(highGoalMadeCount));
                if (highGoalMadeCount == 0) {
                    highGoalMadeSubtract.setVisibility(View.INVISIBLE);
                }
            }
        });

        highGoalMadeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highGoalMadeCount++;
                highGoalMadeNumber.setText(Integer.toString(highGoalMadeCount));
                if (highGoalMadeCount > 0) {
                    highGoalMadeSubtract.setVisibility(View.VISIBLE);
                }
            }
        });

        highGoalMissedSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highGoalMissedCount--;
                highGoalMissedNumber.setText(Integer.toString(highGoalMissedCount));
                if (highGoalMissedCount == 0) {
                    highGoalMissedSubtract.setVisibility(View.INVISIBLE);
                }
            }
        });

        highGoalMissedAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highGoalMissedCount++;
                highGoalMissedNumber.setText(Integer.toString(highGoalMissedCount));
                if (highGoalMissedCount > 0) {
                    highGoalMissedSubtract.setVisibility(View.VISIBLE);
                }
            }
        });

        lowGoalMadeSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowGoalMadeCount--;
                lowGoalMadeNumber.setText(Integer.toString(lowGoalMadeCount));
                if (lowGoalMadeCount == 0) {
                    lowGoalMadeSubtract.setVisibility(View.INVISIBLE);
                }
            }
        });

        lowGoalMadeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowGoalMadeCount++;
                lowGoalMadeNumber.setText(Integer.toString(lowGoalMadeCount));
                if (lowGoalMadeCount > 0) {
                    lowGoalMadeSubtract.setVisibility(View.VISIBLE);
                }
            }
        });

        lowGoalMissedSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowGoalMissedCount--;
                lowGoalMissedNumber.setText(Integer.toString(lowGoalMissedCount));
                if (lowGoalMissedCount == 0) {
                    lowGoalMissedSubtract.setVisibility(View.INVISIBLE);
                }
            }
        });

        lowGoalMissedAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowGoalMissedCount++;
                lowGoalMissedNumber.setText(Integer.toString(lowGoalMissedCount));
                if (lowGoalMissedCount > 0) {
                    lowGoalMissedSubtract.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }




    public void submitForm(View view) {
        EditText roundNumber = (EditText) view.findViewById(R.id.round_number);
        EditText teamNumber = (EditText) view.findViewById(R.id.team_number);
        CheckBox canAuton = (CheckBox) view.findViewById(R.id.can_auton);
        Spinner autonDefense = (Spinner) view.findViewById(R.id.auton_defense);
        CheckBox canAutonShoot = (CheckBox) view.findViewById(R.id.can_auton_shoot);
        RadioGroup autonShootType = (RadioGroup) view.findViewById(R.id.auton_shoot_type);
        LinearLayout teleopDefenses = (LinearLayout) view.findViewById(R.id.defense_list);



        try {
            String fileName = teamNumber.getText().toString();
            File file = new File("/storage/emulated/0/scouting/" + fileName + ".csv");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bufferedWriter;
            bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "IO error", Toast.LENGTH_SHORT).show();
        }
    }




    public void alert() {
        Toast toast = Toast.makeText(getContext(), "You did a thing", Toast.LENGTH_SHORT);
        toast.show();
    }




}
