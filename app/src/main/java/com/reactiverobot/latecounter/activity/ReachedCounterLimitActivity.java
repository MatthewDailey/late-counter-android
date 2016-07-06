package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
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

    @InjectView(R.id.promo_edit_text) EditText promoEditText;

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

        if (!getIntent().getBooleanExtra(REACHED_COUNTER_LIMIT_EXTRA, false)) {
            findViewById(R.id.reached_limit_first_text_view).setVisibility(View.GONE);
        }

        setupPromoCode();

        findViewById(R.id.reached_limit_buy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingMachine.launchPurchasePremiumFlow(ReachedCounterLimitActivity.this,
                        new BillingMachine.PurchaseFlowCompletedHandler() {
                            @Override
                            public void handleResult(IabResult result, Purchase info) {
                                if (result.isSuccess()
                                        || result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
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

    private void setupPromoCode() {
        FirebaseRemoteConfig.getInstance()
                .fetch()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseRemoteConfig.getInstance().activateFetched();

                        final String promoCode = FirebaseRemoteConfig.getInstance()
                                .getString("free_premium_code");

                        promoEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                            @Override
                            public void onTextChanged(CharSequence promoAttempt, int start, int before, int count) {
                                if (promoCode.equals(promoAttempt.toString())) {
                                    promoEditText.setTextColor(getResources().getColor(R.color.green));
                                    enablePremiumMode();
                                } else {
                                    promoEditText.setTextColor(getResources().getColor(R.color.red));
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });
                    }
                });

    }

    private void enablePremiumMode() {
        Toast enablePremiumToast = Toast.makeText(getApplicationContext(),
                "Premium mode enabled.",
                Toast.LENGTH_LONG);
        enablePremiumToast.setGravity(Gravity.CENTER, 0, 0);
        enablePremiumToast.show();

        prefs.enablePremium();
        finish();
    }

}
