package com.kullmar.runemar.updater.deobfuscators;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnreachableCodeRemover implements Deobfuscator {
    private static final Logger logger = LoggerFactory.getLogger(UnreachableCodeRemover.class);
    private int removedInstructions = 0;

    @Override
    public void run(ClassCollection classCollection) {
        for (ClassHierarchy ch : classCollection.getAllClassHierarchies()) {
            ClassNode classNode = ch.getClassNode();
            for (MethodNode method : classNode.methods) {
                if (method.instructions.size() > 0) {
                    Analyzer analyzer = new Analyzer(new BasicInterpreter());
                    try {
                        analyzer.analyze(classNode.name, method);
                        Frame<BasicValue>[] frames = analyzer.getFrames();
                        AbstractInsnNode[] insns = method.instructions.toArray();
                        for (int i = 0; i < frames.length; ++i) {
                            if (frames[i] == null && !(insns[i] instanceof LabelNode)) {
                                method.instructions.remove(insns[i]);
                                removedInstructions++;
                            }
                        }

                    } catch (AnalyzerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        logger.info("Removed {} unreachable instructions", removedInstructions);
    }
}
