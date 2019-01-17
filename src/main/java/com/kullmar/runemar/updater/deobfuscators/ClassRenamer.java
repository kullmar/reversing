package com.kullmar.runemar.updater.deobfuscators;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

public class ClassRenamer implements Deobfuscator {
    public SimpleRemapper getRemapper(ClassCollection classCollection) {
        Map<String, String> classMappings = new HashMap<>();
        for (ClassHierarchy classHierarchy : classCollection.getAllClassHierarchies()) {
            if (!classHierarchy.getRealName().isEmpty()) {
                classMappings.put(classHierarchy.getName(), classHierarchy.getRealName());
            }
            if (!classHierarchy.getDeobfuscatedNames().isEmpty()) {
                classMappings.putAll(classHierarchy.getDeobfuscatedNames());
            }
        }
        return new SimpleRemapper(classMappings);
    }

    @Override
    public void run(ClassCollection classCollection) {
        SimpleRemapper simpleRemapper = getRemapper(classCollection);
        for (ClassHierarchy ch : classCollection.getAllClassHierarchies()) {
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassRemapper classRemapper = new ClassRemapper(cw, simpleRemapper);
            ch.getClassNode().accept(classRemapper);
            ClassNode cn = new ClassNode();
            ClassReader cr = new ClassReader(cw.toByteArray());
            cr.accept(cn, 0);
            ch.setClassNode(cn);
        }
    }
}
