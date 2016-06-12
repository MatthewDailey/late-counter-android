package com.reactiverobot.latecounter.roboguice;

import com.google.inject.AbstractModule;
import com.reactiverobot.latecounter.ILateCounterPrefs;
import com.reactiverobot.latecounter.LateCounterPrefs;

/*
 * Note that for tests this in manually bound in the set up but for applications it is bound
 * by setting metadata in AndroindManifest.xml
 */
public class LateCounterInjectionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ILateCounterPrefs.class).to(LateCounterPrefs.class);
    }
}
