package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class TeamPositionView extends ViewGroup {
    TextView label;
    EditText value;
    String position;

    public TeamPositionView(Context context, String position) {
        super(context);
        init(context, null);
        this.position = position;
    }

    public TeamPositionView(Context context, AttributeSet attributeSet) {
        super(context);
        init(context, attributeSet);
    }

    public TeamPositionView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context);
        init(context, attributeSet);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.team_position_view, null);

        label = (TextView) view.findViewById(R.id.label);
        value = (EditText) view.findViewById(R.id.value);

        label.setText(position + ":");
    }

    public String getValue() {
        return value.getText().toString();
    }
}
