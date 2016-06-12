package com.reactiverobot.latecounter.model;


import com.google.inject.Inject;

import io.realm.Realm;

class CounterTypesRealmImpl implements CounterTypes {

    private final RealmSupplier realmSupplier;

    @Inject
    CounterTypesRealmImpl(RealmSupplier realmSupplier) {
        this.realmSupplier = realmSupplier;
    }

    @Override
    public void create(final String description) {
        realmSupplier.runWithRealm(new RealmSupplier.RealmRunnable() {
            @Override
            public void run(Realm realm) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        CounterType counterType = realm.createObject(CounterType.class);
                        counterType.setDescription(description);
                    }
                });
            }
        });
    }
}
