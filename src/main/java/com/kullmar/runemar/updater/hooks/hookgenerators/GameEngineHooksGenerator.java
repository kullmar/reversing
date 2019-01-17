package com.kullmar.runemar.updater.hooks.hookgenerators;

import com.kullmar.runemar.api.RSGameEngine;
import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.asm.MethodInfo;
import com.kullmar.runemar.updater.hooks.FieldHook;
import com.kullmar.runemar.updater.hooks.GameEngineHooks;
import org.objectweb.asm.Type;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kullmar.runemar.updater.finders.fieldfinders.GameEngineMembersFinder.GAME_CANVAS;

public class GameEngineHooksGenerator implements HookGenerator, GameEngineHooks {
    private FieldInfo gameCanvas;

    @Override
    public FieldHook createCanvasHook() {
        MethodInfo methodInfo = new MethodInfo("getCanvas", "()" + Type.getDescriptor(Canvas.class));
        return new FieldHook(gameCanvas, RSGameEngine.class, methodInfo);
    }

    @Override
    public List<FieldHook> run(Map<String, FieldInfo> identifiedFields) {
        List<FieldHook> fieldHooks = new ArrayList<>();
        gameCanvas = identifiedFields.get(GAME_CANVAS);
        fieldHooks.add(createCanvasHook());
        return fieldHooks;
    }
}
