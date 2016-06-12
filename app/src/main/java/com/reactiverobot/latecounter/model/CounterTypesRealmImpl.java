package com.reactiverobot.latecounter.model;


import android.util.Log;

import com.google.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

class CounterTypesRealmImpl implements CounterTypes {

    private final RealmSupplier realmSupplier;

    @Inject
    CounterTypesRealmImpl(RealmSupplier realmSupplier) {
        this.realmSupplier = realmSupplier;
    }

    @Override
    public void createSafely(final String description) {
        realmSupplier.runWithRealm(new RealmSupplier.RealmRunnable() {
            @Override
            public void run(Realm realm) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        CounterType counterType = realm.createObject(CounterType.class);

                        try {
                            counterType.setDescription(description);
                        } catch (RealmPrimaryKeyConstraintException e) {
                            counterType.deleteFromRealm();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void dump() {
        realmSupplier.runWithRealm(new RealmSupplier.RealmRunnable() {
            @Override
            public void run(Realm realm) {
                RealmResults<CounterType> counterTypes = realm.where(CounterType.class).findAll();
                for (CounterType type : counterTypes) {
                    Log.i("test-load", type.toString());
                }
            }
        });
    }

}
