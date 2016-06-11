package com.reactiverobot.latecounter;


import android.content.Context;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class LateCounterPrefsFastTest {

    @Test
    public void testCanInstantiate() {
        new LateCounterPrefs(mock(Context.class));
    }

}
