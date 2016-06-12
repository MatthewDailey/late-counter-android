package com.reactiverobot.latecounter.model;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.roboguice.shaded.goole.common.base.Suppliers;

import java.util.Date;

import io.realm.Realm;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;

@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({Realm.class})
public class CounterRecordsRealmImplFastTest {

    @Rule
    public PowerMockRule powerMock = new PowerMockRule();

    private RealmSupplier realmSupplier;
    private Realm realm;

    private CounterType counterType;

    @Before
    public void setup() {
        realm = PowerMockito.mock(Realm.class);
        counterType = mock(CounterType.class);
        realmSupplier = new RealmSupplierImpl(Suppliers.ofInstance(realm));

        initializeRealmMockToExecuteTransactions();
    }

    @Test
    public void testCreate() {
        CounterRecords counterRecords = new CounterRecordsRealmImpl(realmSupplier);

        when(realm.createObject(CounterRecord.class)).thenReturn(new CounterRecord());

        counterRecords.create(counterType, new Date(), 2);

        verify(realm).executeTransaction(any(Realm.Transaction.class));
        verify(realm).createObject(CounterRecord.class);
        verify(realm).close();
    }

    private void initializeRealmMockToExecuteTransactions() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Realm.Transaction) invocation.getArguments()[0]).execute(realm);
                return null;
            }
        }).when(realm).executeTransaction(any(Realm.Transaction.class));
    }
}
