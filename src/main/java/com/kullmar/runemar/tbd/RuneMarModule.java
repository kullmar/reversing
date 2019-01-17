package com.kullmar.runemar.tbd;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

public class RuneMarModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).toInstance(new EventBus());
    }
}

