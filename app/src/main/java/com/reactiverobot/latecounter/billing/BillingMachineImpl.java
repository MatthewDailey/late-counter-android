package com.reactiverobot.latecounter.billing;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.inject.Inject;

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

    @Inject
    public BillingMachineImpl(Context context) {
        this.context = context;
    }

    @Override
    public synchronized void launchPurchasePremiumFlow(final Activity callingActivity,
                                                       final PurchaseFlowCompletedHandler completedHandler) {
        runWithFirebaseRemoteConfig(new Runnable() {
            @Override
            public void run() {
                launchPurchasePremiumFlowInternal(callingActivity, completedHandler);
            }
        });
    }

    private void launchPurchasePremiumFlowInternal(final Activity callingActivity, final PurchaseFlowCompletedHandler completedHandler) {
        final IabHelper iabHelper = new IabHelper(context, base64EncodedPublicKey);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");
                if (!result.isSuccess()) {
                    Log.d(TAG, "Setup failed. " + result.getMessage());
                }

                try {
                    iabHelper.launchPurchaseFlow(callingActivity, SKU_PREMIUM, PREMIUM_REQUEST,
                            new IabHelper.OnIabPurchaseFinishedListener() {
                                @Override
                                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                                    Log.d(TAG, "Purchase finished. result: " + result + " purchase: " + info);
                                    completedHandler.handleResult(result, info);
                                }
                            }, getDeveloperPayload());

                    iabHelper.disposeWhenFinished();
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                    // TODO
                }
                Log.d(TAG, "Setup successful.");
            }
        });
    }

    private void runWithFirebaseRemoteConfig(final Runnable runnable) {
        FirebaseRemoteConfig.getInstance()
                .fetch(60)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseRemoteConfig.getInstance().activateFetched();

                runnable.run();
            }
        });
    }

    @Override
    public void checkPurchasedPremium(final CheckPurchaseHandler handler) {
        runWithFirebaseRemoteConfig(new Runnable() {
            @Override
            public void run() {
                checkPurchasedPremiumInternal(handler);
            }
        });
    }

    @Override
    public void getPremiumCodeText(final PremiumCodeHandler premiumCodeHandler) {
        FirebaseRemoteConfig.getInstance()
                .fetch()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseRemoteConfig.getInstance().activateFetched();

                        final String premiumCode = FirebaseRemoteConfig.getInstance()
                                .getString("free_premium_code");

                        premiumCodeHandler.handlePremiumCode(premiumCode);
                    }
                });
    }


    private void checkPurchasedPremiumInternal(final CheckPurchaseHandler handler) {
        final IabHelper iabHelper = new IabHelper(context, base64EncodedPublicKey);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");
                if (!result.isSuccess()) {
                    Log.d(TAG, "Setup failed. " + result.getMessage());
                }

                try {
                    Purchase purchase = iabHelper.queryInventory().getPurchase(SKU_PREMIUM);

                    handler.handleResult(purchase != null && verifyDeveloperPayload(purchase));

                    iabHelper.disposeWhenFinished();
                } catch (IabException e) {
                    // TODO
                    e.printStackTrace();
                }
                Log.d(TAG, "Setup successful.");
            }
        });
    }

    private boolean verifyDeveloperPayload(Purchase premiumPurchase) {
        return premiumPurchase.getDeveloperPayload().equals(getDeveloperPayload());
    }

    @NonNull
    private String getDeveloperPayload() {
        return FirebaseRemoteConfig.getInstance().getString("developer_payload");
    }

}
