package com.kullmar.runemar.updater.finders.fieldfinders;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterFieldFinder {
    private Map<String, FieldInfo> foundFields = new HashMap<>();
    private List<FieldFinder> fieldFinders = new ArrayList<>();

    public MasterFieldFinder() {
        fieldFinders.add(new LocalPlayerFinder());
        fieldFinders.add(new PlayerMembersFinder());
        fieldFinders.add(new ActorMembersFinder());
        fieldFinders.add(new GameEngineMembersFinder());
        fieldFinders.add(new CachedPlayersFinder());
        fieldFinders.add(new CameraFieldsFinder());
    }

    public Map<String, FieldInfo> getFoundFields() {
        return foundFields;
    }

    public void run(ClassCollection classCollection) {
        for (FieldFinder staticFieldFinder : fieldFinders) {
            foundFields.putAll(staticFieldFinder.run(classCollection));
        }
    }
}
