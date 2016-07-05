package com.reactiverobot.latecounter.prefs;

import com.google.inject.AbstractModule;

/*
 * Note that for tests this in manually bound in the set up but for applications it is bound
 * by setting metadata in AndroindManifest.xml
 */
public class PrefsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CounterzPrefs.class).to(CounterzPrefsImpl.class);
    }
}
