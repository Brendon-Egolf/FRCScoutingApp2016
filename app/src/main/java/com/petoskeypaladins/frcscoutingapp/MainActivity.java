package com.petoskeypaladins.frcscoutingapp;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import java.io.IOException;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appBarLayout;
    private String tabletName;
    private ColorDrawable toolbarColor;
    static private ViewPagerAdapter adapter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String RED = "#e51c23", BLUE = "#1c23e5";
        //#YOLOLOLOLOL
        final String ONE_SHADE_OF_GREY = "#696969";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabletName = BluetoothAdapter.getDefaultAdapter().getName();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        appBarLayout = (AppBarLayout) findViewById(R.id.coordinator);
        try {
            if ((Integer.parseInt(tabletName.substring(tabletName.length() - 1)) / 4) == 0){
                toolbarColor = new ColorDrawable(Color.parseColor(RED));
                toolbar.setBackground(toolbarColor);
                tabLayout.setBackground(toolbarColor);
                appBarLayout.setBackground(toolbarColor);
            } else {
                toolbarColor = new ColorDrawable(Color.parseColor(BLUE));
                toolbar.setBackground(toolbarColor);
                tabLayout.setBackground(toolbarColor);
                appBarLayout.setBackground(toolbarColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            toolbarColor = new ColorDrawable(Color.parseColor(ONE_SHADE_OF_GREY));
            toolbar.setBackground(toolbarColor);
            tabLayout.setBackground(toolbarColor);
            appBarLayout.setBackground(toolbarColor);
        }
        toolbar.setTitle(tabletName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ScoutingSheet(), "Scout");
        adapter.addFragment(new DataView(), "View Data");
        adapter.addFragment(new SelectionLists(), "Lists");
        adapter.addFragment(new SyncData(), "Sync Data");
        viewPager.setAdapter(adapter);
    }

    public static android.support.v4.app.Fragment getTabFragment(int position) {
        return adapter.getItem(position);
    }

    public static android.support.v4.app.Fragment getTabFragment(String title) {
        int i;
        for (i = 0; i < adapter.getCount(); i++) {
            if (adapter.getPageTitle(i).equals(title))
                break;
        }
        return adapter.getItem(i);
    }
}