package com.reactiverobot.latecounter.plot;

import android.view.View;

import com.reactiverobot.latecounter.model.CounterRecord;
import com.reactiverobot.latecounter.model.CounterType;

import java.util.List;

public interface PlotProvider {
    View getPlot(List<CounterRecord> counterRecords);
    View getPlot(CounterType counterType);
}
