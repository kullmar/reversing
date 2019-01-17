package com.kullmar.runemar.updater.hooks;

public interface ClientHooks {
    FieldHook createLocalPlayerHook();

    FieldHook createCachedPlayersHook();
}
