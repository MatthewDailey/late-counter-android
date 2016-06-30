package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;

import java.util.List;

import roboguice.activity.RoboActivity;

public class PickCounterTypeActivity extends RoboActivity {

    public final static String WIDGET_ID_EXTRA = "AppWidgetId";

    @Inject CounterTypes counterTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int appWidgetId = savedInstanceState.getInt(WIDGET_ID_EXTRA);

        final List<CounterType> counterTypesWithNoWidget = counterTypes.loadTypesWithNoWidget();

        ListView listView = new ListView(this);
        listView.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return counterTypesWithNoWidget.size();
            }

            @Override
            public CounterType getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                return null;
            }
        });
//        new ArrayAdapter<>(this, R.layout.counter_type_list_item, ));

        setContentView(listView);
    }
}
