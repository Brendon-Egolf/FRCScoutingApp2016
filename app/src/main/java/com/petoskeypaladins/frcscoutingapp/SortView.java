package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.petoskeypaladins.frcscoutingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class SortView extends ViewGroup {


    public SortView(Context context) {
        super(context);
        init(context, null);
    }

    public SortView(Context context, AttributeSet attributeSet) {
        super(context);
        init(context, attributeSet);
    }

    public SortView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context);
        init(context, attributeSet);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void init(Context context, AttributeSet attributeSet) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sort_view, null);


    }
}
