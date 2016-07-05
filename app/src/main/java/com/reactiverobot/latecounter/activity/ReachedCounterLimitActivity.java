package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.billing.BillingMachine;
import com.reactiverobot.latecounter.billing.IabHelper;
import com.reactiverobot.latecounter.billing.IabResult;
import com.reactiverobot.latecounter.billing.Purchase;
import com.reactiverobot.latecounter.prefs.CounterzPrefs;

import roboguice.activity.RoboActivity;

public class ReachedCounterLimitActivity extends RoboActivity {

    @Inject BillingMachine billingMachine;
    @Inject CounterzPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reached_counter_limit);

        findViewById(R.id.reached_limit_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.reached_limit_buy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingMachine.launchPurchasePremiumFlow(ReachedCounterLimitActivity.this,
                        new BillingMachine.PurchaseFlowCompletedHandler() {
                            @Override
                            public void handleResult(IabResult result, Purchase info) {
                                if (result.isSuccess()
                                        || result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
                                    prefs.enablePremium();
                                    finish();
                                } else {
                                    Log.e("ReachedLimit", "Failure to purchase: " + result);
                                    Toast.makeText(getApplicationContext(),
                                            "Premium purchase not completed.",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        billingMachine.shutdown();
    }
}
