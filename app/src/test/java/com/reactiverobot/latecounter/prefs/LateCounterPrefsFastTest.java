package com.reactiverobot.latecounter.prefs;


import android.content.Context;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class LateCounterPrefsFastTest {

    @Test
    public void testCanInstantiate() {
        new LateCounterPrefsImpl(mock(Context.class));
    }

}
