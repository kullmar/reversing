package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class ClassFinder {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected String deobfuscatedName;
    protected ClassHierarchy identifiedClass;
    protected Map<String, String> identifiedNames = new HashMap<>();

    public ClassHierarchy getIdentifiedClass() {
        return identifiedClass;
    }

    protected void updateClassDescription() {
        identifiedClass.setRealName(deobfuscatedName);
        identifiedClass.setDeobfuscatedNames(identifiedNames);
    }

    protected abstract ClassHierarchy identifyClass(ClassCollection classCollection);

    protected void analyzeClass() {
    }

    public void run(ClassCollection classCollection) {
        ClassHierarchy ch = identifyClass(classCollection);
        if (ch == null) {
            logger.warn("Unable to identify {}", deobfuscatedName);
            return;
        }
        identifiedClass = ch;
        logger.info("{} found: {}", deobfuscatedName, ch.getName());
        updateClassDescription();
        analyzeClass();
        identifiedClass = ch;
    }
}
