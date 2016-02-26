package com.petoskeypaladins.frcscoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TeamData extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String teamName = intent.getStringExtra("team-name");
        String teamNumber = intent.getStringExtra("team-number");

        setContentView(R.layout.activity_team_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Team " + teamNumber + ", " + "teamName");
        setSupportActionBar(toolbar);


    }

}
