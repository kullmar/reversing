package com.kullmar.runemar.updater.finders.fieldfinders;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.kullmar.runemar.rs.RSClassNames.PLAYER;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

public class LocalPlayerFinder implements FieldFinder {
    public static final String LOCAL_PLAYER = "localPlayer";

    private static final Logger logger = LoggerFactory.getLogger(LocalPlayerFinder.class);

    public Map<String, FieldInfo> run(ClassCollection classCollection) {
        ClassHierarchy player = classCollection.getClassHierarchyFromRealName(PLAYER);
        if (player == null) {
            throw new IllegalStateException(PLAYER + " not found");
        }
        String playerDesc = "L" + player.getName() + ";";
        for (ClassNode classNode : classCollection.getAllClassNodes()) {
            for (FieldNode fieldNode : classNode.fields) {
                if (fieldNode.desc.equals(playerDesc) && (fieldNode.access & ACC_STATIC) != 0) {
                    logger.info("Found localPlayer: " + classNode.name + "." + fieldNode.name);
                    FieldInfo localPlayer = new FieldInfo(fieldNode.name, classNode.name, playerDesc, true);
                    Map<String, FieldInfo> mapping = new HashMap();
                    mapping.put(LOCAL_PLAYER, localPlayer);
                    return mapping;
                }
            }
        }
        throw new IllegalStateException(LOCAL_PLAYER + " not found");
    }
}
