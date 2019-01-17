package com.kullmar.runemar.updater.asm;

import jdk.internal.org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.objectweb.asm.Opcodes.*;

public class ASMUtils {
    public static void addInterface(ClassNode classNode, Class clazz) {
        String internalName = Type.getInternalName(clazz);
        if (!classNode.interfaces.contains(internalName)) {
            classNode.interfaces.add(internalName);
        }
    }

    public static void addMethods(ClassNode classNode, List<MethodNode> methods) {
        for (MethodNode methodNode : methods) {
            classNode.methods.add(methodNode);
        }
    }

    public static MethodNode createMethod(MethodInfo method, InsnList insnList) {
        MethodNode methodNode = new MethodNode(method.getAccess(), method.getName(), method.getDescriptor(), null,
                null);
        methodNode.instructions = insnList;
        methodNode.maxLocals = 0;
        methodNode.maxStack = 0;
        return methodNode;
    }

    public static boolean isConstructor(String methodName) {
        return methodName.equals("<init>") || methodName.equals("<clinit>");
    }

    public static Set<FieldInfo> findUsedFields(ClassNode classNode, boolean skipConstructor) {
        Set<FieldInfo> usedFields = new HashSet<>();
        for (MethodNode methodNode : classNode.methods) {
            if (skipConstructor && isConstructor(methodNode.name)) {
                continue;
            }
            for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
                if (insnNode instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsnNode = (FieldInsnNode) insnNode;
                    usedFields.add(new FieldInfo(fieldInsnNode.name, fieldInsnNode.owner, fieldInsnNode.desc));
                }
            }
        }
        return usedFields;
    }

    public static MethodNode createGetter(String getterName, FieldInfo fieldInfo) {
        MethodInfo methodInfo = new MethodInfo(getterName, "()" + fieldInfo.desc);
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(ALOAD, 0));
        insnList.add(new FieldInsnNode(GETFIELD, fieldInfo.owner, fieldInfo.name, fieldInfo.desc));
        if (fieldInfo.multiplier != 1) {
            insnList.add(new LdcInsnNode(fieldInfo.multiplier));
            insnList.add(new InsnNode(IMUL));
        }
        insnList.add(new InsnNode(Type.getType(fieldInfo.desc).getOpcode(IRETURN)));
        return createMethod(methodInfo, insnList);
    }

}
