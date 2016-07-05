package com.reactiverobot.latecounter.plot;


import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.inject.Inject;
import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterRecords;
import com.reactiverobot.latecounter.model.CounterType;

import org.roboguice.shaded.goole.common.base.Function;
import org.roboguice.shaded.goole.common.collect.Iterables;
import org.roboguice.shaded.goole.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.List;

class PlotProviderMPAndroidChartImpl implements PlotProvider {

    private final Context context;
    private final CounterRecords counterRecords;

    @Inject
    PlotProviderMPAndroidChartImpl(Context context, CounterRecords counterRecords) {
        this.context = context;
        this.counterRecords = counterRecords;
    }

    @Override
    public View getPlot(List<CounterRecord> records, int colorId) {
        int barEntryIndex = 0;
        List<Entry> barEntries = Lists.newArrayList();
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

        // TODO: Make chart type configurable via prefs.
        // Pass empty string for description
        LineDataSet barDataSet = new LineDataSet(barEntries, "");
        int dataSetColor = context.getResources().getColor(colorId);
        barDataSet.setColor(dataSetColor);
        barDataSet.setCircleColor(dataSetColor);
        barDataSet.setCircleColorHole(dataSetColor);
        barDataSet.setLineWidth(5);
        barDataSet.setCircleRadius(7);
        barDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        barDataSet.setHighLightColor(dataSetColor);

        LineData barData = new LineData(dates, barDataSet);

        LineChart barChart = new LineChart(context);

        barChart.getLegend().setEnabled(false);

        // Hide y-axis.
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);

        // Pin min Y-val to 0.
        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.setScaleYEnabled(false);

//        barChart.setBackgroundColor(context.getResources().getColor(R.color.beige));

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
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setLabelRotationAngle(-90f);
        xAxis.setSpaceBetweenLabels(0);
//        xAxis.setAvoidFirstLastClipping(true);
        return barChart;
    }

    @Override
    public View getPlot(CounterType counterType) {
        return getPlot(counterRecords.loadAllForTypeOrderedByDate(counterType),
                counterType.getColorId());
    }
}
