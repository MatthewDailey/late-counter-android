package com.reactiverobot.latecounter.prefs;

import com.google.inject.AbstractModule;
import com.reactiverobot.latecounter.prefs.LateCounterPrefs;
import com.reactiverobot.latecounter.prefs.LateCounterPrefsImpl;

/*
 * Note that for tests this in manually bound in the set up but for applications it is bound
 * by setting metadata in AndroindManifest.xml
 */
public class PrefsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LateCounterPrefs.class).to(LateCounterPrefsImpl.class);
    }
}
