package com.reactiverobot.latecounter.model;


import android.support.annotation.NonNull;

import com.google.inject.Inject;

import org.roboguice.shaded.goole.common.base.Optional;
import org.roboguice.shaded.goole.common.base.Throwables;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    public CounterType updateWidgetForType(final String description, final int widgetId) {
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
    public List<CounterType> loadAllTypes() {
        return realmSupplier.callWithRealm(new RealmSupplier.RealmCallable<List<CounterType>>() {
            @Override
            public List<CounterType> call(Realm realm) {
                return realm.copyFromRealm(realm.where(CounterType.class).findAll());
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

    @Override
    public void createUniqueTypeForWidget(final String description, final int widgetId, final int colorId)
            throws FailureCreatingCounterTypeException {
        final AtomicReference<String> errorMessage = new AtomicReference<>();

        realmSupplier.runWithRealm(new RealmSupplier.RealmRunnable() {
            @Override
            public void run(Realm realm) {
                realm.beginTransaction();

                if (description.isEmpty()) {
                    errorMessage.set("Counter description must not be empty.");
                } else if (!realm.where(CounterType.class)
                        .equalTo("widgetId", widgetId)
                        .findAll().isEmpty()) {
                    errorMessage.set("This widget has a counter already. " +
                                     "Please close everything and try again.");
                } else if (!realm.where(CounterType.class)
                        .equalTo("description", description)
                        .findAll().isEmpty()) {
                    errorMessage.set("There is already a widget for this counter.");
                } else {
                    CounterType type = realm.createObject(CounterType.class);
                    type.setDescription(description);
                    type.setWidgetId(widgetId);
                    type.setColorId(colorId);
                }

                realm.commitTransaction();
            }
        });

        if (errorMessage.get() != null) {
            throw new FailureCreatingCounterTypeException(errorMessage.get());
        }
    }

    @Override
    public void deleteWithDescription(final String description) {
        realmSupplier.runWithRealm(new RealmSupplier.RealmRunnable() {
            @Override
            public void run(Realm realm) {
                realm.beginTransaction();

                realm.where(CounterType.class)
                        .equalTo("description", description)
                        .findAll()
                        .deleteAllFromRealm();

                realm.commitTransaction();
            }
        });
    }

    @NonNull
    private RealmQuery<CounterType> loadTypesWithWidgetId(Realm realm, int widgetId) {
        return realm.where(CounterType.class).equalTo("widgetId", widgetId);
    }



}
