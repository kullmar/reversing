package com.kullmar.runemar.updater.finders.fieldfinders;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.kullmar.runemar.rs.RSClassNames.GAME_ENGINE;

public class GameEngineMembersFinder implements FieldFinder {
    public static final String GAME_CANVAS = "gameCanvas";
    private ClassNode gameEngine;

    @Override
    public Map<String, FieldInfo> run(ClassCollection classCollection) {
        Map<String, FieldInfo> mapping = new HashMap<>();
        gameEngine = classCollection.getClassHierarchyFromRealName(GAME_ENGINE).getClassNode();
        mapping.put(GAME_CANVAS, findCanvas());
        return mapping;
    }

    private FieldInfo findCanvas() {
        String canvasDesc = Type.getDescriptor(Canvas.class);
        for (FieldNode fieldNode : gameEngine.fields) {
            if (fieldNode.desc.equals(canvasDesc)) {
                return new FieldInfo(fieldNode.name, gameEngine.name, canvasDesc);
            }
        }
        throw new IllegalStateException("Could not find Canvas member in " + GAME_ENGINE);
    }
}
