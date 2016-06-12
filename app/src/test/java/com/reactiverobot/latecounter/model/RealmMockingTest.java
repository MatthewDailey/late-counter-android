package com.reactiverobot.latecounter.model;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.roboguice.shaded.goole.common.base.Suppliers;

import io.realm.Realm;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

@PrepareForTest({Realm.class})
public abstract class RealmMockingTest {

    @Rule
    public PowerMockRule powerMock = new PowerMockRule();

    public RealmSupplier realmSupplier;
    public Realm realm;

    @Before
    public void setupRealmMock() {
        realm = PowerMockito.mock(Realm.class);

        realmSupplier = new RealmSupplierImpl(Suppliers.ofInstance(realm));

        initializeRealmMockToExecuteTransactions();
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
