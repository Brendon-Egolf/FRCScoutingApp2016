package com.petoskeypaladins.frcscoutingapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TeamData extends AppCompatActivity {


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String teamName = intent.getStringExtra("team-name");
        String teamNumber = intent.getStringExtra("team-number");
        String[] teamStats = intent.getStringArrayExtra("team-stats");
        String[] statKeys = getResources().getStringArray(R.array.stat_keys);

        setContentView(R.layout.activity_team_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Team " + teamNumber + ", " + teamName);
        setSupportActionBar(toolbar);

        ImageView teamPicture = (ImageView) findViewById(R.id.team_picture);

        teamPicture.setImageURI(Uri.parse("/storage/emulated/0/scouting/" + teamNumber + ".jpg"));

        LinearLayout teamStatsView = (LinearLayout) findViewById(R.id.team_stats);

        if (teamStats[0].equals("no-data")) {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
            TextView error = new TextView(this);
            error.setText("No data on this team");
            error.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            teamStatsView.addView(error);
        } else {
            int j = 0;

            for (int i = 0; i < statKeys.length; i++, j++) {
                LinearLayout.LayoutParams layoutParamsUntabbed = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                if (statKeys[i].contains("defenses")) {
                    TextView defenseView = new TextView(this);
                    defenseView.setText(statKeys[i] + ": ");
                    defenseView.setLayoutParams(layoutParamsUntabbed);
                    defenseView.setTextColor(getResources().getColor(R.color.black));
                    teamStatsView.addView(defenseView);
                    String[] defenses = getResources().getStringArray(R.array.defenses);
                    int difference = j - i;
                    for (; j - i - difference < defenses.length; j++) {
                        LinearLayout.LayoutParams layoutParamsTabbed = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView data = new TextView(this);
                        data.setTextColor(getResources().getColor(R.color.black)); 
                        data.setText(defenses[j - i - difference] + ": " + teamStats[j]);
                        layoutParamsTabbed.setMarginStart(16);
                        data.setLayoutParams(layoutParamsTabbed);
                        teamStatsView.addView(data);
                    }
                    j--;
                } else {
                    TextView data = new TextView(this);
                    data.setTextColor(getResources().getColor(R.color.black));
                    data.setText(statKeys[i] + ": " + teamStats[j]);
                    data.setLayoutParams(layoutParamsUntabbed);
                    teamStatsView.addView(data);
                }
            }
        }
    }
}
