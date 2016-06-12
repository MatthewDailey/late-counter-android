package com.reactiverobot.latecounter.model;

import com.google.inject.Inject;

import java.util.Date;

import io.realm.Realm;


class CounterRecordsRealmImpl implements CounterRecords {

    private final RealmSupplier realmSupplier;

    @Inject
    CounterRecordsRealmImpl(RealmSupplier realmSupplier) {
        this.realmSupplier = realmSupplier;
    }

    @Override
    public void create(final CounterType type, final Date date, final int count) {
        realmSupplier.runWithRealm(new RealmSupplier.RealmRunnable() {
            @Override
            public void run(Realm realm) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        CounterRecord dailyRecord = realm.createObject(CounterRecord.class);
                        dailyRecord.setCounterType(type);
                        dailyRecord.setDate(date);
                        dailyRecord.setCount(count);
                    }
                });
            }
        });
    }
}
