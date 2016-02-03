package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class Defense extends LinearLayout {
    private int defensePasses;

    public Defense(Context context, Boolean lowBar) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_defense, this, true);
        View view = inflater.inflate(R.layout.layout_defense, this, true);

        final Spinner defenseSpinner = (Spinner) view.findViewById(R.id.defense_type);
        final Button subtractPass = (Button) view.findViewById(R.id.subtract_pass);
        final TextView passes = (TextView) view.findViewById(R.id.passes);
        final Button addPass = (Button) view.findViewById(R.id.add_pass);

        ArrayAdapter<CharSequence> arrayAdapter;
        defensePasses = 0;
        updatePasses(passes, defensePasses);
        if (lowBar) {
            arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.low_bar, android.R.layout.simple_spinner_dropdown_item);
        } else {
            arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.teleop_defenses, android.R.layout.simple_spinner_dropdown_item);
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defenseSpinner.setAdapter(arrayAdapter);

        subtractPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses--;
                updatePasses(passes, defensePasses);
                if (defensePasses == 0) {
                    subtractPass.setVisibility(View.INVISIBLE);
                }
            }
        });

        addPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defensePasses++;
                updatePasses(passes, defensePasses);
                if (defensePasses > 0) {
                    subtractPass.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void updatePasses(TextView passes, int defensePasses) {
        passes.setText(Integer.toString(defensePasses));
    }

    public String getDefense() {
        Spinner defense = (Spinner) getRootView().findViewById(R.id.defense_type);
        return (String) defense.getSelectedItem();
    }

    public int getDefensePasses() {
        return defensePasses;
    }

}
