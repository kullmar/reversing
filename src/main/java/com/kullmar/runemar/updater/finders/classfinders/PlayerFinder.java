package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;

import static com.kullmar.runemar.rs.RSClassNames.ACTOR;
import static com.kullmar.runemar.rs.RSClassNames.PLAYER;

public class PlayerFinder extends ClassFinder {
    public PlayerFinder() {
        deobfuscatedName = PLAYER;
    }

    @Override
    protected ClassHierarchy identifyClass(ClassCollection classCollection) {
        ClassHierarchy actor = classCollection.getClassHierarchyFromRealName((ACTOR));
        if (actor == null) {
            logger.warn("Renderable has not been identified yet. Run RenderableFinder first");
            return null;
        }
        for (ClassHierarchy classHierarchy : actor.getChildren()) {
            if (classHierarchy.getClassNode().fields.size() > 5) {
                return classHierarchy;
            }
        }
        return null;
    }
}
