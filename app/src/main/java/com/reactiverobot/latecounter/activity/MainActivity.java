package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.reactiverobot.latecounter.model.CounterDailyRecord;
import com.reactiverobot.latecounter.R;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "LateCounter-Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("basicRalmConfig")
                .schemaVersion(1)
                .build();

        Realm realmDb = Realm.getInstance(config);

        realmDb.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CounterDailyRecord dailyRecord = realm.createObject(CounterDailyRecord.class);
                dailyRecord.setCount(1);
                dailyRecord.setDate(new Date());
                dailyRecord.setCounterType("cool-count");
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Successfully stored daily record");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "Error storing record! " + error);
            }
        });

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
