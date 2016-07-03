package com.reactiverobot.latecounter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterTypes;

import at.markushi.ui.CircleButton;
import roboguice.activity.RoboActivity;

public class CreateCounterTypeActivity extends RoboActivity {

    @Inject
    CounterTypes counterTypes;

    private int counterColorId = android.R.color.black;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int appWidgetId = getIntent().getIntExtra(PickCounterTypeActivity.WIDGET_ID_EXTRA, -1);
        // TODO: add check handling start activity without widgetId.

        setContentView(R.layout.activity_create_counter_type);

        findViewById(R.id.create_counter_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // TODO: Set app widget color.
                    counterTypes.createUniqueTypeForWidget(getNewDescription(), appWidgetId);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(PickCounterTypeActivity.WIDGET_ID_EXTRA, appWidgetId);

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } catch (CounterTypes.FailureCreatingCounterTypeException e) {
                    Toast.makeText(CreateCounterTypeActivity.this,
                            e.message,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.pick_counter_color_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(CreateCounterTypeActivity.this, PickCounterColorActivity.class),
                        PickCounterColorActivity.PICK_COLOR_REQUEST_CODE);
            }
        });

        findViewById(R.id.create_counter_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getNewDescription() {
        return ((EditText) findViewById(R.id.create_counter_text)).getText().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PickCounterColorActivity.PICK_COLOR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                counterColorId = data.getIntExtra(PickCounterColorActivity.COLOR_ID_EXTRA,
                        android.R.color.black);
                ((CircleButton) findViewById(R.id.pick_counter_color_button))
                        .setColor(getResources().getColor(counterColorId));
            }
        }
    }
}
