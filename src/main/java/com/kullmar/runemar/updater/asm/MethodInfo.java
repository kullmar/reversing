package com.kullmar.runemar.updater.asm;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class MethodInfo extends org.objectweb.asm.commons.Method {
    private int access;

    public MethodInfo(String name, String descriptor) {
        super(name, descriptor);
        access = ACC_PUBLIC;
    }

    public MethodInfo(String name, String descriptor, int access) {
        super(name, descriptor);
        this.access = access;
    }

    public int getAccess() {
        return access;
    }
}
