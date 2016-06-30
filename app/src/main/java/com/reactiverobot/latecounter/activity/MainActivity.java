package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterTypes;

import roboguice.activity.RoboActionBarActivity;


public class MainActivity extends RoboActionBarActivity {

    private static final String TAG = "LateCounter-Activity";

    @Inject
    CounterTypes counterTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        counterTypes.createSafely("test1");
        counterTypes.createSafely("test1");
        counterTypes.createSafely("test2");
        counterTypes.createSafely("test3");
        counterTypes.createSafely("test4");
        counterTypes.createSafely("test5");
        counterTypes.createSafely("test6");
        counterTypes.createSafely("test9");
        counterTypes.createSafely("test7");
        counterTypes.createSafely("test8");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
