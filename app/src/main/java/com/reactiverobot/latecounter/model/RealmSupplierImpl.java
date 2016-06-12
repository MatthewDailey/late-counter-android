package com.reactiverobot.latecounter.model;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.google.inject.Inject;

import org.roboguice.shaded.goole.common.base.Supplier;

import io.realm.Realm;
import io.realm.RealmConfiguration;


class RealmSupplierImpl implements RealmSupplier {

    private final Supplier<Realm> supplier;

    @Inject
    RealmSupplierImpl(final Context context) {
        this.supplier = new Supplier<Realm>() {
            @Override
            public Realm get() {
                RealmConfiguration config = new RealmConfiguration.Builder(context).build();

                return Realm.getInstance(config);
            }
        };
    }

    @VisibleForTesting
    RealmSupplierImpl(Supplier<Realm> testSupplier) {
        this.supplier = testSupplier;
    }

    @Override
    public Realm get() {
        return supplier.get();
    }

    @Override
    public void runWithRealm(RealmRunnable realmRunnable) {
        Realm realm = get();
        try {
            realmRunnable.run(realm);
        } finally {
            realm.close();
        }
    }
}
