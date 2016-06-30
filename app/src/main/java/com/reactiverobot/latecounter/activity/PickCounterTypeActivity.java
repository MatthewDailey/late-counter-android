package com.reactiverobot.latecounter.activity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.reactiverobot.latecounter.widget.GenericCounterWidget;

import java.util.List;

import roboguice.activity.RoboActivity;

public class PickCounterTypeActivity extends RoboActivity {

    public static Intent getStartIntent(Context context, int widgetId) {
        Intent launchPickTypeActivity = new Intent(context, PickCounterTypeActivity.class);
        launchPickTypeActivity.putExtra(PickCounterTypeActivity.WIDGET_ID_EXTRA, widgetId);
        return launchPickTypeActivity;
    }

    public static final int CREATE_COUNTER_TYPE_REQUEST_CODE = 12;
    public final static String WIDGET_ID_EXTRA = "AppWidgetId";

    @Inject CounterTypes counterTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int appWidgetId = getIntent().getIntExtra(WIDGET_ID_EXTRA, -1);
        // TODO: add check handling start activity without widgetId.

        final List<CounterType> counterTypesWithNoWidget = counterTypes.loadTypesWithNoWidget();

        setContentView(R.layout.counter_type_list);

        ListView listView = (ListView) findViewById(R.id.counter_type_list_view);
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
                    counterTypeView.setClickable(false);
                    counterTypeView.setOnClickListener(null);

                    TextView titleView = (TextView) counterTypeView
                            .findViewById(R.id.counter_type_list_item_name);
                    titleView.setText("Pick a counter:");
                    titleView.setTypeface(null, Typeface.BOLD);

                    return counterTypeView;
                } else if (position == counterTypesWithNoWidget.size() + 1) {
                    // Footer to create new type.
                    View counterTypeView = LayoutInflater
                            .from(PickCounterTypeActivity.this)
                            .inflate(R.layout.counter_type_list_item, null);

                    counterTypeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startCreateCounterTypeActivityForResult();
                        }
                    });

                    TextView titleView = (TextView) counterTypeView
                            .findViewById(R.id.counter_type_list_item_name);
                    titleView.setText("Add a new counter.");
                    titleView.setTypeface(null, Typeface.ITALIC);

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
                            broadcastUpdateWidget(appWidgetId);
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
    }

    private void broadcastUpdateWidget(int appWidgetId) {
        Intent intent = new Intent(this, GenericCounterWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        sendBroadcast(intent);
    }

    private void startCreateCounterTypeActivityForResult() {
        Intent intent = new Intent(this, CreateCounterTypeActivity.class);
        startActivityForResult(intent, CREATE_COUNTER_TYPE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_COUNTER_TYPE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Successfully created new counter type associated with the widget.
                finish();
            }
        }
    }
}
