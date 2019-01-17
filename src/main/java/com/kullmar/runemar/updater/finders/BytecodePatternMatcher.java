package com.kullmar.runemar.updater.finders;

import org.objectweb.asm.tree.AbstractInsnNode;

public class BytecodePatternMatcher {
    public int run(AbstractInsnNode[] instructions, int[] opCodePattern) {
        for (int i = 0, j = 0; i < instructions.length; ++i) {
            int k = i, l = j;
            while(instructions[k].getOpcode() == opCodePattern[l]) {
                if (++l == opCodePattern.length) return i;
                if (++k == instructions.length) return j;
            }
        }
        return -1;
    }
}
