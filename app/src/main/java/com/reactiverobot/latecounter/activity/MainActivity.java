package com.reactiverobot.latecounter.activity;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.analytics.CounterzAnalytics;
import com.reactiverobot.latecounter.billing.BillingMachine;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;
import com.reactiverobot.latecounter.plot.PlotProvider;
import com.reactiverobot.latecounter.prefs.CounterzPrefs;
import com.reactiverobot.latecounter.widget.GenericCounterWidget;

import org.roboguice.shaded.goole.common.base.Optional;

import java.util.List;

import at.markushi.ui.CircleButton;
import roboguice.activity.RoboActionBarActivity;


public class MainActivity extends RoboActionBarActivity {

    private static final String TAG = "LateCounter-Activity";

    @Inject CounterTypes counterTypes;
    @Inject CounterRecords counterRecords;
    @Inject PlotProvider plotProvider;
    @Inject BillingMachine billingMachine;
    @Inject CounterzPrefs prefs;
    @Inject CounterzAnalytics analytics;

    private ArrayAdapter<CounterType> counterTypeArrayAdapter;

    private Optional<CounterType> counterTypeToUpdateColor = Optional.absent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analytics.synchronizeUserProperties();
        analytics.reportMainActivity();

        setContentView(R.layout.activity_main);

        checkIfPremiumEnabled();

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

        counterTypeArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                showEmptyLayoutIfNecessary();
            }
        });

        listView.setAdapter(counterTypeArrayAdapter);
    }

    private void checkIfPremiumEnabled() {
        if (billingMachine != null && !prefs.isPremiumEnabled()) {
            billingMachine.checkPurchasedPremium(new BillingMachine.CheckPurchaseHandler() {
                @Override
                public void handleResult(boolean isPurchased) {
                    if (isPurchased) {
                        prefs.enablePremium();
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        counterTypeArrayAdapter.notifyDataSetChanged();
    }

    private void showEmptyLayoutIfNecessary() {
        if (counterTypeArrayAdapter.isEmpty()) {
            findViewById(R.id.empty_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty_layout).setVisibility(View.GONE);
        }
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

        CircleButton changeColorButton = (CircleButton) counterTypeView.findViewById(R.id.change_color_button);
        changeColorButton.setColor(getResources().getColor(counterType.getColorId()));
        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterTypeToUpdateColor = Optional.of(counterType);
                startActivityForResult(
                        new Intent(MainActivity.this, PickCounterColorActivity.class),
                        PickCounterColorActivity.PICK_COLOR_REQUEST_CODE);
            }
        });

        View plot = plotProvider.getPlot(counterType);
        plot.setMinimumHeight(500);
        counterTypeView.addView(plot);

        counterTypeView.setPadding(10, 10, 10, 60);
        return counterTypeView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PickCounterColorActivity.PICK_COLOR_REQUEST_CODE) {
            if (resultCode == RESULT_OK && counterTypeToUpdateColor.isPresent()) {
                analytics.reportChangedCounterColor();

                int counterColorId = data.getIntExtra(PickCounterColorActivity.COLOR_ID_EXTRA,
                        counterTypeToUpdateColor.get().getColorId());

                int counterTypeListPosition =
                        counterTypeArrayAdapter.getPosition(counterTypeToUpdateColor.get());
                counterTypeArrayAdapter.getItem(counterTypeListPosition).setColorId(counterColorId);

                counterTypeArrayAdapter.notifyDataSetChanged();

                counterTypes.updateColorForType(counterTypeToUpdateColor.get().getDescription(), counterColorId);

                broadcastUpdateWidget(counterTypeToUpdateColor.get().getWidgetId());
            }
            counterTypeToUpdateColor = Optional.absent();
        }
    }

    private void deleteCounterType(CounterType counterType) {
        analytics.reportCounterDeleted();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
