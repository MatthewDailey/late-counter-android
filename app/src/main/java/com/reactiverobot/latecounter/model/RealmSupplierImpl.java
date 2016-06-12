package com.reactiverobot.latecounter.model;

import android.content.Context;

import com.google.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;


class RealmSupplierImpl implements RealmSupplier {

    private final Context context;

    @Inject
    RealmSupplierImpl(Context context) {
        this.context = context;
    }

    @Override
    public Realm get() {
        RealmConfiguration config = new RealmConfiguration.Builder(context).build();

        return Realm.getInstance(config);
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
