package com.kullmar.runemar.updater.deobfuscators;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UnusedFieldsRemover implements Deobfuscator {
    private static final Logger logger = LoggerFactory.getLogger(UnusedFieldsRemover.class);
    private Set<FieldInfo> usedFields = new HashSet<>();

    @Override
    public void run(ClassCollection classCollection) {
        long startTime = System.currentTimeMillis();
        logger.info("Removing unused fields...");
        int fieldsRemoved = 0;
        List<ClassNode> classNodes = classCollection.getAllClassNodes();
        findUsedFields(classNodes);
        for (ClassNode classNode : classNodes) {
            Iterator iterator = classNode.fields.listIterator();
            while (iterator.hasNext()) {
                FieldNode fieldNode = (FieldNode) iterator.next();
                FieldInfo fieldInfo = new FieldInfo(fieldNode.name, classNode.name, fieldNode.desc);
                if (!shouldKeepField(fieldInfo, classCollection)) {
                    iterator.remove();
                    ++fieldsRemoved;
                }
            }
        }
        long timeElapsed = System.currentTimeMillis() - startTime;
        logger.info("Removed {} unused fields in {} ms", fieldsRemoved, timeElapsed);
    }

    private void findUsedFields(List<ClassNode> classNodes) {
        for (ClassNode classNode : classNodes) {
            for (MethodNode methodNode : classNode.methods) {
                for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
                    if (insnNode instanceof FieldInsnNode) {
                        FieldInsnNode fieldInsnNode = (FieldInsnNode) insnNode;
                        usedFields.add(new FieldInfo(fieldInsnNode.name, fieldInsnNode.owner, fieldInsnNode.desc));
                    }
                }
            }
        }
    }

    private boolean shouldKeepField(FieldInfo fieldInfo, ClassCollection classCollection) {
        return usedFields.contains(fieldInfo) || isFieldUsedAsDowncast(fieldInfo, classCollection);
    }

    private boolean isFieldUsedAsDowncast(FieldInfo fieldInfo, ClassCollection classCollection) {
        ClassHierarchy classHierarchy = classCollection.getClassHierarchy(fieldInfo.owner);
        List<ClassHierarchy> children = classHierarchy.getChildren();
        if (children.isEmpty()) {
            return false;
        }
        for (ClassHierarchy child : children) {
            FieldInfo childField = new FieldInfo(fieldInfo.name, child.getName(), fieldInfo.desc);
            if (usedFields.contains(childField)) {
                return true;
            }
            if (isFieldUsedAsDowncast(childField, classCollection)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    private boolean isFieldUsedAsUpcast(FieldInfo fieldInfo, ClassCollection classCollection) {
        ClassHierarchy classHierarchy = classCollection.getClassHierarchy(fieldInfo.owner);
        ClassHierarchy parent = classHierarchy.getParent();
        if (parent == null) {
            return false;
        }
        if (!classHierarchy.getNonJarParent().isEmpty()) {
            return false;
        }
        FieldInfo parentField = new FieldInfo(fieldInfo.name, parent.getName(), fieldInfo.desc);
        if (usedFields.contains(parentField)) {
            return true;
        }
        return isFieldUsedAsUpcast(parentField, classCollection);
    }
}
