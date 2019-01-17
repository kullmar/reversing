package com.kullmar.runemar.updater.hooks;

import com.kullmar.runemar.updater.asm.FieldInfo;
import com.kullmar.runemar.updater.asm.MethodInfo;

public class FieldHook {
    private FieldInfo fieldInfo;
    private Class itf;
    private MethodInfo methodInfo;
    private boolean isStatic = false;
    private String targetClassName;

    public FieldHook(FieldInfo fieldInfo, Class itf, MethodInfo methodInfo) {
        this.fieldInfo = fieldInfo;
        this.itf = itf;
        this.methodInfo = methodInfo;
        this.targetClassName = fieldInfo.owner;
    }

    public FieldHook(FieldInfo fieldInfo, Class itf, MethodInfo methodInfo, String targetClassName) {
        this.fieldInfo = fieldInfo;
        this.itf = itf;
        this.methodInfo = methodInfo;
        this.isStatic = true;
        this.targetClassName = targetClassName;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public Class getItf() {
        return itf;
    }

    public MethodInfo getMethodInfo() {
        return methodInfo;
    }

    public boolean isStatic() {
        return isStatic;
    }


}
