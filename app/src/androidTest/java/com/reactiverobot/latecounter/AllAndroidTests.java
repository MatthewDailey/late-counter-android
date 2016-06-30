package com.reactiverobot.latecounter;

import com.reactiverobot.latecounter.model.CounterRecordTest;
import com.reactiverobot.latecounter.model.CounterTypeTest;
import com.reactiverobot.latecounter.model.SimpleRealmSmokeTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CounterRecordTest.class,
        CounterTypeTest.class,
        SimpleRealmSmokeTest.class,
})
public class AllAndroidTests {
}
