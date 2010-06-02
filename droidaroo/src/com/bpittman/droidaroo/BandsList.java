package com.bpittman.droidaroo;

import java.io.IOException;

import android.app.ListActivity;
import android.database.sqlite.SQLiteException;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;


public class BandsList extends ListActivity {

	private DataBaseHelper myDbHelper;
	private Spinner daySpinner;
	private Spinner venueSpinner;
	private ArrayAdapter<String> daySpinnerAdapter;
	private ArrayAdapter<String> venueSpinnerAdapter;

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bands);
		myDbHelper = new DataBaseHelper(this);
		daySpinner = (Spinner)findViewById(R.id.daySpinner);
		venueSpinner = (Spinner)findViewById(R.id.venueSpinner);

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
		fillData();
	}

    private void fillData() {
        //fill the daySpinner
        String[] days = new String[] { "All Days", "Thursday", "Friday", "Saturday", "Sunday" };
        daySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);

        //fill the venueSpinner
        venueSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        venueSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        venueSpinner.setAdapter(venueSpinnerAdapter);
        venueSpinnerAdapter.add("Major Stages");
        venueSpinnerAdapter.add("All Stages");
        Cursor venuesCursor = myDbHelper.getVenues();
        int venueID = venuesCursor.getColumnIndexOrThrow("venueId");
        if (venuesCursor.getCount() > 0) {
            if (venuesCursor.moveToFirst()) {
                do {
                    venueSpinnerAdapter.add(venuesCursor.getString(venueID));
                } while(venuesCursor.moveToNext());
            }
        }

        // Get all of the bands from the database and create the item list
        Cursor c = myDbHelper.getBands();
        startManagingCursor(c);

        String[] from = new String[] { "line1", "venueId", "start" };
        int[] to = new int[] { R.id.text1, R.id.text2, R.id.text3 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter bands =
            new SimpleCursorAdapter(this, R.layout.bands_row, c, from, to);
        setListAdapter(bands);
    }
}
