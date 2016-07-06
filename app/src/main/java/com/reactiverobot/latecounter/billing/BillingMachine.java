package com.reactiverobot.latecounter.billing;

import android.app.Activity;

public interface BillingMachine {

    void launchPurchasePremiumFlow(Activity callingActivity,
                                   PurchaseFlowCompletedHandler completedHandler);

    public interface PurchaseFlowCompletedHandler {
        void handleResult(IabResult result, Purchase info);
    }
}
