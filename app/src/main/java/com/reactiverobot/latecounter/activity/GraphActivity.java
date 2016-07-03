package com.reactiverobot.latecounter.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;

import org.roboguice.shaded.goole.common.base.Function;
import org.roboguice.shaded.goole.common.base.Optional;
import org.roboguice.shaded.goole.common.collect.Iterables;
import org.roboguice.shaded.goole.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import roboguice.activity.RoboActionBarActivity;

public class GraphActivity extends RoboActionBarActivity {

    private final static String TAG = "GraphActivity";
    public final static String COUNTER_TYPE_TO_GRAPH_EXTRA = "counter_type_to_graph";

    @Inject CounterTypes counterTypes;
    @Inject CounterRecords counterRecords;

    @NonNull // after onCreate succeeds.
    private CounterType counterType;
    private boolean showingRealData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String counterTypeToGraph = getIntent().getStringExtra(COUNTER_TYPE_TO_GRAPH_EXTRA);
        Optional<CounterType> counterTypeOption = counterTypes.getType(counterTypeToGraph);

        if (counterTypeOption.isPresent()) {
            setTitle(counterTypeToGraph);
            counterType = counterTypeOption.get();
            setChartFromRealData();
        } else {
            Toast.makeText(this,
                    "There is no data for the type '" + counterTypeToGraph + "'.",
                    Toast.LENGTH_SHORT)
                .show();
            finish();
        }

    }

    private void setChartFromRealData() {
        setContentView(getBarChart(counterRecords.loadAllForTypeOrderedByDate(counterType)));
    }

    @NonNull
    private BarChart getBarChart(List<CounterRecord> records) {
        int barEntryIndex = 0;
        List<BarEntry> barEntries = Lists.newArrayList();
        for (CounterRecord record : records) {
            barEntries.add(new BarEntry(record.getCount(), barEntryIndex++, record.getDate()));
        }

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d yyyy");
        List<String> dates = Lists.newArrayList(Iterables.transform(records,
                new Function<CounterRecord, String>() {
                    @Override
                    public String apply(CounterRecord counterRecord) {
                        return dateFormat.format(counterRecord.getDate());
                    }
                }));

        // Pass empty string for description
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setColor(getResources().getColor(R.color.green));

        BarData barData = new BarData(dates, barDataSet);

        BarChart barChart = new BarChart(this);

        barChart.getLegend().setEnabled(false);

        // Hide the right y-axis.
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawGridLines(false);

        // Pin min Y-val to 0.
        barChart.getAxisLeft().setAxisMinValue(0);

        barChart.setBackgroundColor(getResources().getColor(R.color.beige));

        // Hide the description in bottom-right of chart.
        barChart.setDescription("");

        // Set data and tell chart to draw.
        barChart.setData(barData);
        barChart.invalidate();


        if (records.size() > 7) {
            float numDesiredVisible = 7;
            float percentDesiredVisible = (records.size() / numDesiredVisible);
            barChart.zoomAndCenterAnimated(percentDesiredVisible,
                    1f,
                    records.size(),
                    barChart.getY(),
                    YAxis.AxisDependency.RIGHT,
                    1000);
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        return barChart;
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
            synchronized (this) {
                if (showingRealData) {
                    setTitle(counterType.getDescription() + " - Sample Data");
                    setContentView(getBarChart(getSampleCounterRecords()));
                    showingRealData = false;
                } else {
                    setTitle(counterType.getDescription());
                    setChartFromRealData();
                    showingRealData = true;
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
