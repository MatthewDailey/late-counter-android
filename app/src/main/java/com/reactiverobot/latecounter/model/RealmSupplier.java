package com.reactiverobot.latecounter.model;

import io.realm.Realm;

interface RealmSupplier {
    Realm get();
    void runWithRealm(RealmRunnable realmRunnable);

    interface RealmRunnable {
        void run(Realm realm);
    }
}
