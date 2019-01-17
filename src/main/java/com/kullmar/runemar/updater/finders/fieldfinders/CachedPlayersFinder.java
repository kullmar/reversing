package com.kullmar.runemar.updater.finders.fieldfinders;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.HashMap;
import java.util.Map;

import static com.kullmar.runemar.rs.RSClassNames.PLAYER;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

public class CachedPlayersFinder implements FieldFinder {
    public static final String CACHED_PLAYERS = "cachedPlayers";

    private ClassNode client;
    private ClassNode player;

    @Override
    public Map<String, FieldInfo> run(ClassCollection classCollection) {
        client = classCollection.getClassHierarchy("client").getClassNode();
        player = classCollection.getClassHierarchyFromRealName(PLAYER).getClassNode();
        Map<String, FieldInfo> mapping = new HashMap<>();
        mapping.put(CACHED_PLAYERS, findCachedPlayers());
        return mapping;
    }

    private FieldInfo findCachedPlayers() {
        String descriptor = "[L" + player.name + ";";
        for (FieldNode fieldNode : client.fields) {
            if (fieldNode.desc.equals(descriptor) && (fieldNode.access & ACC_STATIC) != 0) {
                return new FieldInfo(fieldNode.name, client.name, descriptor, true);
            }
        }
        throw new IllegalStateException("Could not find " + CACHED_PLAYERS);
    }
}
