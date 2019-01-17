package com.kullmar.runemar.updater.deobfuscators;

import com.kullmar.runemar.updater.classtree.ClassCollection;
import com.kullmar.runemar.updater.classtree.ClassHierarchy;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.tree.AbstractInsnNode.*;

@Deprecated
public class DuplicateMethodsRemover implements Deobfuscator {
    private static final Logger logger = LoggerFactory.getLogger(DuplicateMethodsRemover.class);

    public void findDuplicateMethods(ClassCollection classCollection) {
        for (ClassHierarchy ch : classCollection.getAllClassHierarchies()) {
            if ((ch.getClassNode().access & (ACC_ABSTRACT + ACC_INTERFACE)) != 0) {
                continue;
            }
            ClassNode cn = ch.getClassNode();
            findDuplicateMethods(cn);
        }
    }

    public Map<MethodNode, MethodNode> findDuplicateMethods(ClassNode cn) {
        if (cn.name.equals("gy")) {
            logger.info("");
        }
        Map<MethodNode, MethodNode> duplicateMethods = new HashMap<>();
        for (int i = 0; i < cn.methods.size(); ++i) {
            MethodNode mn1 = cn.methods.get(i);
            for (int j = i + 1; j < cn.methods.size(); ++j) {
                MethodNode mn2 = cn.methods.get(j);
                if (methodEquals(mn1, mn2)) {
                    logger.info("Found duplicate methods in class {}: {}", cn.name, mn2.name + mn2.desc);
                }
            }
        }
        return duplicateMethods;
    }

    private boolean methodEquals(MethodNode mn1, MethodNode mn2) {
        if (mn1.access != mn2.access || !mn1.desc.equals(mn2.desc)) {
            return false;
        }
        InsnList insnList1 = mn1.instructions;
        InsnList insnList2 = mn2.instructions;
        if (insnList1.size() != insnList2.size()) {
            return false;
        }
        ListIterator<AbstractInsnNode> iterator1 = insnList1.iterator();
        ListIterator<AbstractInsnNode> iterator2 = insnList2.iterator();
        while (iterator1.hasNext()) {
            AbstractInsnNode insn1 = iterator1.next();
            AbstractInsnNode insn2 = iterator2.next();
            if (!instructionEquals(insn1, insn2)) {
                return false;
            }
        }
        return true;
    }

    private boolean instructionEquals(AbstractInsnNode insn1, AbstractInsnNode insn2) {
        if (insn1.getOpcode() != insn2.getOpcode()) {
            return false;
        }
        switch (insn1.getType()) {
            case FIELD_INSN:
                FieldInsnNode fieldInsn1 = (FieldInsnNode) insn1;
                FieldInsnNode fieldInsn2 = (FieldInsnNode) insn2;
                return fieldInsn1.desc.equals(fieldInsn2.desc) && fieldInsn1.name.equals(fieldInsn2.name) &&
                        fieldInsn1.owner.equals(fieldInsn2.owner);
//            case FRAME:
//                FrameNode frameInsn1 = (FrameNode) insn1;
//                FrameNode frameInsn2 = (FrameNode) insn2;
//                return frameInsn1.local.equals(frameInsn2.local) && frameInsn1.stack.equals(frameInsn2.stack);
            case IINC_INSN:
                IincInsnNode iincInsn1 = (IincInsnNode) insn1;
                IincInsnNode iincInsn2 = (IincInsnNode) insn2;
                return iincInsn1.var == iincInsn2.var && iincInsn1.incr == iincInsn2.incr;
            case INVOKE_DYNAMIC_INSN:
                InvokeDynamicInsnNode invokeDynamicInsn1 = (InvokeDynamicInsnNode) insn1;
                InvokeDynamicInsnNode invokeDynamicInsn2 = (InvokeDynamicInsnNode) insn2;
                return invokeDynamicInsn1.bsm.equals(invokeDynamicInsn2.bsm) && invokeDynamicInsn1.bsmArgs.equals(
                        invokeDynamicInsn2.bsmArgs) && invokeDynamicInsn1.desc.equals(invokeDynamicInsn2.desc) &&
                        invokeDynamicInsn1.name.equals(invokeDynamicInsn2.name);
//            case JUMP_INSN:
//                JumpInsnNode jumpInsn1 = (JumpInsnNode) insn1;
//                JumpInsnNode jumpInsn2 = (JumpInsnNode) insn2;
//                return jumpInsn1.label.getLabel().toString().equals(jumpInsn2.label.getLabel().toString());
//            case LABEL:
//                LabelNode labelInsn1 = (LabelNode) insn1;
//                LabelNode labelInsn2 = (LabelNode) insn2;
//                return labelInsn1.getLabel().toString().equals(labelInsn2.getLabel().toString());
            case LDC_INSN:
                LdcInsnNode ldcInsn1 = (LdcInsnNode) insn1;
                LdcInsnNode ldcInsn2 = (LdcInsnNode) insn2;
                return ldcInsn1.cst.equals(ldcInsn2.cst);
//            case LINE:
//                LineNumberNode lineInsn1 = (LineNumberNode) insn1;
//                LineNumberNode lineInsn2 = (LineNumberNode) insn2;
//                return lineInsn1.line == lineInsn2.line;
            case LOOKUPSWITCH_INSN:
                LookupSwitchInsnNode lookupInsn1 = (LookupSwitchInsnNode) insn1;
                LookupSwitchInsnNode lookupInsn2 = (LookupSwitchInsnNode) insn2;
                return lookupInsn1.keys.equals(lookupInsn2.keys);
            case METHOD_INSN:
                MethodInsnNode methodInsn1 = (MethodInsnNode) insn1;
                MethodInsnNode methodInsn2 = (MethodInsnNode) insn2;
                return methodInsn1.desc.equals(methodInsn2.desc) && methodInsn1.name.equals(methodInsn2.name) &&
                        methodInsn1.owner.equals(methodInsn2.owner);
            case MULTIANEWARRAY_INSN:
                MultiANewArrayInsnNode multiANewArrayInsn1 = (MultiANewArrayInsnNode) insn1;
                MultiANewArrayInsnNode multiANewArrayInsn2 = (MultiANewArrayInsnNode) insn2;
                return multiANewArrayInsn1.desc.equals(multiANewArrayInsn2.desc) && multiANewArrayInsn1.dims ==
                        multiANewArrayInsn2.dims;
            case TABLESWITCH_INSN:
                return true;
            case TYPE_INSN:
                TypeInsnNode typeInsn1 = (TypeInsnNode) insn1;
                TypeInsnNode typeInsn2 = (TypeInsnNode) insn2;
                return typeInsn1.desc.equals(typeInsn2.desc);
            case VAR_INSN:
                VarInsnNode varInsn1 = (VarInsnNode) insn1;
                VarInsnNode varInsn2 = (VarInsnNode) insn2;
                return varInsn1.var == varInsn2.var;
            default:
                return true;
        }
    }

    @Override
    public void run(ClassCollection classCollection) {
        findDuplicateMethods(classCollection);
    }
}
