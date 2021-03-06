package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.analytics.CounterzAnalytics;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;
import com.reactiverobot.latecounter.plot.PlotProvider;

import org.roboguice.shaded.goole.common.base.Optional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import roboguice.activity.RoboActionBarActivity;

public class GraphActivity extends RoboActionBarActivity {

    private final static String TAG = "GraphActivity";
    public final static String COUNTER_TYPE_TO_GRAPH_EXTRA = "counter_type_to_graph";

    @Inject CounterTypes counterTypes;
    @Inject PlotProvider plotProvider;
    @Inject CounterzAnalytics analytics;

    @NonNull // after onCreate succeeds.
    private CounterType counterType;
    private boolean showingRealData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analytics.reportGraphActivity();

        String counterTypeToGraph = getIntent().getStringExtra(COUNTER_TYPE_TO_GRAPH_EXTRA);
        Optional<CounterType> counterTypeOption = counterTypes.getType(counterTypeToGraph);

        if (counterTypeOption.isPresent()) {
            setTitle(counterTypeToGraph);
            counterType = counterTypeOption.get();
            setContentView(plotProvider.getPlot(counterType));
        } else {
            Toast.makeText(this,
                    "There is no data for the type '" + counterTypeToGraph + "'.",
                    Toast.LENGTH_SHORT)
                .show();
            finish();
        }

    }

    @NonNull
    private List<CounterRecord> getSampleCounterRecords() {
        List<CounterRecord> counterRecordsOrderedByDate = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int dateCount = 100;
        Random random = new Random();
        cal.add(Calendar.DATE, -dateCount);
        for (int dateNum = 0; dateNum < dateCount; dateNum++) {
            cal.add(Calendar.DATE, 1);
            counterRecordsOrderedByDate.add(CounterRecord.create(cal.getTime(), random.nextInt(10), counterType));
        }
        return counterRecordsOrderedByDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toggle_sample_data) {
            analytics.reportToggledSampleData();
            synchronized (this) {
                if (showingRealData) {
                    setTitle(counterType.getDescription() + " - Sample Data");
                    setContentView(
                            plotProvider.getPlot(
                                getSampleCounterRecords(),
                                counterType.getColorId()));
                    showingRealData = false;
                } else {
                    setTitle(counterType.getDescription());
                    setContentView(plotProvider.getPlot(counterType));
                    showingRealData = true;
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
