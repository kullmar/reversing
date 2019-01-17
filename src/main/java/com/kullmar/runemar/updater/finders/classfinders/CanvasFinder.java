package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;

@Deprecated
public class CanvasFinder extends ClassFinder {
    public CanvasFinder() {
        deobfuscatedName = "Canvas";
    }

    @Override
    protected ClassHierarchy identifyClass(ClassCollection classCollection) {
        for (ClassHierarchy classHierarchy : classCollection.getAllClassHierarchies()) {
            if (classHierarchy.getNonJarParent().equals("java/awt/Canvas")) {
                return classHierarchy;
            }
        }
        return null;
    }
}
