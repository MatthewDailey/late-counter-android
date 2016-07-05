package com.reactiverobot.latecounter.model;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.roboguice.shaded.goole.common.base.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class CounterTypeTest {

    @Rule
    public RealmSupplierRule realm = new RealmSupplierRule();

    private CounterTypes counterTypes;

    @Before
    public void setupCounterTypes() {
        counterTypes = new CounterTypesRealmImpl(realm.getRealmSupplier());
    }

    @Test
    public void testReadNonExistantType() {
        assertFalse(counterTypes.getType("new-type").isPresent());
    }

    @Test
    public void testCreateWithWidgetId() throws CounterTypes.CounterTypesException {
        CounterType type = counterTypes.createUniqueTypeForWidget("new-type", 1, android.R.color.black);
        Optional<CounterType> typeOptional = counterTypes.getType("new-type");
        assertThat(type, is(equalTo(typeOptional.get())));
    }

    @Test
    public void testLoadTypeByWidgetId() throws CounterTypes.CounterTypesException {
        CounterType originalType = counterTypes
                .createUniqueTypeForWidget("new-type", 1, android.R.color.black);

        Optional<CounterType> type = counterTypes.getTypeForWidget(1);
        assertThat(type.get(), is(equalTo(originalType)));
    }

    @Test
    public void testLoadNonExistantTypeByWidgetId() throws CounterTypes.CounterTypesException {
        counterTypes.createUniqueTypeForWidget("widget", 1, android.R.color.black);

        Optional<CounterType> type = counterTypes.getTypeForWidget(2);
        assertThat(type.isPresent(), is(false));
    }

    @Test
    public void testLoadAllTypesWithNoWidget() throws CounterTypes.CounterTypesException {
        counterTypes.createUniqueTypeForWidget("widget", 1, android.R.color.black);
        counterTypes.createUniqueTypeForWidget("widget2", 2, android.R.color.black);
        counterTypes.removeWidgetId(1);

        assertThat(counterTypes.loadTypesWithNoWidget().size(), is(equalTo(1)));
    }

    @Test
    public void testRemoveWidgetIdForType() throws CounterTypes.CounterTypesException {
        CounterType type = counterTypes.createUniqueTypeForWidget("widget", 1, android.R.color.black);

        assertThat(counterTypes.loadTypesWithNoWidget().size(), is(equalTo(0)));

        counterTypes.removeWidgetId(type.getWidgetId());

        assertThat(counterTypes.loadTypesWithNoWidget().size(), is(equalTo(1)));
    }

    @Test
    public void testCreateUnique() throws CounterTypes.CounterTypesException {
        counterTypes.createUniqueTypeForWidget("new-type", 1, android.R.color.black);

        Optional<CounterType> type = counterTypes.getType("new-type");
        assertThat(type.get().getWidgetId(), is(equalTo(1)));
        assertThat(type.get().getColorId(), is(equalTo(android.R.color.black)));
    }

    @Test(expected = CounterTypes.CounterTypesException.class)
    public void testCreateUniqueWithDuplicateDescription()
            throws CounterTypes.CounterTypesException {
        counterTypes.createUniqueTypeForWidget("new-type", 1, android.R.color.black);
        counterTypes.createUniqueTypeForWidget("new-type", 2, 2);
    }

    @Test(expected = CounterTypes.CounterTypesException.class)
    public void testCreateUniqueWithDuplicateWidgetid()
            throws CounterTypes.CounterTypesException {
        counterTypes.createUniqueTypeForWidget("new-type", 1, android.R.color.black);
        counterTypes.createUniqueTypeForWidget("different-new-type", 1, android.R.color.black);
    }

    @Test(expected = CounterTypes.CounterTypesException.class)
    public void testCreateUniqueWithEmptyDescription()
            throws CounterTypes.CounterTypesException {
        counterTypes.createUniqueTypeForWidget("", 1, android.R.color.black);
    }

    @Test
    public void testDeleteByDescription() throws CounterTypes.CounterTypesException {
        counterTypes.createUniqueTypeForWidget("type", 1, android.R.color.black);

        assertThat(counterTypes.getType("type").isPresent(), is(true));

        counterTypes.deleteWithDescription("diff-type");

        assertThat(counterTypes.getType("type").isPresent(), is(true));

        counterTypes.deleteWithDescription("type");

        assertThat(counterTypes.getType("type").isPresent(), is(false));
    }
}
