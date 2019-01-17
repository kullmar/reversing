package com.kullmar.runemar.updater.asm;

public class FieldInfo {
    public String name;
    public String owner;
    public String desc;
    public int multiplier = 1;
    public boolean isStatic = false;

    public FieldInfo(String name, String owner, String desc) {
        this.name = name;
        this.owner = owner;
        this.desc = desc;
    }

    public FieldInfo(String name, String owner, String desc, int multiplier) {
        this(name, owner, desc);
        this.multiplier = multiplier;
    }

    public FieldInfo(String name, String owner, String desc, boolean isStatic) {
        this(name, owner, desc);
        this.isStatic = isStatic;
    }

    public FieldInfo(String name, String owner, String desc, int multiplier, boolean isStatic) {
        this(name, owner, desc, multiplier);
        this.isStatic = isStatic;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldInfo)) {
            return false;
        }
        FieldInfo fieldInfo = (FieldInfo) o;
        return name.equals(fieldInfo.name) && owner.equals(fieldInfo.owner) && desc.equals(fieldInfo.desc);
    }

    @Override
    public int hashCode() {
        return (name + owner + desc).hashCode();
    }

    @Override
    public String toString() {
        return owner + "." + name + " : " + desc;
    }
}
