package com.kullmar.runemar.updater.hooks;

import java.util.ArrayList;
import java.util.List;

public class Hooks {
    private List<FieldHook> fieldHooks = new ArrayList<>();

    public List<FieldHook> getFieldHooks() {
        return fieldHooks;
    }

    public void addFieldHook(FieldHook fieldHook) {
        fieldHooks.add(fieldHook);
    }

    public void addFieldHooks(List<FieldHook> fieldHooks) {
        this.fieldHooks.addAll(fieldHooks);
    }
}
