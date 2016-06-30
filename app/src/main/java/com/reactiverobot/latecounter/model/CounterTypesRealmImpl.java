package com.reactiverobot.latecounter.model;


import android.support.annotation.NonNull;

import com.google.inject.Inject;

import org.roboguice.shaded.goole.common.base.Optional;
import org.roboguice.shaded.goole.common.base.Throwables;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

class CounterTypesRealmImpl implements CounterTypes {

    private static final int NO_WIDGET_ID = -1;
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
                    counterType.setWidgetId(NO_WIDGET_ID);

                    realm.commitTransaction();

                    return counterType;
                } catch (RealmPrimaryKeyConstraintException e) {
                    counterType.deleteFromRealm();
                    realm.commitTransaction();

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
                    counterType.setWidgetId(widgetId);

                    realm.commitTransaction();

                    return realm.copyFromRealm(counterType);
                } catch (RealmPrimaryKeyConstraintException e) {
                    counterType.deleteFromRealm();
                    CounterType preExistingType = realm.where(CounterType.class)
                            .equalTo("description", description)
                            .findAll()
                            .first();
                    preExistingType.setWidgetId(widgetId);
                    realm.commitTransaction();

                    return realm.copyFromRealm(preExistingType);
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

    @Override
    public Optional<CounterType> getTypeForWidget(final int widgetId) {
        return realmSupplier.callWithRealm(new RealmSupplier.RealmCallable<Optional<CounterType>>() {
            @Override
            public Optional<CounterType> call(Realm realm) {
                RealmResults<CounterType> counterType = realm.where(CounterType.class)
                        .equalTo("widgetId", widgetId)
                        .findAll();
                if (counterType.isEmpty()) {
                    return Optional.absent();
                } else {
                    return Optional.of(realm.copyFromRealm(counterType.first()));
                }
            }
        });
    }

    @Override
    public List<CounterType> loadTypesWithNoWidget() {
        return realmSupplier.callWithRealm(new RealmSupplier.RealmCallable<List<CounterType>>() {
            @Override
            public List<CounterType> call(Realm realm) {
                return realm.copyFromRealm(loadTypesWithWidgetId(realm, NO_WIDGET_ID).findAll());
            }
        });
    }

    @Override
    public void removeWidgetId(final int widgetId) {
        realmSupplier.runWithRealm(new RealmSupplier.RealmRunnable() {
            @Override
            public void run(Realm realm) {
                realm.beginTransaction();

                for (CounterType type : loadTypesWithWidgetId(realm, widgetId).findAll()) {
                    type.setWidgetId(NO_WIDGET_ID);
                }

                realm.commitTransaction();
            }
        });
    }

    @NonNull
    private RealmQuery<CounterType> loadTypesWithWidgetId(Realm realm, int widgetId) {
        return realm.where(CounterType.class).equalTo("widgetId", widgetId);
    }

}
