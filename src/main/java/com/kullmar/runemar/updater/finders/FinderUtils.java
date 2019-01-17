package com.kullmar.runemar.updater.finders;

import com.kullmar.runemar.updater.asm.FieldInfo;

import java.util.HashMap;
import java.util.Map;

public class FinderUtils {
    public static Map<String, FieldInfo> createFieldMapping(String key, FieldInfo value) {
        Map<String, FieldInfo> mapping = new HashMap<>();
        mapping.put(key, value);
        return mapping;
    }
}
