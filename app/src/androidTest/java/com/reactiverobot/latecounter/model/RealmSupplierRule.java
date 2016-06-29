package com.reactiverobot.latecounter.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.roboguice.shaded.goole.common.base.Supplier;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

class RealmSupplierRule extends ExternalResource {

    private RealmSupplier realmSupplier;
    private TemporaryFolder testFolder = new TemporaryFolder();

    public RealmSupplier getRealmSupplier() {
        return realmSupplier;
    }

    @Override
    public void before() throws Throwable {
        testFolder.create();

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

    @Override
    public void after() {
        testFolder.delete();
    }
}
