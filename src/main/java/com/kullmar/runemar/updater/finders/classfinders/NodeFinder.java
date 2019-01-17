package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.kullmar.runemar.rs.RSClassNames.NODE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class NodeFinder extends ClassFinder {
    public NodeFinder() {
        deobfuscatedName = NODE;
    }

    private List<ClassHierarchy> findInteresting(ClassCollection classCollection) {
        List<ClassHierarchy> interesting = new ArrayList<>(classCollection.getAllClassHierarchies());
        interesting.sort(new SortByChildren());
        return interesting.subList(0, 5);
    }

    @Override
    protected ClassHierarchy identifyClass(ClassCollection classCollection) {
        List<ClassHierarchy> potentialMatches = findInteresting(classCollection);
        for (ClassHierarchy ch : potentialMatches) {
            if (isNode(ch)) {
                return ch;
            }
        }
        return null;
    }

    @Override
    protected void analyzeClass() {
        identifyFields();
        identifyMethods();
    }

    private void identifyMethods() {
        if (identifiedClass == null) {
            return;
        }
        String className = identifiedClass.getName();
        for (MethodNode methodNode : identifiedClass.getClassNode().methods) {
            if (methodNode.name.equals("<init>") || methodNode.name.equals("<clinit>")) {
                continue;
            }
            String identifiedName = "";
            String obfuscatedName = className + "." + methodNode.name + methodNode.desc;
            if (methodNode.instructions.size() > 20) {
                identifiedName = "remove";
            }
            else {
                identifiedName = "isNotFirst";
            }
            identifiedNames.put(obfuscatedName, identifiedName);
        }
    }

    private void identifyFields() {
        if (identifiedClass == null) {
            return;
        }
        String className = identifiedClass.getName();
        String classDesc = "L" + className + ";";
        for (FieldNode field : identifiedClass.getClassNode().fields) {
            String identifiedName = "";
            String obfuscatedName = className + "." + field.name;
            if (field.desc.equals(classDesc)) {
                if ((field.access & ACC_PUBLIC) != 0) {
                    identifiedName = "next";
                }
                else {
                    identifiedName = "previous";
                }
            }
            else if (field.desc.equals("J")) {
                identifiedName = "uid";
            }
            identifiedNames.put(obfuscatedName, identifiedName);
        }
    }

    private boolean isNode(ClassHierarchy classHierarchy) {
        if (classHierarchy.getParent() != null) {
            return false;
        }
        ClassNode cn = classHierarchy.getClassNode();
        return hasCorrectFields(cn);
    }

    private boolean hasCorrectFields(ClassNode cn) {
        if (cn.fields.size() != 3) {
            return false;
        }
        int nodeFields = 0, longFields = 0;
        String classDesc = "L" + cn.name + ";";
        String longDesc = "J";
        for (FieldNode fn : cn.fields) {
            if (fn.desc.equals(classDesc)) {
                nodeFields++;
            }
            else if (fn.desc.equals(longDesc)) {
                longFields++;
            }
        }
        return nodeFields == 2 && longFields == 1;
    }

    class SortByChildren implements Comparator<ClassHierarchy> {
        @Override
        public int compare(ClassHierarchy a, ClassHierarchy b) {
            return b.getChildren().size() - a.getChildren().size();
        }
    }
}
