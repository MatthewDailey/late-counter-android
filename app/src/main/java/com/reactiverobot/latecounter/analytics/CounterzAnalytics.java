package com.reactiverobot.latecounter.analytics;

public interface CounterzAnalytics {
    void reportCounterCreated();

    void reportCreateCounterActivity();

    void reportGraphActivity();

    void reportToggledSampleData();

    void reportMainActivity();

    void reportCounterDeleted();

    void reportChangedCounterColor();

    void reportPickCounterColorActivity();

    void reportPickCounterTypeActivity();

    /*
     * Sets has_opened_upgrade to true.
     */
    void reportReachedCounterLimitActivity();

    /*
     * Sets has_opened_upgrade to true.
     */
    void reportWantedPremiumActivity();

    void reportClickedBuyPremium();

    void reportClickedCancelBuyingPremium();

    void reportCorrectPremiumPassword();

    void reportPurchasedPremium();

    void reportPreviouslyPurchasedPremium();

    void reportAttemptedPremiumPassword();

    void reportEnabledPremium();

    void reportSettingsActivity();

    void reportToggledUseBarChart();

    /**
     * Update user properties to match those on the device.
     * <ul>
     *     <li>Number of counters.</li>
     * </ul>
     */
    void synchronizeUserProperties();

    void reportCounterClicked();
}
