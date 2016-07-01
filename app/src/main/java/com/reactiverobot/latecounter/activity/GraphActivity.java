package com.reactiverobot.latecounter.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.inject.Inject;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;
import com.reactiverobot.latecounter.model.CounterTypes;

import org.roboguice.shaded.goole.common.base.Function;
import org.roboguice.shaded.goole.common.collect.Iterables;
import org.roboguice.shaded.goole.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import roboguice.activity.RoboActionBarActivity;

public class GraphActivity extends RoboActionBarActivity {

    @Inject CounterTypes counterTypes;
    @Inject CounterRecords counterRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<CounterType> counterTypeList = counterTypes.loadAllTypes();

        if (!counterTypeList.isEmpty()) {
            CounterType counterType = counterTypeList.get(0);
            List<CounterRecord> counterRecordsOrderedByDate = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            int dateCount = 100;
            Random random = new Random();

            cal.add(Calendar.DATE, -dateCount);
            for (int dateNum = 0; dateNum < dateCount; dateNum++) {
                cal.add(Calendar.DATE, 1);
                counterRecordsOrderedByDate.add(CounterRecord.create(cal.getTime(), random.nextInt(10), counterType));
            }

            counterRecordsOrderedByDate.addAll(this.counterRecords.loadAllForTypeOrderedByDate(counterType));



            int barEntryIndex = 0;
            List<BarEntry> barEntries = Lists.newArrayList();
            for (CounterRecord record : counterRecordsOrderedByDate) {
                barEntries.add(new BarEntry(record.getCount(), barEntryIndex++, record.getDate()));
            }

            final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d yyyy");
            List<String> dates = Lists.newArrayList(Iterables.transform(counterRecordsOrderedByDate,
                    new Function<CounterRecord, String>() {
                        @Override
                        public String apply(CounterRecord counterRecord) {
                            return dateFormat.format(counterRecord.getDate());
                        }
                    }));

            BarDataSet barDataSet = new BarDataSet(barEntries, counterType.getDescription());
            BarData barData = new BarData(dates, barDataSet);
            BarChart barChart = new BarChart(this);
            barChart.setData(barData);
            barChart.invalidate();
            barChart.setDescription(counterType.getDescription());

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(10f);
            xAxis.setTextColor(Color.RED);

            setContentView(barChart);
        }
    }

}
