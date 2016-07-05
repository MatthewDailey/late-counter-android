package com.reactiverobot.latecounter.billing;

import com.google.inject.AbstractModule;

public class BillingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BillingMachine.class).to(BillingMachineImpl.class);
    }
}
