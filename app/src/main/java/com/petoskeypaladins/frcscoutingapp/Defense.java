package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;


public class Defense extends LinearLayout {


    public Defense(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_defense, this, true);
        View view = inflater.inflate(R.layout.layout_defense, this, true);

        view.findViewById(R.id.defense_type);


        final Spinner defenseSpinner = (Spinner) view.findViewById(R.id.defense_type);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.defenses, android.R.layout.simple_spinner_dropdown_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        defenseSpinner.setAdapter(arrayAdapter);





    }



}
