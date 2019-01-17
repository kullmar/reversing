package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;

import static com.kullmar.runemar.rs.RSClassNames.GAME_ENGINE;

public class GameEngineFinder extends ClassFinder {
    public GameEngineFinder() {
        deobfuscatedName = GAME_ENGINE;
    }

    @Override
    protected ClassHierarchy identifyClass(ClassCollection classCollection) {
        for (ClassHierarchy classHierarchy : classCollection.getAllClassHierarchies()) {
            if (classHierarchy.getName().equals("client")) {
                return classHierarchy.getParent();
            }
        }
        return null;
    }
}
