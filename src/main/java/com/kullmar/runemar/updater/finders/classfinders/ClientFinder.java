package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import com.kullmar.runemar.updater.finders.BytecodePatternMatcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

import static com.kullmar.runemar.rs.RSClassNames.CLIENT;

public class ClientFinder extends ClassFinder {
    private int revision;

    public ClientFinder() {
        deobfuscatedName = CLIENT;
    }

    @Override
    protected ClassHierarchy identifyClass(ClassCollection classCollection) {
        ClassHierarchy client = null;
        for (ClassHierarchy classHierarchy : classCollection.getAllClassHierarchies()) {
            if (classHierarchy.getName().equals("client")) {
                client = classHierarchy;
                break;
            }
        }
        return client;
    }

    @Override
    public void analyzeClass() {
        identifyRevision();
    }

    private void identifyRevision() {
        int[] pattern = new int[]{Opcodes.ALOAD, Opcodes.SIPUSH, Opcodes.SIPUSH, Opcodes.SIPUSH};
        ClassNode classNode = identifiedClass.getClassNode();
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("init") && methodNode.desc.equals("()V")) {
                int i = new BytecodePatternMatcher().run(methodNode.instructions.toArray(), pattern);
                if (i == -1) {
                    logger.info("Could not find revision");
                    return;
                }
                IntInsnNode revision = (IntInsnNode) methodNode.instructions.get(i + 3);
                this.revision = revision.operand;
                logger.info("Revision: " + revision.operand);
            }
        }
    }
}
