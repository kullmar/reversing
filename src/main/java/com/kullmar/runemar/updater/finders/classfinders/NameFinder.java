package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.ArrayList;
import java.util.List;

import static com.kullmar.runemar.rs.RSClassNames.NAME;

public class NameFinder extends ClassFinder {
    public NameFinder() {
        deobfuscatedName = NAME;
    }

    @Override
    protected ClassHierarchy identifyClass(ClassCollection classCollection) {
        for (ClassHierarchy classHierarchy : getComparableChildren(classCollection)) {
            if (isMatch(classHierarchy.getClassNode())) {
                return classHierarchy;
            }
        }
        return null;
    }

    private List<ClassHierarchy> getComparableChildren(ClassCollection classCollection) {
        List<ClassHierarchy> comparableChildren = new ArrayList<>();
        for (ClassHierarchy classHierarchy : classCollection.getAllClassHierarchies()) {
            List<String> nonJarInterfaces = classHierarchy.getNonJarInterfaces();
            if (!nonJarInterfaces.isEmpty() && nonJarInterfaces.contains("java/lang/Comparable")) {
                comparableChildren.add(classHierarchy);
            }
        }
        return comparableChildren;
    }

    private boolean isMatch(ClassNode classNode) {
        if (classNode.fields.size() != 2) {
            return false;
        }
        for (FieldNode fieldNode : classNode.fields) {
            if (!fieldNode.desc.equals(Type.getDescriptor(String.class))) {
                return false;
            }
        }
        return true;
    }
}
