package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class ScoutingSheet extends Fragment {

    private LinearLayout stackContainer;
    private LinearLayout.LayoutParams stackContainerParameters;
    private Context context;

    private int stackCount;


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


        final LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.stack_container);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final ScrollView scrollView = (ScrollView)view.findViewById(R.id.scroll_view);

        final EditText roundNumber = (EditText)view.findViewById(R.id.round_number);
        final EditText teamNumber = (EditText)view.findViewById(R.id.team_number);
        final Button submit = (Button)view.findViewById(R.id.submit);
        roundNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    teamNumber.requestFocus();
                }

                return false;
            }
        });

        final LinearLayout spotlight = (LinearLayout)view.findViewById(R.id.spotlight);

        teamNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    teamNumber.clearFocus();
                    spotlight.requestFocus();

                }

                return false;
            }
        });

        Button addStack = (Button) view.findViewById(R.id.add_stack);
        addStack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                stackCount++;
                Stack stack;
                stack = new Stack(getContext(), stackCount);
                linearLayout.addView(stack, layoutParams);

                scrollView.scrollTo(0, scrollView.getMaxScrollAmount() + 56);
            }
        });

        Button removeStack = (Button)view.findViewById(R.id.remove_stack);
        removeStack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (stackCount > 0) {
                    Stack lastStack = (Stack) linearLayout.getChildAt(stackCount - 1);
                    linearLayout.removeView(lastStack);
                    stackCount--;
                } else {
                    Toast.makeText(getContext(), "No stacks to remove", Toast.LENGTH_SHORT).show();
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });



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
