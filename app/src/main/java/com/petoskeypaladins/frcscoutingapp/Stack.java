package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Stack extends RelativeLayout {


    public Stack(Context context, int stackCount) {
        super(context);

        int stackId;
        String stackIdPointer;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.view_stack, this, true);

        TextView stackName = (TextView)getRootView().findViewById(R.id.stack_label);
        stackName.setText("Stack " + stackCount);

        stackIdPointer = "R.id.stack_".concat(Integer.toString(stackCount));

        stackId = getResources().getIdentifier(stackIdPointer, "Stack", "com.petoskeypaladins.recyclerushscouting");
        this.setId(stackId);
    }



}
