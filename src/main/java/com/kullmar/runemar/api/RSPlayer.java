package com.kullmar.runemar.api;

public interface RSPlayer extends RSActor {
    RSName getRsName();

    default String getName() {
        if (getRsName() == null || getRsName().toString() == null) {
            return "UNKNOWN";
        }
        return getRsName().toString();
    }
    // int getHp();
    // int getCombatLevel();
}
