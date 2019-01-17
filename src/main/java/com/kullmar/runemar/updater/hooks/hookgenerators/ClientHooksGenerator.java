package com.kullmar.runemar.updater.hooks.hookgenerators;

import com.kullmar.runemar.api.RSClient;
import com.kullmar.runemar.api.RSPlayer;
import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.asm.MethodInfo;
import com.kullmar.runemar.updater.hooks.ClientHooks;
import com.kullmar.runemar.updater.hooks.FieldHook;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kullmar.runemar.updater.finders.fieldfinders.CachedPlayersFinder.CACHED_PLAYERS;
import static com.kullmar.runemar.updater.finders.fieldfinders.LocalPlayerFinder.LOCAL_PLAYER;

public class ClientHooksGenerator implements HookGenerator, ClientHooks {
    private FieldInfo localPlayer;
    private FieldInfo cachedPlayers;

    @Override
    public List<FieldHook> run(Map<String, FieldInfo> identifiedFields) {
        List<FieldHook> fieldHooks = new ArrayList<>();
        localPlayer = identifiedFields.get(LOCAL_PLAYER);
        cachedPlayers = identifiedFields.get(CACHED_PLAYERS);
        fieldHooks.add(createCachedPlayersHook());
        fieldHooks.add(createLocalPlayerHook());
        return fieldHooks;
    }

    @Override
    public FieldHook createLocalPlayerHook() {
        MethodInfo methodInfo = new MethodInfo("getLocalPlayer", "()" + Type.getDescriptor(RSPlayer.class));
        return new FieldHook(localPlayer, RSClient.class, methodInfo, "client");
    }

    @Override
    public FieldHook createCachedPlayersHook() {
        MethodInfo methodInfo = new MethodInfo("getCachedPlayers", "()[" + Type.getDescriptor(RSPlayer.class));
        return new FieldHook(cachedPlayers, RSClient.class, methodInfo, "client");
    }
}
