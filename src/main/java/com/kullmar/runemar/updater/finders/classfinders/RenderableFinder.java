package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.ArrayList;
import java.util.List;

import static com.kullmar.runemar.rs.RSClassNames.CACHEABLE_NODE;
import static com.kullmar.runemar.rs.RSClassNames.RENDERABLE;
import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class RenderableFinder extends ClassFinder {
    public RenderableFinder() {
        deobfuscatedName = RENDERABLE;
    }

    @Override
    protected ClassHierarchy identifyClass(ClassCollection classCollection) {
        ClassHierarchy cacheableNode = classCollection.getClassHierarchyFromRealName(CACHEABLE_NODE);
        if (cacheableNode == null) {
            logger.warn("CacheableNode has not been identified yet. Run CacheableNodeFinder first");
            return null;
        }
        List<ClassHierarchy> possibleMatches = new ArrayList<>();
        for (ClassHierarchy classHierarchy : cacheableNode.getChildren()) {
            if ((classHierarchy.getClassNode().access & ACC_ABSTRACT) != 0) {
                possibleMatches.add(classHierarchy);
            }
        }
        for (ClassHierarchy classHierarchy : possibleMatches) {
            if (isRenderable(classHierarchy.getClassNode())) {
                return classHierarchy;
            }
        }
        return null;
    }

    private boolean isRenderable(ClassNode classNode) {
        return hasCorrectFields(classNode);
    }

    private boolean hasCorrectFields(ClassNode classNode) {
        List<FieldNode> fields = classNode.fields;
        if (fields.size() != 1) {
            return false;
        }
        FieldNode fieldNode = fields.get(0);
        return (fieldNode.access & ACC_PUBLIC) != 0 && fieldNode.desc.equals("I");
    }
}
