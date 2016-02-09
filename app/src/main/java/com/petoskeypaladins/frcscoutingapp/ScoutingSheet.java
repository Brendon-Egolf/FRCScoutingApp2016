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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class ScoutingSheet extends Fragment {
    private int highGoalMadeCount, highGoalMissedCount, lowGoalMadeCount, lowGoalMissedCount;
    private int[] defensePasses = new int[5];
    BufferedWriter writer;
    final int ONE = 1;

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

        final Spinner defense1Type = (Spinner) view.findViewById(R.id.defense_1_type);
        final Button defense1AddPass = (Button) view.findViewById(R.id.defense_1_add_pass);
        final TextView defense1Passes = (TextView) view.findViewById(R.id.defense_1_passes);
        final Button defense1SubtractPass = (Button) view.findViewById(R.id.defense_1_subtract_pass);

        final Spinner defense2Type = (Spinner) view.findViewById(R.id.defense_2_type);
        final Button defense2AddPass = (Button) view.findViewById(R.id.defense_2_add_pass);
        final TextView defense2Passes = (TextView) view.findViewById(R.id.defense_2_passes);
        final Button defense2SubtractPass = (Button) view.findViewById(R.id.defense_2_subtract_pass);

        final Spinner defense3Type = (Spinner) view.findViewById(R.id.defense_3_type);
        final Button defense3AddPass = (Button) view.findViewById(R.id.defense_3_add_pass);
        final TextView defense3Passes = (TextView) view.findViewById(R.id.defense_3_passes);
        final Button defense3SubtractPass = (Button) view.findViewById(R.id.defense_3_subtract_pass);

        final Spinner defense4Type = (Spinner) view.findViewById(R.id.defense_4_type);
        final Button defense4AddPass = (Button) view.findViewById(R.id.defense_4_add_pass);
        final TextView defense4Passes = (TextView) view.findViewById(R.id.defense_4_passes);
        final Button defense4SubtractPass = (Button) view.findViewById(R.id.defense_4_subtract_pass);

        final Spinner defense5Type = (Spinner) view.findViewById(R.id.defense_5_type);
        final Button defense5AddPass = (Button) view.findViewById(R.id.defense_5_add_pass);
        final TextView defense5Passes = (TextView) view.findViewById(R.id.defense_5_passes);
        final Button defense5SubtractPass = (Button) view.findViewById(R.id.defense_5_subtract_pass);

        for (int i:defensePasses) {
            i = 0;
        }


        defense1Passes.setText(Integer.toString(defensePasses[1-ONE]));
        defense2Passes.setText(Integer.toString(defensePasses[2-ONE]));
        defense3Passes.setText(Integer.toString(defensePasses[3-ONE]));
        defense4Passes.setText(Integer.toString(defensePasses[4-ONE]));
        defense5Passes.setText(Integer.toString(defensePasses[5-ONE]));

        ArrayAdapter<CharSequence> defenseArrayAdapter;
        ArrayAdapter<CharSequence> lowBarArrayAdapter;

        lowBarArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.low_bar, android.R.layout.simple_spinner_dropdown_item);
        defenseArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.teleop_defenses, android.R.layout.simple_spinner_dropdown_item);

        defenseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lowBarArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        defense1Type.setAdapter(lowBarArrayAdapter);

        defense1SubtractPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[1 - ONE]--;
                defense1Passes.setText(Integer.toString(defensePasses[1 - ONE]));
                if (defensePasses[1 - ONE] == 0) {
                    defense1SubtractPass.setVisibility(View.INVISIBLE);
                }
            }
        });

        defense1AddPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[1 - ONE]++;
                defense1Passes.setText(Integer.toString(defensePasses[1 - ONE]));
                if (defensePasses[1 - ONE] > 0) {
                    defense1SubtractPass.setVisibility(View.VISIBLE);
                }
            }
        });

        defense2Type.setAdapter(defenseArrayAdapter);
        defense2SubtractPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[2 - ONE]--;
                defense2Passes.setText(Integer.toString(defensePasses[2 - ONE]));
                if (defensePasses[2 - ONE] == 0) {
                    defense2SubtractPass.setVisibility(View.INVISIBLE);
                }
            }
        });


        defense2AddPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[2 - ONE]++;
                defense2Passes.setText(Integer.toString(defensePasses[2 - ONE]));
                if (defensePasses[2 - ONE] > 0) {
                    defense2SubtractPass.setVisibility(View.VISIBLE);
                }
            }
        });

        defense3Type.setAdapter(defenseArrayAdapter);
        defense3SubtractPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[3 - ONE]--;
                defense3Passes.setText(Integer.toString(defensePasses[3 - ONE]));
                if (defensePasses[3 - ONE] == 0) {
                    defense3SubtractPass.setVisibility(View.INVISIBLE);
                }
            }
        });


        defense3AddPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[3 - ONE]++;
                defense3Passes.setText(Integer.toString(defensePasses[3 - ONE]));
                if (defensePasses[3 - ONE] > 0) {
                    defense3SubtractPass.setVisibility(View.VISIBLE);
                }
            }
        });

        defense4Type.setAdapter(defenseArrayAdapter);
        defense4SubtractPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[4 - ONE]--;
                defense4Passes.setText(Integer.toString(defensePasses[4 - ONE]));
                if (defensePasses[4 - ONE] == 0) {
                    defense4SubtractPass.setVisibility(View.INVISIBLE);
                }
            }
        });


        defense4AddPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[4 - ONE]++;
                defense4Passes.setText(Integer.toString(defensePasses[4 - ONE]));
                if (defensePasses[4 - ONE] > 0) {
                    defense4SubtractPass.setVisibility(View.VISIBLE);
                }
            }
        });

        defense5Type.setAdapter(defenseArrayAdapter);
        defense5SubtractPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[5 - ONE]--;
                defense5Passes.setText(Integer.toString(defensePasses[5 - ONE]));
                if (defensePasses[5 - ONE] == 0) {
                    defense5SubtractPass.setVisibility(View.INVISIBLE);
                }
            }
        });


        defense5AddPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses[5 - ONE]++;
                defense5Passes.setText(Integer.toString(defensePasses[5 - ONE]));
                if (defensePasses[5 - ONE] > 0) {
                    defense5SubtractPass.setVisibility(View.VISIBLE);
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
        CheckBox capture = (CheckBox) view.findViewById(R.id.can_capture);
        CheckBox climb = (CheckBox) view.findViewById(R.id.can_climb);
        Spinner defense1Type = (Spinner) view.findViewById(R.id.defense_1_type);
        Spinner defense2Type = (Spinner) view.findViewById(R.id.defense_2_type);
        Spinner defense3Type = (Spinner) view.findViewById(R.id.defense_3_type);
        Spinner defense4Type = (Spinner) view.findViewById(R.id.defense_4_type);
        Spinner defense5Type = (Spinner) view.findViewById(R.id.defense_5_type);

        if (roundNumber.getText().toString().equals("") || teamNumber.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Fill out the team and round number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (defense2Type.getSelectedItem().toString().equals("Defense crossed?")) {
            Toast.makeText(getContext(), "Fill out all defenses, 'defense crossed?' is not a defense", Toast.LENGTH_SHORT).show();
            return;
        }

        if (defense3Type.getSelectedItem().toString().equals("Defense crossed?")) {
            Toast.makeText(getContext(), "Fill out all defenses, 'defense crossed?' is not a defense", Toast.LENGTH_SHORT).show();
            return;
        }

        if (defense4Type.getSelectedItem().toString().equals("Defense crossed?")) {
            Toast.makeText(getContext(), "Fill out all defenses, 'defense crossed?' is not a defense", Toast.LENGTH_SHORT).show();
            return;
        }

        if (defense5Type.getSelectedItem().toString().equals("Defense crossed?")) {
            Toast.makeText(getContext(), "Fill out all defenses, 'defense crossed?' is not a defense", Toast.LENGTH_SHORT).show();
            return;
        }



        try {
            String fileName = teamNumber.getText().toString();
            File file = new File("/storage/emulated/0/scouting/" + fileName + ".csv");
            new File("/storage/emulated/0/scouting/").mkdir();


            if (!file.exists()) {
                file.createNewFile();
                Toast.makeText(getContext(), "rise my son." + fileName, Toast.LENGTH_SHORT).show();
            }

            writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));

            write(roundNumber.getText().toString());
            write(Boolean.toString(canAuton.isChecked()));
            write(autonDefense.getSelectedItem().toString());
            write(Boolean.toString(canAutonShoot.isChecked()));
            if (canAutonShoot.isChecked()) {
                RadioButton shootType = (RadioButton) view.findViewById(autonShootType.getCheckedRadioButtonId());
                write(shootType.getText().toString());
            } else {
                write("null");
            }
            write(defense1Type.getSelectedItem().toString());
            write(Integer.toString(defensePasses[1 - ONE]));
            write(defense2Type.getSelectedItem().toString());
            write(Integer.toString(defensePasses[2 - ONE]));
            write(defense3Type.getSelectedItem().toString());
            write(Integer.toString(defensePasses[3 - ONE]));
            write(defense4Type.getSelectedItem().toString());
            write(Integer.toString(defensePasses[4 - ONE]));
            write(defense5Type.getSelectedItem().toString());
            write(Integer.toString(defensePasses[5 - ONE]));
            if ((highGoalMadeCount + highGoalMissedCount) > 0) {
                write(Double.toString((double) highGoalMadeCount / (highGoalMadeCount + highGoalMissedCount)));
            } else {
                write("0.0");
            }
            if ((lowGoalMadeCount + lowGoalMissedCount) > 0) {
                write(Double.toString((double) lowGoalMadeCount / (lowGoalMadeCount + lowGoalMissedCount)));
            } else {
                write("0.0");
            }
            write(Boolean.toString(capture.isChecked()));
            writer.write(Boolean.toString(climb.isChecked()));
            writer.newLine();
            writer.flush();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "IO error", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getContext(), "You have submitted your form to the scouting god.", Toast.LENGTH_SHORT).show();
    }




    public void alert() {
        Toast toast = Toast.makeText(getContext(), "You did a thing", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void write(String data) {
        try {
            writer.write(data + ",");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "IO error", Toast.LENGTH_SHORT).show();
        }
    }


}
