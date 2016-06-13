package com.reactiverobot.latecounter.model;

import io.realm.Realm;

interface RealmSupplier {
    Realm get();
    void runWithRealm(RealmRunnable realmRunnable);
    <T> T callWithRealm(RealmCallable<T> realmCallable);

    interface RealmRunnable {
        void run(Realm realm);
    }

    interface RealmCallable<T> {
        T call(Realm realm);
    }
}
