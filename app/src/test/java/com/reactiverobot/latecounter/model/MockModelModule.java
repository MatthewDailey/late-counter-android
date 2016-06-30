package com.reactiverobot.latecounter.model;

import com.google.inject.AbstractModule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.mockito.Mockito.mock;

public class MockModelModule extends AbstractModule implements TestRule {

    public CounterTypes counterTypes;
    public CounterRecords mockCounterRecords;

    @Override
    protected void configure() {
        bind(CounterTypes.class).toInstance(counterTypes);
        bind(CounterRecords.class).toInstance(mockCounterRecords);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                counterTypes = mock(CounterTypes.class);
                mockCounterRecords = mock(CounterRecords.class);

                base.evaluate();
            }
        };
    }
}
