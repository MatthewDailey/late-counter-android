package com.reactiverobot.latecounter;


import android.content.Context;

import com.reactiverobot.latecounter.prefs.LateCounterPrefsImpl;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class LateCounterPrefsFastTest {

    @Test
    public void testCanInstantiate() {
        new LateCounterPrefsImpl(mock(Context.class));
    }

}
