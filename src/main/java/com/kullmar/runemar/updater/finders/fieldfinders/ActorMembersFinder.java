package com.kullmar.runemar.updater.finders.fieldfinders;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

import static com.kullmar.runemar.rs.RSClassNames.ACTOR;

public class ActorMembersFinder implements FieldFinder {
    public static final String ACTOR_X = "actorX";
    public static final String ACTOR_Y = "actorY";

    private ClassNode actor;

    @Override
    public Map<String, FieldInfo> run(ClassCollection classCollection) {
        Map<String, FieldInfo> mapping = new HashMap<>();
        actor = classCollection.getClassHierarchyFromRealName(ACTOR).getClassNode();
        mapping.put(ACTOR_X, findX());
        mapping.put(ACTOR_Y, findY());
        return mapping;
    }

    private FieldInfo findX() {
        return new FieldInfo("ae", actor.name, "I", -24202613);
    }

    private FieldInfo findY() {
        return new FieldInfo("ac", actor.name, "I", 2109067129);
    }
}
