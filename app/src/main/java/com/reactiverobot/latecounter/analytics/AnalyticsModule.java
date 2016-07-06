package com.reactiverobot.latecounter.analytics;

import com.google.inject.AbstractModule;

public class AnalyticsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CounterzAnalytics.class).to(FirebaseCounterzAnalytics.class);
    }
}
