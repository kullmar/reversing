package com.kullmar.runemar.updater.finders.fieldfinders;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

import static com.kullmar.runemar.rs.RSClassNames.NAME;
import static com.kullmar.runemar.rs.RSClassNames.PLAYER;

public class PlayerMembersFinder implements FieldFinder {
    public static final String PLAYER_NAME = "playerName";

    private ClassNode player;
    private ClassNode rsName;

    @Override
    public Map<String, FieldInfo> run(ClassCollection classCollection) {
        Map<String, FieldInfo> mapping = new HashMap<>();
        player = classCollection.getClassHierarchyFromRealName(PLAYER).getClassNode();
        rsName = classCollection.getClassHierarchyFromRealName(NAME).getClassNode();
        mapping.put(PLAYER_NAME, findName());
        return mapping;
    }

    private FieldInfo findName() {
        return new FieldInfo("g", player.name, "L" + rsName.name + ";");
    }
}
