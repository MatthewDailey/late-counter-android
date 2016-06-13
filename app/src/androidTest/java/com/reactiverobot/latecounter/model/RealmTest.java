package com.reactiverobot.latecounter.model;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.roboguice.shaded.goole.common.base.Supplier;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RealmTest {

    RealmSupplier realmSupplier;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setupRealm() throws IOException {
        File tempFolder = testFolder.newFolder("realmdata");
        final RealmConfiguration config = new RealmConfiguration.Builder(tempFolder)
                .deleteRealmIfMigrationNeeded()
                .build();

        realmSupplier = new RealmSupplierImpl(new Supplier<Realm>() {
            @Override
            public Realm get() {
                return Realm.getInstance(config);
            }
        });
    }

    @Test
    public void testCreateAndQueryRealm() {
        CounterType counterType = CounterType.withDescription("desc");

        CounterRecordsRealmImpl counterRecords = new CounterRecordsRealmImpl(realmSupplier);

        CounterRecord counterRecordYesterday = counterRecords.create(counterType, new Date(-1), 0);

        CounterRecord counterRecordToday = counterRecords.create(counterType, new Date(), 0);

        CounterRecord todaysCount = counterRecords.getTodaysCount(counterType);

        assertThat(todaysCount, is(equalTo(counterRecordToday)));
    }
}
