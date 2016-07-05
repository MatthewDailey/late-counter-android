package com.reactiverobot.latecounter.activity;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;
import com.reactiverobot.latecounter.plot.PlotProvider;
import com.reactiverobot.latecounter.widget.GenericCounterWidget;

import java.util.List;

import roboguice.activity.RoboActionBarActivity;


public class MainActivity extends RoboActionBarActivity {

    private static final String TAG = "LateCounter-Activity";

    @Inject CounterTypes counterTypes;
    @Inject CounterRecords counterRecords;
    @Inject PlotProvider plotProvider;

    private ArrayAdapter<CounterType> counterTypeArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final List<CounterType> counterTypeList = counterTypes.loadAllTypes();

        ListView listView = (ListView) findViewById(R.id.main_counter_type_list);

        counterTypeArrayAdapter = new ArrayAdapter<CounterType>(
                this,
                R.layout.main_counter_type_list_item,
                counterTypeList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return getViewForCounterType(getItem(position));
            }
        };

        listView.setAdapter(counterTypeArrayAdapter);
    }

    private View getViewForCounterType(final CounterType counterType) {
        ViewGroup counterTypeView = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.main_counter_type_list_item, null);

        TextView counterTypeDescriptionView = (TextView) counterTypeView.findViewById(R.id.main_counter_type_name);
        counterTypeDescriptionView.setText(counterType.getDescription());

        ImageButton deleteTypeButton = (ImageButton) counterTypeView.findViewById(R.id.main_delete_button);
        deleteTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure you want to delete the '"
                                + counterType.getDescription()
                                + "' counter and all associated data?")
                        .setPositiveButton("Yes, delete it.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCounterType(counterType);
                            }
                        })
                        .setNegativeButton("No.", null)
                        .show();
            }
        });

        ImageButton graphTypeButton = (ImageButton) counterTypeView.findViewById(R.id.main_graph_button);
        graphTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent graphIntent = new Intent(MainActivity.this, GraphActivity.class);
                graphIntent.putExtra(GraphActivity.COUNTER_TYPE_TO_GRAPH_EXTRA, counterType.getDescription());
                startActivity(graphIntent);
            }
        });

        View plot = plotProvider.getPlot(counterType);
        plot.setMinimumHeight(500);
        counterTypeView.addView(plot);

        counterTypeView.setPadding(10, 10, 10, 60);
        return counterTypeView;
    }

    private void deleteCounterType(CounterType counterType) {
        counterTypes.deleteWithDescription(counterType.getDescription());
        counterRecords.deleteType(counterType);
        broadcastUpdateWidget(counterType.getWidgetId());
        counterTypeArrayAdapter.remove(counterType);
    }

    // TODO: Duplicated from {@link PickCounterTypeActivity}
    private void broadcastUpdateWidget(int appWidgetId) {
        Intent intent = new Intent(this, GenericCounterWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        sendBroadcast(intent);
    }

}
