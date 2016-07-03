package com.reactiverobot.latecounter.plot;

import com.google.inject.AbstractModule;

public class PlotModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PlotProvider.class).to(PlotProviderMPAndroidChartImpl.class);
    }
}
