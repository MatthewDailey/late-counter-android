package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.analytics.CounterzAnalytics;
import com.reactiverobot.latecounter.billing.BillingMachine;
import com.reactiverobot.latecounter.billing.IabHelper;
import com.reactiverobot.latecounter.billing.IabResult;
import com.reactiverobot.latecounter.billing.Purchase;
import com.reactiverobot.latecounter.prefs.CounterzPrefs;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ReachedCounterLimitActivity extends RoboActivity {

    public static final String REACHED_COUNTER_LIMIT_EXTRA = "REACHED_COUNTER_LIMIT_EXTRA";

    @Inject BillingMachine billingMachine;
    @Inject CounterzPrefs prefs;
    @Inject CounterzAnalytics analytics;


    @InjectView(R.id.promo_edit_text) EditText promoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reached_counter_limit);

        findViewById(R.id.reached_limit_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.reportClickedCancelBuyingPremium();

                finish();
            }
        });

        if (getIntent().getBooleanExtra(REACHED_COUNTER_LIMIT_EXTRA, false)) {
            analytics.reportReachedCounterLimitActivity();
        } else {
            analytics.reportWantedPremiumActivity();
            findViewById(R.id.reached_limit_first_text_view).setVisibility(View.GONE);
        }

        billingMachine.getPremiumCodeText(new BillingMachine.PremiumCodeHandler() {
            @Override
            public void handlePremiumCode(final String premiumCode) {
                promoEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence promoAttempt, int start, int before, int count) {
                        if (premiumCode.equals(promoAttempt.toString())) {
                            analytics.reportCorrectPremiumPassword();
                            promoEditText.setTextColor(getResources().getColor(R.color.green));
                            enablePremiumMode();
                        } else {
                            analytics.reportAttemptedPremiumPassword();
                            promoEditText.setTextColor(getResources().getColor(R.color.red));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
            }
        });

        findViewById(R.id.reached_limit_buy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.reportClickedBuyPremium();

                billingMachine.launchPurchasePremiumFlow(ReachedCounterLimitActivity.this,
                        new BillingMachine.PurchaseFlowCompletedHandler() {
                            @Override
                            public void handleResult(IabResult result, Purchase info) {
                                if (result.isSuccess()) {
                                    analytics.reportPurchasedPremium();
                                    enablePremiumMode();
                                } else if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
                                    analytics.reportPreviouslyPurchasedPremium();
                                    enablePremiumMode();
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

    private void enablePremiumMode() {
        analytics.reportEnabledPremium();

        Toast enablePremiumToast = Toast.makeText(getApplicationContext(),
                "Premium mode enabled.",
                Toast.LENGTH_LONG);
        enablePremiumToast.setGravity(Gravity.CENTER, 0, 0);
        enablePremiumToast.show();

        prefs.enablePremium();
        finish();
    }

}
