package com.kullmar.runemar.updater.deobfuscators;

import com.kullmar.runemar.updater.classtree.ClassCollection;

import java.util.ArrayList;
import java.util.List;

public class MasterDeobfuscator implements Deobfuscator {
    private List<Deobfuscator> deobfuscators = new ArrayList<>();

    public MasterDeobfuscator() {
        deobfuscators.add(new UnusedMethodsRemover());
        deobfuscators.add(new UnusedFieldsRemover());
        // deobfuscators.add(new RuntimeExceptionRemover());
        // deobfuscators.add(new ClassRenamer());
    }

    @Override
    public void run(ClassCollection classCollection) {
        for (Deobfuscator deobfuscator : deobfuscators) {
            deobfuscator.run(classCollection);
        }
    }
}
