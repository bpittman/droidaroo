package com.bpittman.droidaroo;

import java.io.IOException;

import android.app.ListActivity;
import android.database.sqlite.SQLiteException;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;


public class BandsList extends ListActivity {

	private DataBaseHelper myDbHelper;

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bands);
		myDbHelper = new DataBaseHelper(this);

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
        // Get all of the bands from the database and create the item list
        Cursor c = myDbHelper.getBands();
        startManagingCursor(c);

        String[] from = new String[] { "line1" };
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter bands =
            new SimpleCursorAdapter(this, R.layout.bands_row, c, from, to);
        setListAdapter(bands);
    }
}
