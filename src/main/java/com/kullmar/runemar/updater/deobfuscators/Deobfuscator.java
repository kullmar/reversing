package com.kullmar.runemar.updater.deobfuscators;

import com.kullmar.runemar.updater.classtree.ClassCollection;

public interface Deobfuscator {
    void run(ClassCollection classCollection);
}
