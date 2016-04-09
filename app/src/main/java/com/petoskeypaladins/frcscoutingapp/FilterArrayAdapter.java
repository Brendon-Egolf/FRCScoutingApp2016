package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;


/**
 * Created by begolf on 2/25/16.
 */
public class FilterArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> filterList;
    private ArrayList<Double> filterValues;

    public FilterArrayAdapter(Context context, int resource, int resourceId, ArrayList<String> filterList) {
        super(context, resource, resourceId, filterList);
        this.filterList = filterList;
        filterValues = new ArrayList<>(getCount());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_view, parent, false);
        TextView filterType = (TextView) view.findViewById(R.id.filter_type);
        EditText filterValue = (EditText) view.findViewById(R.id.filter_value);

        filterType.setText(filterList.get(position));

        filterValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    filterValues.set(position, Double.parseDouble(s.toString()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    filterValues.set(position, -1.);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    public double getValue(int position) {
        return filterValues.get(position);
    }
}
