package com.kullmar.runemar.updater.hooks.hookgenerators;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.hooks.Hooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MasterHookGenerator {
    private List<HookGenerator> hookGenerators = new ArrayList<>();

    public MasterHookGenerator() {
        hookGenerators.add(new ActorHooksGenerator());
        hookGenerators.add(new ClientHooksGenerator());
        hookGenerators.add(new PlayerHooksGenerator());
        hookGenerators.add(new GameEngineHooksGenerator());
        hookGenerators.add(new CameraHooksGenerator());
    }

    public Hooks run(Map<String, FieldInfo> identifiedFields) {
        Hooks hooks = new Hooks();
        for (HookGenerator hookGenerator : hookGenerators) {
            hooks.addFieldHooks(hookGenerator.run(identifiedFields));
        }
        return hooks;
    }
}
