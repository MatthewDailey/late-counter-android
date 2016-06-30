package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterTypes;

import roboguice.activity.RoboActivity;

public class PickCounterTypeActivity extends RoboActivity {

    @Inject CounterTypes counterTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = new ListView(this);
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.counter_type_list_item, counterTypes.loadTypesWithNoWidget()));

        setContentView(listView);
    }
}
