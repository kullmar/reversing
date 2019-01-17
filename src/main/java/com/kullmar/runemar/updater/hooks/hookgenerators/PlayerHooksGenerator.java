package com.kullmar.runemar.updater.hooks.hookgenerators;

import com.kullmar.runemar.api.RSName;
import com.kullmar.runemar.api.RSPlayer;
import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.asm.MethodInfo;
import com.kullmar.runemar.updater.hooks.FieldHook;
import com.kullmar.runemar.updater.hooks.PlayerHooks;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kullmar.runemar.updater.finders.fieldfinders.PlayerMembersFinder.PLAYER_NAME;

public class PlayerHooksGenerator implements HookGenerator, PlayerHooks {
    private FieldInfo playerName;

    @Override
    public List<FieldHook> run(Map<String, FieldInfo> identifiedFields) {
        List<FieldHook> fieldHooks = new ArrayList<>();
        playerName = identifiedFields.get(PLAYER_NAME);
        fieldHooks.add(createNameHook());
        return fieldHooks;
    }

    @Override
    public FieldHook createNameHook() {
        MethodInfo methodInfo = new MethodInfo("getRsName", "()" + Type.getDescriptor(RSName.class));
        return new FieldHook(playerName, RSPlayer.class, methodInfo);
    }
}
