package com.reactiverobot.latecounter.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.inject.Inject;
import com.reactiverobot.latecounter.model.CounterTypes;
import com.reactiverobot.latecounter.prefs.CounterzPrefs;

class FirebaseCounterzAnalytics implements CounterzAnalytics {

    private final FirebaseAnalytics analytics;
    private final CounterTypes counterTypes;
    private final CounterzPrefs prefs;

    @Inject
    FirebaseCounterzAnalytics(Context context, CounterTypes counterTypes, CounterzPrefs prefs) {
        this.counterTypes = counterTypes;
        this.prefs = prefs;
        this.analytics = FirebaseAnalytics.getInstance(context);
        analytics.setAnalyticsCollectionEnabled(true);
    }

    @Override
    public void reportCounterCreated() {
        analytics.logEvent("counter_created", new Bundle());
    }

    @Override
    public void reportCreateCounterActivity() {
        analytics.logEvent("activity_counter_created", new Bundle());
    }

    @Override
    public void reportGraphActivity() {
        analytics.logEvent("activity_graph", new Bundle());
    }

    @Override
    public void reportToggledSampleData() {
        analytics.logEvent("toggled_sample_data", new Bundle());
    }

    @Override
    public void reportMainActivity() {
        analytics.logEvent("activity_main", new Bundle());
    }

    @Override
    public void reportCounterDeleted() {
        analytics.logEvent("counter_deleted", new Bundle());
    }

    @Override
    public void reportChangedCounterColor() {
        analytics.logEvent("counter_change_color", new Bundle());
    }

    @Override
    public void reportPickCounterColorActivity() {
        analytics.logEvent("activity_pick_counter_color", new Bundle());
    }

    @Override
    public void reportPickCounterTypeActivity() {
        analytics.logEvent("activity_pick_counter", new Bundle());
    }

    @Override
    public void reportReachedCounterLimitActivity() {
        analytics.setUserProperty("has_opened_upgrade", "true");
        analytics.logEvent("activity_reached_counter_limit", new Bundle());
    }

    @Override
    public void reportWantedPremiumActivity() {
        analytics.setUserProperty("has_opened_upgrade", "true");
        analytics.logEvent("activity_wants_premium", new Bundle());
    }

    @Override
    public void reportClickedBuyPremium() {
        analytics.logEvent("premium_clicked_buy", new Bundle());
    }

    @Override
    public void reportClickedCancelBuyingPremium() {
        analytics.logEvent("premium_clicked_canel", new Bundle());
    }

    @Override
    public void reportCorrectPremiumPassword() {
        analytics.logEvent("premium_entered_password", new Bundle());
    }

    @Override
    public void reportPurchasedPremium() {
        analytics.logEvent("premium_purchased", new Bundle());
    }

    @Override
    public void reportPreviouslyPurchasedPremium() {
        analytics.logEvent("premium_previously_purchased", new Bundle());
    }

    @Override
    public void reportAttemptedPremiumPassword() {
        analytics.logEvent("premium_attempted_password", new Bundle());
    }

    @Override
    public void reportEnabledPremium() {
        analytics.logEvent("premium_endabled", new Bundle());
    }

    @Override
    public void reportSettingsActivity() {
        analytics.logEvent("activity_settings", new Bundle());
    }

    @Override
    public void reportToggledUseBarChart() {
        analytics.logEvent("toggled_use_barchart", new Bundle());
    }

    @Override
    public void synchronizeUserProperties() {
        analytics.setUserProperty("num_widgets",
                String.valueOf(counterTypes.loadAllTypes().size()));
        analytics.setUserProperty("use_bar_chart",
                String.valueOf(prefs.shouldUseBarChart()));
    }

    @Override
    public void reportCounterClicked() {
        analytics.logEvent("counter_clicked", new Bundle());
    }
}
