package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class Defense extends ViewGroup {
    private int defensePasses;

    public Defense(Context context) {
        super(context);
        init(context, null);
    }

    public Defense(Context context, AttributeSet attributeSet) {
        super(context);
        init(context, attributeSet);
    }

    public Defense(Context context, AttributeSet attributeSet, int defStyle) {
        super(context);
        init(context, attributeSet);
    }

    public void init(Context context, AttributeSet attributeSet) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_defense, null);

        final Spinner defenseSpinner = (Spinner) view.findViewById(R.id.defense_type);
        final Button subtractPass = (Button) view.findViewById(R.id.subtract_pass);
        final TextView passes = (TextView) view.findViewById(R.id.passes);
        final Button addPass = (Button) view.findViewById(R.id.add_pass);

        TypedArray attributes = context.obtainStyledAttributes(R.styleable.Defense);
        boolean lowBar = Boolean.parseBoolean(attributes.getString(R.styleable.Defense_isLowBar));



        ArrayAdapter<CharSequence> arrayAdapter;
        defensePasses = 0;
        updatePasses(passes, defensePasses);
        if (lowBar) {
            arrayAdapter = ArrayAdapter.createFromResource(context,
                    R.array.low_bar, android.R.layout.simple_spinner_dropdown_item);
        } else {
            arrayAdapter = ArrayAdapter.createFromResource(context,
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
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
