package yuuria.stackupper.core.utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class PatchConstant64 {
    public static boolean patch(MethodNode method) {
        boolean changed = false;

        for (AbstractInsnNode insn : method.instructions.toArray()) {
            if (!isConstant64(insn)) continue;

            MethodInsnNode call = new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "yuuria/stackupper/StackUpperHelpers",
                    "getMaxStackSize",
                    "()I",
                    false
            );

            method.instructions.set(insn, call);
            changed = true;
        }

        return changed;
    }

    private static boolean isConstant64(AbstractInsnNode insn) {
        if (insn instanceof IntInsnNode) {
            int op = insn.getOpcode();
            if (op != Opcodes.BIPUSH && op != Opcodes.SIPUSH) return false;
            return ((IntInsnNode) insn).operand == 64;
        }
        if (insn instanceof LdcInsnNode) {
            Object cst = ((LdcInsnNode) insn).cst;
            return cst instanceof Integer && (Integer) cst == 64;
        }
        return false;
    }
}
