package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
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

        final int appWidgetId = getIntent().getIntExtra(WIDGET_ID_EXTRA, -1);
        // TODO: add check handling start activity without widgetId.

        final List<CounterType> counterTypesWithNoWidget = counterTypes.loadTypesWithNoWidget();

        ListView listView = new ListView(this);
        listView.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                // Add a header for a title and a footer to create a new type.
                return counterTypesWithNoWidget.size() + 2;
            }

            @Override
            public CounterType getItem(int position) {
                int listPosition = position - 1; // One header item as title.
                if (listPosition >= 0 && listPosition < counterTypesWithNoWidget.size()) {
                    return counterTypesWithNoWidget.get(position - 1);
                } else {
                    throw new IllegalArgumentException("Attempting to retrieve item outside "
                            + "valid range. position: " + position
                            + ", num counter types: " + counterTypesWithNoWidget.size());
                }
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (position == 0) {
                    // Header for list title.
                    View counterTypeView = LayoutInflater
                            .from(PickCounterTypeActivity.this)
                            .inflate(R.layout.counter_type_list_item, null);
                    return counterTypeView;
                } else if (position == counterTypesWithNoWidget.size() + 1) {
                    // Footer to create new type.
                    View counterTypeView = LayoutInflater
                            .from(PickCounterTypeActivity.this)
                            .inflate(R.layout.counter_type_list_item, null);
                    return counterTypeView;
                } else {
                    final CounterType type = getItem(position);

                    View counterTypeView = LayoutInflater
                            .from(PickCounterTypeActivity.this)
                            .inflate(R.layout.counter_type_list_item, null);

                    counterTypeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            counterTypes.createSafelyWithWidgetId(type.getDescription(), appWidgetId);
                            finish();
                        }
                    });

                    TextView counterTypeName = (TextView) counterTypeView
                            .findViewById(R.id.counter_type_list_item_name);

                    counterTypeName.setText(type.getDescription());
                    return counterTypeView;
                }
            }
        });
//        new ArrayAdapter<>(this, R.layout.counter_type_list_item, ));

        setContentView(listView);
    }
}
