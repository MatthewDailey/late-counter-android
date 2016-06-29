package com.reactiverobot.latecounter.model;


import com.google.inject.Inject;

import org.roboguice.shaded.goole.common.base.Optional;
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
                    return getType(description).get();
                } catch (Throwable e) {
                    throw Throwables.propagate(e);
                }
            }
        });
    }

    @Override
    public CounterType createSafelyWithWidgetId(final String description, final int widgetId) {
        return realmSupplier.callWithRealm(new RealmSupplier.RealmCallable<CounterType>() {
            @Override
            public CounterType call(Realm realm) {
                realm.beginTransaction();
                CounterType counterType = realm.createObject(CounterType.class);
                try {
                    counterType.setDescription(description);
                    counterType.setWidgetid(widgetId);

                    realm.commitTransaction();

                    return realm.copyFromRealm(counterType);
                } catch (RealmPrimaryKeyConstraintException e) {
                    counterType.deleteFromRealm();
                    return getType(description).get();
                } catch (Throwable e) {
                    throw Throwables.propagate(e);
                }
            }
        });
    }

    @Override
    public Optional<CounterType> getType(final String description) {
        return realmSupplier.callWithRealm(new RealmSupplier.RealmCallable<Optional<CounterType>>() {
            @Override
            public Optional<CounterType> call(Realm realm) {
                RealmResults<CounterType> counterType = realm.where(CounterType.class)
                        .equalTo("description", description)
                        .findAll();
                if (counterType.isEmpty()) {
                    return Optional.absent();
                } else {
                    return Optional.of(realm.copyFromRealm(counterType.first()));
                }
            }
        });
    }

}
