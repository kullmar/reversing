package com.kullmar.runemar.updater.hooks.hookgenerators;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.hooks.FieldHook;

import java.util.List;
import java.util.Map;

public interface HookGenerator {
    List<FieldHook> run(Map<String, FieldInfo> identifiedFields);
}
