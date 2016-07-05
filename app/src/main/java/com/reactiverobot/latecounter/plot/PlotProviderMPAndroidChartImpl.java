package com.reactiverobot.latecounter.plot;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
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

    public static final int IDEAL_NUMBER_OF_VISIBLE_DATA_POINTS = 7;
    private final Context context;
    private final CounterRecords counterRecords;

    @Inject
    PlotProviderMPAndroidChartImpl(Context context, CounterRecords counterRecords) {
        this.context = context;
        this.counterRecords = counterRecords;
    }

    @Override
    public View getPlot(List<CounterRecord> records, int colorId) {
        int dataSetColor = context.getResources().getColor(colorId);

        // TODO: Make chart type configurable via prefs.
        // Pass empty string for description
        BarLineChartBase chart = getLineChart(records, dataSetColor);

        chart.getLegend().setEnabled(false);
        chart.setDescription("");

        setupChartZoom(records, chart);

        setupYAxis(chart);
        setupXAxis(chart);

        return chart;
    }

    @NonNull
    private BarLineChartBase getLineChart(List<CounterRecord> records, int dataSetColor) {
        LineDataSet dataSet = new LineDataSet(getYAxisValues(records), "");
        dataSet.setColor(dataSetColor);
        dataSet.setCircleColor(dataSetColor);
        dataSet.setCircleColorHole(dataSetColor);
        dataSet.setHighLightColor(dataSetColor);
        dataSet.setLineWidth(5);
        dataSet.setCircleRadius(7);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        BarLineChartBase chart = new LineChart(context);
        chart.setData(new LineData(getXAxisValues(records), dataSet));
        return chart;
    }

    @NonNull
    private List<String> getXAxisValues(List<CounterRecord> records) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d yyyy");
        return Lists.newArrayList(Iterables.transform(records,
                new Function<CounterRecord, String>() {
                    @Override
                    public String apply(CounterRecord counterRecord) {
                        return dateFormat.format(counterRecord.getDate());
                    }
                }));
    }

    @NonNull
    private List<Entry> getYAxisValues(List<CounterRecord> records) {
        int barEntryIndex = 0;
        List<Entry> barEntries = Lists.newArrayList();
        for (CounterRecord record : records) {
            barEntries.add(new BarEntry(record.getCount(), barEntryIndex++, record.getDate()));
        }
        return barEntries;
    }

    private void setupYAxis(BarLineChartBase chart) {
        // Hide y-axis.
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);

        // Pin min Y-val to 0.
        chart.getAxisLeft().setAxisMinValue(0);
        chart.setScaleYEnabled(false);
    }

    private void setupChartZoom(List<CounterRecord> records, BarLineChartBase chart) {
        if (records.size() > IDEAL_NUMBER_OF_VISIBLE_DATA_POINTS) {
            float percentDesiredVisible = (records.size() / IDEAL_NUMBER_OF_VISIBLE_DATA_POINTS);
            chart.zoomAndCenterAnimated(percentDesiredVisible,
                    1f,
                    records.size(),
                    chart.getY(),
                    YAxis.AxisDependency.RIGHT,
                    1000);
        }
    }

    private void setupXAxis(Chart chart) {
        XAxis xAxis = chart.getXAxis();

        // Hide x-axis gridlines.
        xAxis.setDrawGridLines(false);

        // Put the axis at bottom instead of top.
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Setup the x-axis labels.
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setLabelRotationAngle(-90f);
        xAxis.setSpaceBetweenLabels(0);
    }

    @Override
    public View getPlot(CounterType counterType) {
        return getPlot(counterRecords.loadAllForTypeOrderedByDate(counterType),
                counterType.getColorId());
    }
}
