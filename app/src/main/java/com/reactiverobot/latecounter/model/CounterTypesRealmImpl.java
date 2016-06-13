package com.reactiverobot.latecounter.model;


import android.util.Log;

import com.google.inject.Inject;

import org.roboguice.shaded.goole.common.base.Throwables;

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
    public CounterType createSafely(final String description) {
        return realmSupplier.callWithRealm(new RealmSupplier.RealmCallable<CounterType>() {
            @Override
            public CounterType call(Realm realm) {
                realm.beginTransaction();
                CounterType counterType = realm.createObject(CounterType.class);
                try {
                    counterType.setDescription(description);

                    realm.commitTransaction();

                    return counterType;
                } catch (RealmPrimaryKeyConstraintException e) {
                    counterType.deleteFromRealm();
                    return realm.where(CounterType.class)
                            .equalTo("description", description)
                            .findAll()
                            .first();
                } catch (Throwable e) {
                    throw Throwables.propagate(e);
                }
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
