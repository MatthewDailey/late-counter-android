package com.reactiverobot.latecounter.billing;

import android.app.Activity;

public interface BillingMachine {

    void launchPurchasePremiumFlow(Activity callingActivity,
                                   PurchaseFlowCompletedHandler completedHandler);

    void checkPurchasedPremium(CheckPurchaseHandler checkPurchaseHandler);

    void getPremiumCodeText(PremiumCodeHandler premiumCodeHandler);

    interface PurchaseFlowCompletedHandler {
        void handleResult(IabResult result, Purchase info);
    }

    interface CheckPurchaseHandler {
        void handleResult(boolean isPurchased);
    }

    interface PremiumCodeHandler {
        void handlePremiumCode(String premiumCode);
    }
}
