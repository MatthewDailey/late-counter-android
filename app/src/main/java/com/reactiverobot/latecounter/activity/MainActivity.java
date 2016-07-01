package com.reactiverobot.latecounter.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;

import java.util.List;

import roboguice.activity.RoboActionBarActivity;


public class MainActivity extends RoboActionBarActivity {

    private static final String TAG = "LateCounter-Activity";

    @Inject CounterTypes counterTypes;
    @Inject CounterRecords counterRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<CounterType> counterTypeList = counterTypes.loadAllTypes();

        ListView listView = (ListView) findViewById(R.id.main_counter_type_list);

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return counterTypeList.size();
            }

            @Override
            public CounterType getItem(int position) {
                return counterTypeList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return getViewForCounterType(getItem(position));
            }
        });

    }

    private View getViewForCounterType(final CounterType counterType) {
        View counterTypeView = LayoutInflater.from(this).inflate(R.layout.counter_type_list_item, null);

        TextView counterTypeDescriptionView = (TextView) counterTypeView.findViewById(R.id.main_counter_type_name);
        counterTypeDescriptionView.setText(counterType.getDescription());

        ImageButton deleteTypeButton = (ImageButton) counterTypeView.findViewById(R.id.main_delete_button);
        deleteTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure you want to delete the '"
                                + counterType.getDescription() + "' counter?")
                        .setPositiveButton("Yes.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                counterTypes.deleteWithDescription(counterType.getDescription());
                                counterRecords.deleteType(counterType);
                            }
                        })
                        .setNegativeButton("Cancel.", null)
                        .show();
            }
        });

        ImageButton graphTypeButton = (ImageButton) counterTypeView.findViewById(R.id.main_graph_button);
        graphTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO pass counter type for graphing.
                startActivity(new Intent(MainActivity.this, GraphActivity.class));
            }
        });

        return counterTypeView;
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
