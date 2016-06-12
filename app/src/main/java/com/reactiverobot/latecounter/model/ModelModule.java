package com.reactiverobot.latecounter.model;

import com.google.inject.AbstractModule;

public class ModelModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RealmSupplier.class).to(RealmSupplierImpl.class);
        bind(CounterTypes.class).to(CounterTypesRealmImpl.class);
        bind(CounterRecords.class).to(CounterRecordsRealmImpl.class);
    }
}
