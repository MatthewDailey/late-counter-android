package com.reactiverobot.latecounter.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.realm.implementation.RealmBarData;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.inject.Inject;

import org.roboguice.shaded.goole.common.base.Throwables;
import org.roboguice.shaded.goole.common.collect.Lists;

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
    public View getPlotViewForType(final Context context, final CounterType counterType) {
        return realmSupplier.callWithRealm(
                new RealmSupplier.RealmCallable<View>() {
                    @Override
                    public View call(Realm realm) {
                        RealmResults<CounterRecord> counterRecordsOfType = realm.where(CounterRecord.class)
                                .equalTo("counterType.description", counterType.getDescription())
                                .findAll();

                        RealmBarDataSet<CounterRecord> dataSet = new RealmBarDataSet<>(
                                counterRecordsOfType,
                                "count",
                                null);
                                //"date");

                        RealmBarData realmBarData = new RealmBarData(
                                counterRecordsOfType,
                                "date",
                                Lists.<IBarDataSet>newArrayList(dataSet));

                        BarChart barChart = new BarChart(context);
                        barChart.setData(realmBarData);
                        barChart.invalidate();
                        barChart.setDescription(counterType.getDescription());
                        return barChart;
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
