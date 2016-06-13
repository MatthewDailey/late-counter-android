package com.reactiverobot.latecounter.model;

import com.google.inject.Inject;

import org.roboguice.shaded.goole.common.base.Throwables;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

                    realm.commitTransaction();

                    return realm.copyFromRealm(counterRecord);
                } catch (Throwable t){
                    realm.cancelTransaction();
                    throw Throwables.propagate(t);
                }
            }
        });
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

    private Date getStartOfToday() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }


}
