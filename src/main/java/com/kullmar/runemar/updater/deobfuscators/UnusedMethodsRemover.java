package com.kullmar.runemar.updater.deobfuscators;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class  UnusedMethodsRemover implements Deobfuscator {
    private static final Logger logger = LoggerFactory.getLogger(UnusedMethodsRemover.class);
    private Set<Method> usedMethods;

    public void removeUnusedMethods(ClassCollection classCollection) {
        long startTime = System.currentTimeMillis();
        logger.info("Removing unused methods...");
        int methodsRemovedCount = 0;
        usedMethods = getUsedMethods(classCollection);
        for (ClassHierarchy classHierarchy : classCollection.getAllClassHierarchies()) {
            ClassNode classNode = classHierarchy.getClassNode();
            List<MethodNode> methodNodes = classNode.methods;
            List<MethodNode> newMethodNodes = new ArrayList<MethodNode>();
            for (MethodNode methodNode : methodNodes) {
                String internalName = constructInternalName(classNode.name, methodNode.name);
                Method method = new Method(internalName, methodNode.desc);
                if (!shouldKeepMethod(method, classCollection)) {
                    // logger.info("Removed method {}", method);
                    methodsRemovedCount++;
                    continue;
                }
                newMethodNodes.add(methodNode);
            }
            classNode.methods = newMethodNodes;
        }
        long timeElapsed = System.currentTimeMillis() - startTime;
        logger.info("Removed {} unused methods in {} ms", methodsRemovedCount, timeElapsed);
    }

    private boolean shouldKeepMethod(Method method, ClassCollection classCollection) {
        String internalMethodName = method.getName();
        String methodName = getMethodNameFromInternal(internalMethodName);
        ClassHierarchy clazz = classCollection.getClassHierarchyFromInternal(internalMethodName);
        return usedMethods.contains(method) || methodName.equals("<clinit>") || !isObfuscated(methodName) ||
            methodCallRefersInterface(method, classCollection) || methodCallRefersParent(method, classCollection) ||
                methodCallRefersChild(method, classCollection);
    }

    private boolean methodCallRefersInterface(Method method, ClassCollection classCollection) {
        String internalMethodName = method.getName();
        ClassHierarchy owner = classCollection.getClassHierarchyFromInternal(internalMethodName);
        List<ClassHierarchy> interfaces = owner.getInterfaces();
        if (interfaces.isEmpty()) {
            return false;
        }
        String methodName = getMethodNameFromInternal(internalMethodName);
        String desc = method.getDescriptor();
        for (ClassHierarchy itf : interfaces) {
            ClassNode classNode = itf.getClassNode();
            List<MethodNode> methodNodes = classNode.methods;
            for (MethodNode methodNode : methodNodes) {
                if (methodNode.name.equals(methodName) && methodNode.desc.equals(desc) &&
                        usedMethods.contains(new Method(constructInternalName(classNode.name, methodName), desc))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean methodCallRefersParent(Method method, ClassCollection classCollection) {
        String internalMethodName = method.getName();
        ClassHierarchy owner = classCollection.getClassHierarchyFromInternal(internalMethodName);
        ClassHierarchy parent = owner.getParent();
        if (parent == null) {
            return false;
        }
        String methodName = getMethodNameFromInternal(internalMethodName);
        String desc = method.getDescriptor();
        ClassNode parentNode = parent.getClassNode();
        List<MethodNode> methodNodes = parentNode.methods;
        Method parentMethod = new Method(constructInternalName(parent.getName(), methodName), desc);
        for (MethodNode methodNode : methodNodes) {
            if (methodNode.name.equals(methodName) && methodNode.desc.equals(desc) &&
                    usedMethods.contains(parentMethod)) {
                return true;
            }
        }
        return methodCallRefersParent(parentMethod, classCollection);
    }

    private boolean methodCallRefersChild(Method method, ClassCollection classCollection) {
        String internalMethodName = method.getName();
        List<ClassHierarchy> children = classCollection.getClassHierarchyFromInternal(internalMethodName).getChildren();
        if (children.isEmpty()) {
            return false;
        }
        for (ClassHierarchy child : children) {
            String methodName = getMethodNameFromInternal(internalMethodName);
            String childMethodName = constructInternalName(child.getName(), methodName);
            Method childMethod = new Method(childMethodName, method.getDescriptor());
            if (usedMethods.contains(childMethod)) {
                return true;
            }
            if (methodCallRefersChild(childMethod, classCollection)) {
                return true;
            }
        }
        return false;
    }

    private String getMethodNameFromInternal(String internalMethodName) {
        String[] splitName = internalMethodName.split("/");
        return splitName[splitName.length - 1];
    }

    private boolean isObfuscated(String name) {
        return name.length() < 3;
    }

    private Set<Method> getUsedMethods(ClassCollection classCollection) {
        Set<Method> usedMethods = new HashSet<Method>();
        for (ClassHierarchy classHierarchy : classCollection.getAllClassHierarchies()) {
            ClassNode classNode = classHierarchy.getClassNode();
            for (MethodNode methodNode : classNode.methods) {
                InsnList insnList = methodNode.instructions;
                Iterator<AbstractInsnNode> iterator = insnList.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode instruction = iterator.next();
                    if (instruction instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) instruction;
                        String internalName = constructInternalName(methodInsnNode.owner, methodInsnNode.name);
                        Method method = new Method(internalName, methodInsnNode.desc);
                        usedMethods.add(method);
                    }
                }
            }
        }
        return usedMethods;
    }

    private String constructInternalName(String className, String methodName) {
        return className + "/" + methodName;
    }


    @Override
    public void run(ClassCollection classCollection) {
        removeUnusedMethods(classCollection);
    }
}
