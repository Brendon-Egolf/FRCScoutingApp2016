package com.petoskeypaladins.frcscoutingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class FilterView extends View {


    public FilterView(Context context) {
        super(context);
        init(context, null);
    }

    public FilterView(Context context, AttributeSet attributeSet) {
        super(context);
        init(context, attributeSet);
    }

    public FilterView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context);
        init(context, attributeSet);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void init(Context context, AttributeSet attributeSet) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_view, null);


    }
}
