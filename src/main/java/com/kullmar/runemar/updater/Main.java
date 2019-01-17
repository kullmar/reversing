package com.kullmar.runemar.updater;

import com.kullmar.runemar.api.RSName;
import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import com.kullmar.runemar.updater.deobfuscators.MasterDeobfuscator;
import com.kullmar.runemar.graphics.ObservableCanvas;
import com.kullmar.runemar.rs.RSDownloader;
import com.kullmar.runemar.tbd.JarUtils;
import com.kullmar.runemar.updater.finders.classfinders.MasterClassFinder;
import com.kullmar.runemar.updater.finders.fieldfinders.MasterFieldFinder;
import com.kullmar.runemar.updater.hooks.Hooks;
import com.kullmar.runemar.updater.hooks.hookgenerators.MasterHookGenerator;
import com.kullmar.runemar.updater.injection.FieldGetter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.Map;

import static com.kullmar.runemar.rs.RSClassNames.NAME;

public class Main {
    public static void main(String[] args) throws IOException {
        // new RSDownloader().downloadClient();
        ClassCollection classCollection = new ClassCollection(RSDownloader.getGamepackLocation());
        MasterDeobfuscator masterDeobfuscator = new MasterDeobfuscator();
        masterDeobfuscator.run(classCollection);
        MasterClassFinder masterAnalyzer = new MasterClassFinder();
        masterAnalyzer.run(classCollection);
        MasterFieldFinder masterFieldFinder = new MasterFieldFinder();
        masterFieldFinder.run(classCollection);
        MasterHookGenerator masterHookGenerator = new MasterHookGenerator();
        Hooks hooks = masterHookGenerator.run(masterFieldFinder.getFoundFields());
        FieldGetter fieldGetter = new FieldGetter();
        fieldGetter.inject(classCollection, hooks.getFieldHooks());
        replaceCanvas(classCollection);
        addRsNameInterface(classCollection.getClassHierarchyFromRealName(NAME).getClassNode());
        Map<String, byte[]> transformedClasses = classCollection.generateClasses();
        JarUtils.writeNewJar(transformedClasses, "transformed.jar");
    }

    private static void replaceCanvas(ClassCollection classCollection) {
        SimpleRemapper remapper = new SimpleRemapper("java/awt/Canvas", Type.getInternalName(ObservableCanvas.class));
        for (ClassHierarchy ch : classCollection.getAllClassHierarchies()) {
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassRemapper classRemapper = new ClassRemapper(cw, remapper);
            ch.getClassNode().accept(classRemapper);
            ClassNode cn = new ClassNode();
            ClassReader cr = new ClassReader(cw.toByteArray());
            cr.accept(cn, 0);
            ch.setClassNode(cn);
        }
    }

    private static void addRsNameInterface(ClassNode name) {
        name.interfaces.add(Type.getInternalName(RSName.class));
    }
}
