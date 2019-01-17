package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

import static com.kullmar.runemar.rs.RSClassNames.CACHEABLE_NODE;
import static com.kullmar.runemar.rs.RSClassNames.NODE;

public class CacheableNodeFinder extends ClassFinder {
    public CacheableNodeFinder() {
        deobfuscatedName = CACHEABLE_NODE;
    }

    @Override
    protected ClassHierarchy identifyClass(ClassCollection classCollection) {
        ClassHierarchy node = classCollection.getClassHierarchyFromRealName(NODE);
        if (node == null) {
            logger.warn("Node has not been identified yet. Run NodeFinder first");
            return null;
        }
        List<ClassHierarchy> possibleMatches = node.getChildren();
        for (ClassHierarchy ch : possibleMatches) {
            if (isCacheableNode(ch.getClassNode())) {
                return ch;
            }
        }
        return null;
    }

    private boolean isCacheableNode(ClassNode cn) {
        return hasCorrectFields(cn);
    }

    private boolean hasCorrectFields(ClassNode cn) {
        if (cn.fields.size() != 3) {
            return false;
        }
        int cacheableNodes = 0, longFields = 0;
        String classDesc = "L" + cn.name + ";";
        String longDesc = "J";
        for (FieldNode fn : cn.fields) {
            if (fn.desc.equals(classDesc)) {
                cacheableNodes++;
            }
            else if (fn.desc.equals(longDesc)) {
                longFields++;
            }
        }
        return cacheableNodes == 2 && longFields == 1;
    }
}
