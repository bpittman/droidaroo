package com.bpittman.droidaroo;

import android.app.Activity;
import android.widget.Button;
import android.view.View;
import android.os.Bundle;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import java.io.IOException;

public class Droidaroo extends Activity {
    private View.OnClickListener mBandsListener = new View.OnClickListener() {
        public void onClick(View v) {
        	Intent myIntent = new Intent(Droidaroo.this, BandsList.class);
        	startActivity(myIntent);
        }
    };
    private View.OnClickListener mStagesListener = new View.OnClickListener() {
        public void onClick(View v) {
        	Intent myIntent = new Intent(Droidaroo.this, StagesList.class);
        	startActivity(myIntent);
        }
    };
    private View.OnClickListener mStarredListener = new View.OnClickListener() {
        public void onClick(View v) {
        	Intent myIntent = new Intent(Droidaroo.this, StarredList.class);
        	startActivity(myIntent);
        }
    };
    private View.OnClickListener mMapListener = new View.OnClickListener() {
        public void onClick(View v) {
        	Intent myIntent = new Intent(Droidaroo.this, Map.class);
        	startActivity(myIntent);
        }
    };
    private View.OnClickListener mTodayListener = new View.OnClickListener() {
        public void onClick(View v) {
        	Intent myIntent = new Intent(Droidaroo.this, TodayList.class);
        	startActivity(myIntent);
        }
    };
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button bandsButton = (Button)findViewById(R.id.bands);
        bandsButton.setOnClickListener(mBandsListener);
        Button stagesButton = (Button)findViewById(R.id.stages);
        stagesButton.setOnClickListener(mStagesListener);
        Button starredButton = (Button)findViewById(R.id.starred);
        starredButton.setOnClickListener(mStarredListener);
        Button mapButton = (Button)findViewById(R.id.map);
        mapButton.setOnClickListener(mMapListener);
        Button todayButton = (Button)findViewById(R.id.today);
        todayButton.setOnClickListener(mTodayListener);

        DataBaseHelper myDbHelper = new DataBaseHelper(this);
 
        try {
        	myDbHelper.createDataBase();
        } catch (IOException ioe) {
        	throw new Error("Unable to create database");
        }
        try {
        	myDbHelper.openDataBase();
        }catch(SQLiteException sqle){
        	throw sqle;
        }
    }
}
