package com.reactiverobot.latecounter.model;

import android.support.annotation.NonNull;

import com.google.inject.Inject;

import org.roboguice.shaded.goole.common.base.Throwables;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


class CounterRecordsRealmImpl implements CounterRecords {

    private final RealmSupplier realmSupplier;

    @Inject
    CounterRecordsRealmImpl(RealmSupplier realmSupplier) {
        this.realmSupplier = realmSupplier;
    }

    @Override
    public CounterRecord create(final CounterType type, final Date date, final int count) {
        return realmSupplier.callWithRealm(new RealmSupplier.RealmCallable<CounterRecord>() {
            @Override
            public CounterRecord call(Realm realm) {
                try {
                    realm.beginTransaction();
                    CounterRecord counterRecord = createNewCounterRecordInTransaction(realm, type, date, count);

                    realm.commitTransaction();

                    return realm.copyFromRealm(counterRecord);
                } catch (Throwable t) {
                    realm.cancelTransaction();
                    throw Throwables.propagate(t);
                }
            }
        });
    }

    @NonNull
    private CounterRecord createNewCounterRecordInTransaction(Realm realm, CounterType type, Date date, int count) {
        assert realm.isInTransaction();

        CounterType nonFinalType = type;
        if (!type.isValid()) {
            RealmResults<CounterType> realmResults = realm.where(CounterType.class)
                    .equalTo("description", type.getDescription())
                    .findAll();

            if (realmResults.isEmpty()) {
                nonFinalType = realm.copyToRealm(type);
            } else {
                nonFinalType = realmResults.first();
            }
        }

        CounterRecord counterRecord = realm.createObject(CounterRecord.class);
        counterRecord.setCounterType(nonFinalType);
        counterRecord.setDate(date);
        counterRecord.setCount(count);
        return counterRecord;
    }

    @Override
    public CounterRecord getTodaysCount(final CounterType counterType) {
        return realmSupplier.callWithRealm(new RealmSupplier.RealmCallable<CounterRecord>() {
            @Override
            public CounterRecord call(Realm realm) {
                RealmResults<CounterRecord> todaysCount = realm.where(CounterRecord.class)
                        .equalTo("counterType.description", counterType.getDescription())
                        .greaterThanOrEqualTo("date", getStartOfToday())
                        .findAll();

                if (todaysCount.isEmpty()) {
                    return create(counterType, new Date(), 0);
                } else {
                    return realm.copyFromRealm(todaysCount.first());
                }
            }
        });
    }

    @Override
    public void incrementTodaysCount(final CounterType counterType) {
        realmSupplier.runWithRealm(new RealmSupplier.RealmRunnable() {
            @Override
            public void run(Realm realm) {
                realm.beginTransaction();

                RealmResults<CounterRecord> todaysCount = realm.where(CounterRecord.class)
                        .equalTo("counterType.description", counterType.getDescription())
                        .greaterThanOrEqualTo("date", getStartOfToday())
                        .findAll();

                if (todaysCount.isEmpty()) {
                    createNewCounterRecordInTransaction(realm, counterType, new Date(), 1);
                } else {
                    CounterRecord counterRecord = todaysCount.first();
                    counterRecord.setCount(counterRecord.getCount() + 1);
                }
                realm.commitTransaction();
            }
        });
    }

    @Override
    public List<CounterRecord> loadAllForTypeOrderedByDate(final CounterType counterType) {
        return realmSupplier.callWithRealm(
                new RealmSupplier.RealmCallable<List<CounterRecord>>() {
                    @Override
                    public List<CounterRecord> call(Realm realm) {
                        return realm.copyFromRealm(
                                realm.where(CounterRecord.class)
                                        .equalTo("counterType.description", counterType.getDescription())
                                        .findAllSorted("date"));
                    }
                }
        );
    }

    private Date getStartOfToday() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }


}
