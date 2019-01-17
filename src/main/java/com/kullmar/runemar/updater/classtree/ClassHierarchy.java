package com.kullmar.runemar.updater.classtree;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassHierarchy {
    private ClassNode classNode;
    private ClassHierarchy parent;
    private String nonJarParent = "";
    private List<ClassHierarchy> interfaces = new ArrayList<ClassHierarchy>();
    private List<String> nonJarInterfaces = new ArrayList<>();
    private List<ClassHierarchy> children = new ArrayList<ClassHierarchy>();
    private String realName = "";
    private Map<String, String> deobfuscatedNames = new HashMap<>();

    public ClassHierarchy(byte[] clazz) {
        classNode = new ClassNode();
        ClassReader cr = new ClassReader(clazz);
        cr.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
    }

    public ClassHierarchy(ClassNode cn) {
        classNode = cn;
    }

    public Map<String, String> getDeobfuscatedNames() {
        return deobfuscatedNames;
    }

    public void setDeobfuscatedNames(Map<String, String> deobfuscatedNames) {
        this.deobfuscatedNames = deobfuscatedNames;
    }

    public String getNonJarParent() {
        return nonJarParent;
    }

    public void setNonJarParent(String nonJarParent) {
        this.nonJarParent = nonJarParent;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    public void setClassNode(ClassNode classNode) {
        this.classNode = classNode;
    }

    public String getName() {
        return classNode.name;
    }

    @Override
    public int hashCode() {
        return classNode.hashCode();
    }

    @Override
    public String toString() {
        return classNode.name;
    }

    public List<ClassHierarchy> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<ClassHierarchy> interfaces) {
        this.interfaces = interfaces;
    }

    public ClassHierarchy getParent() {
        return parent;
    }

    public void setParent(ClassHierarchy parent) {
        this.parent = parent;
    }

    public List<ClassHierarchy> getChildren() {
        return children;
    }

    public void setChildren(List<ClassHierarchy> children) {
        this.children = children;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<String> getNonJarInterfaces() {
        return nonJarInterfaces;
    }

    public void setNonJarInterfaces(List<String> nonJarInterfaces) {
        this.nonJarInterfaces = nonJarInterfaces;
    }
}
