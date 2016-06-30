package com.reactiverobot.latecounter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;
import com.reactiverobot.latecounter.R;
import com.reactiverobot.latecounter.model.CounterTypes;

import roboguice.activity.RoboActivity;

public class CreateCounterTypeActivity extends RoboActivity {

    @Inject
    CounterTypes counterTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int appWidgetId = getIntent().getIntExtra(PickCounterTypeActivity.WIDGET_ID_EXTRA, -1);
        // TODO: add check handling start activity without widgetId.

        setContentView(R.layout.create_counter_type_layout);

        findViewById(R.id.create_counter_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    counterTypes.createUniqueTypeForWidget(getNewDescription(), appWidgetId);
                } catch (CounterTypes.FailureCreatingCounterTypeException e) {
                    Toast.makeText(CreateCounterTypeActivity.this,
                            e.message,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getNewDescription() {
        return ((EditText) findViewById(R.id.create_counter_text)).getText().toString();
    }

}
