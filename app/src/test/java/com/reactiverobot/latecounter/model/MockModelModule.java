package com.reactiverobot.latecounter.model;

import com.google.inject.AbstractModule;
import com.reactiverobot.latecounter.billing.BillingMachine;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.mockito.Mockito.mock;

public class MockModelModule extends AbstractModule implements TestRule {

    public CounterTypes mockCounterTypes;
    public CounterRecords mockCounterRecords;
    public BillingMachine mockBillingMachine;

    @Override
    protected void configure() {
        bind(BillingMachine.class).toInstance(mockBillingMachine);
        bind(CounterTypes.class).toInstance(mockCounterTypes);
        bind(CounterRecords.class).toInstance(mockCounterRecords);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                mockCounterTypes = mock(CounterTypes.class);
                mockCounterRecords = mock(CounterRecords.class);
                mockBillingMachine = mock(BillingMachine.class);

                base.evaluate();
            }
        };
    }
}
