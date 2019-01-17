package com.kullmar.runemar.updater.deobfuscators;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

public class RuntimeExceptionRemover implements Deobfuscator {
    private static final Logger logger = LoggerFactory.getLogger(RuntimeExceptionRemover.class);

    @Override
    public void run(ClassCollection classCollection) {
        int blocksRemoved = 0;
        for (ClassHierarchy classHierarchy : classCollection.getAllClassHierarchies()) {
            ClassNode classNode = classHierarchy.getClassNode();
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.tryCatchBlocks.isEmpty()) {
                    continue;
                }
                Collection<TryCatchBlockNode> runtimeExceptions = new ArrayList<>();
                for (TryCatchBlockNode tryCatchBlockNode : methodNode.tryCatchBlocks) {
                    if (tryCatchBlockNode.type != null && !tryCatchBlockNode.type.equals("java/lang/RuntimeException")) {
                        continue;
                    }
                    runtimeExceptions.add(tryCatchBlockNode);
                    ++blocksRemoved;
                }
                methodNode.tryCatchBlocks.removeAll(runtimeExceptions);
            }
        }
        logger.info("Removed {} RuntimeException blocks", blocksRemoved);
    }
}
