package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.kullmar.runemar.updater.asm.ASMUtils.findUsedFields;
import static com.kullmar.runemar.rs.RSClassNames.ACTOR;
import static com.kullmar.runemar.rs.RSClassNames.RENDERABLE;
import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;

public class ActorFinder extends ClassFinder {

    public ActorFinder() {
        deobfuscatedName = ACTOR;
    }

    @Override
    protected ClassHierarchy identifyClass(ClassCollection classCollection) {
        ClassHierarchy renderable = classCollection.getClassHierarchyFromRealName(RENDERABLE);
        if (renderable == null) {
            logger.warn("Renderable has not been identified yet. Run RenderableFinder first");
            return null;
        }
        for (ClassHierarchy classHierarchy : renderable.getChildren()) {
            if ((classHierarchy.getClassNode().access & ACC_ABSTRACT) != 0) {
                return classHierarchy;
            }
        }
        return null;
    }

    private void findPotentialCoords() {
        ClassNode actor = identifiedClass.getClassNode();
        List<FieldInfo> intFields = new ArrayList<>();
        for (FieldNode field : actor.fields) {
            if (field.desc.equals("I")) {
                intFields.add(new FieldInfo(field.name, actor.name, field.desc));
            }
        }
        Set<FieldInfo> usedFields = findUsedFields(actor, false);
        List<FieldInfo> potentialMatches = new ArrayList<>();
        for (FieldInfo intField : intFields) {
            if (!usedFields.contains(intField)) {
                potentialMatches.add(intField);
            }
        }
        logger.info("Potential x, y matches: " + potentialMatches.toString());
    }
}
