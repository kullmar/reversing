package com.kullmar.runemar.updater.finders.fieldfinders;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;

import java.util.Map;

public interface FieldFinder {
    Map<String, FieldInfo> run(ClassCollection classCollection);
}
