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


public class ScoutingSheet extends Fragment {





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
                submitForm();
            }
        });


        roundNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    roundNumber.clearFocus();
                }

                return false;
            }
        });

        teamNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    teamNumber.clearFocus();
                }

                return false;
            }
        });

        final Spinner autonDefenseSpinner = (Spinner) view.findViewById(R.id.auton_defense_cross);
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
            defenseList.addView(new Defense(getContext()));
        }



        return view;
    }




    public void submitForm() {

        EditText roundNumber = (EditText)getView().findViewById(R.id.round_number);
        EditText teamNumber = (EditText) getView().findViewById(R.id.team_number);
        CheckBox canAuton = (CheckBox) getView().findViewById(R.id.can_auton);



    }

    public void writeData(View view) {

    }


    public void alert() {
        Toast toast = Toast.makeText(getContext(), "You did a thing", Toast.LENGTH_SHORT);
        toast.show();
    }




}
