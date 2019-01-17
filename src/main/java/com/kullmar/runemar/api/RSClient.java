package com.kullmar.runemar.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface RSClient extends RSGameEngine {
    RSPlayer getLocalPlayer();

    RSPlayer[] getCachedPlayers();

    int getCameraX();
    int getCameraY();
    int getCameraZ();
    int getCameraYaw();
    int getCameraPitch();

    default List<RSPlayer> getPlayers() {
        return new ArrayList<>(Arrays.asList(getCachedPlayers()));
    }
}
