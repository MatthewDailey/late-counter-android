package com.reactiverobot.latecounter.billing;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.inject.Inject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class BillingMachineImpl implements BillingMachine {

    static final int PREMIUM_REQUEST = 10001;

    private static final String SKU_PREMIUM = "counterz_premium";
    public static final String TAG = "CounterzBilling";

    // TODO: Break this out so it is harder for attacker to spoof.
    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiZGDpda8i1U7Oa" +
            "aDoroh6XnWhNtFJhph6IZrqwGjiijRk+3aMhUAvKbmYnLh9hpFqVGHk+HcfpQmTufyYAdJl68oDkAisZYC" +
            "S+Y/M680aeKUs+p3ZySDDV01kNxZ7tl2ymaMvc9XEriSaFigK34fLgsJ4p0aOLYeoZnU2BYs6s/C5N9Dky" +
            "jJAvQOnpwWafLJrmCMDsrHe/+4VQ32SnUOgk/Xw9zgULn3JrAvmSvA4TEO+VoZVCL3bOLy7t6TKUngF42z" +
            "lSOPf0LGPwDjV+3O2DUTNkK/f4o2MMUb6g+3BQCZ6BzMQqZfILEvbkmbB9wAnTdJWYM9eftlxws9PNdHGQ" +
            "IDAQAB";

    private final Context context;

    private IabHelper iabHelper;
    private CountDownLatch iabStartLatch = new CountDownLatch(0);

    @Inject
    public BillingMachineImpl(Context context) {
        this.context = context;
    }

    @Override
    public synchronized void start() {
        iabStartLatch = new CountDownLatch(1);

        this.iabHelper = new IabHelper(context, base64EncodedPublicKey);

        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");
                iabStartLatch.countDown();
                Log.d(TAG, "Setup successful.");
            }
        });
    }

    @Override
    public synchronized void shutdown() {
        try {
            iabHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            // TODO
        }
        iabHelper = null;
    }

    private boolean verifyDeveloperPayload(Purchase premiumPurchase) {
        // TODO: Store payload on a server somewhere.
        return true;
    }

    @NonNull
    private String getDeveloperPayload() {
        // TODO: Compute a random payload and store it somewhere.
        return "";
    }

    @Override
    public synchronized void launchPurchasePremiumFlow(final Activity callingActivity) {
        try {
            iabHelper.launchPurchaseFlow(callingActivity, SKU_PREMIUM, PREMIUM_REQUEST,
                    new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    Log.d(TAG, "Purchase finished. result: " + result + " purchase: " + info);
                }
            }, getDeveloperPayload());
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            // TODO
        }
    }

    @Override
    public synchronized boolean hasPurchasedPremium() {
        if (iabHelper != null) {
            try {
                iabStartLatch.await(5, TimeUnit.SECONDS);

                Purchase premiumPurchase = iabHelper.queryInventory().getPurchase(SKU_PREMIUM);
                return premiumPurchase != null && verifyDeveloperPayload(premiumPurchase);
            } catch (IabException e) {
                e.printStackTrace();
                // TODO
            } catch (InterruptedException e) {
                e.printStackTrace();
                // TODO
            }
        }
        return false;
    }
}
