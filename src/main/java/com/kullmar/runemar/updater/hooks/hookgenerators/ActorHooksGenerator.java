package com.kullmar.runemar.updater.hooks.hookgenerators;

import com.kullmar.runemar.api.RSActor;
import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.asm.MethodInfo;
import com.kullmar.runemar.updater.hooks.ActorHooks;
import com.kullmar.runemar.updater.hooks.FieldHook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kullmar.runemar.updater.finders.fieldfinders.ActorMembersFinder.ACTOR_X;
import static com.kullmar.runemar.updater.finders.fieldfinders.ActorMembersFinder.ACTOR_Y;

public class ActorHooksGenerator implements HookGenerator, ActorHooks {
    private FieldInfo actorX;
    private FieldInfo actorY;

    @Override
    public List<FieldHook> run(Map<String, FieldInfo> identifiedFields) {
        actorX = identifiedFields.get(ACTOR_X);
        actorY = identifiedFields.get(ACTOR_Y);
        List<FieldHook> fieldHooks = new ArrayList<>();
        fieldHooks.add(createXHook());
        fieldHooks.add(createYHook());
        return fieldHooks;
    }

    @Override
    public FieldHook createXHook() {
        MethodInfo methodInfo = new MethodInfo("getX", "()I");
        return new FieldHook(actorX, RSActor.class, methodInfo);
    }

    @Override
    public FieldHook createYHook() {
        MethodInfo methodInfo = new MethodInfo("getY", "()I");
        return new FieldHook(actorY, RSActor.class, methodInfo);
    }
}
