package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


}
