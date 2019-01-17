package com.kullmar.runemar.updater.injection;

import com.kullmar.runemar.updater.asm.MethodInfo;
import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.hooks.FieldHook;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;

import static com.kullmar.runemar.updater.asm.ASMUtils.createMethod;
import static org.objectweb.asm.Opcodes.*;

public class FieldGetter {
    public void inject(ClassCollection classCollection, List<FieldHook> fieldHooks) {
        for (FieldHook fieldHook : fieldHooks) {
            ClassNode classToInject = classCollection.getClassHierarchy(fieldHook.getTargetClassName()).getClassNode();
            if (!verifyInterfaceMethod(fieldHook.getMethodInfo(), fieldHook.getItf())) {
                throw new IllegalStateException("Method to be injected does not match interface method");
            }
            injectFieldGetter(classToInject, fieldHook);
        }
    }

    private void injectFieldGetter(ClassNode classToInject, FieldHook fieldHook) {
        MethodNode method = createGetter(fieldHook);
        String itfInternalName = Type.getInternalName(fieldHook.getItf());
        if (!classToInject.interfaces.contains(itfInternalName)) {
            classToInject.interfaces.add(itfInternalName);
        }
        classToInject.methods.add(method);
    }

    private MethodNode createGetter(FieldHook fieldHook) {
        MethodInfo methodInfo = new MethodInfo(fieldHook.getMethodInfo().getName(),
                fieldHook.getMethodInfo().getDescriptor());
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(ALOAD, 0));
        int fieldInsnOpCode = fieldHook.isStatic() ? GETSTATIC : GETFIELD;
        insnList.add(new FieldInsnNode(fieldInsnOpCode, fieldHook.getFieldInfo().owner, fieldHook.getFieldInfo().name,
                fieldHook.getFieldInfo().desc));
        if (fieldHook.getFieldInfo().multiplier != 1) {
            insnList.add(new LdcInsnNode(fieldHook.getFieldInfo().multiplier));
            insnList.add(new InsnNode(IMUL));
        }
        insnList.add(new InsnNode(Type.getType(fieldHook.getFieldInfo().desc).getOpcode(IRETURN)));
        return createMethod(methodInfo, insnList);
    }

    private boolean verifyInterfaceMethod(MethodInfo methodInfo, Class itf) {
        return true;
    }
}
