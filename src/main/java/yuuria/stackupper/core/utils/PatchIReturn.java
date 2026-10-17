package yuuria.stackupper.core.utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

// force patches IRETURN Invoke immediately
public class PatchIReturn {
    public static boolean patch(MethodNode methodNode)
    {
        boolean changed = false;
        MethodInsnNode call = new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "yuuria/stackupper/StackUpperHelpers",
                "getMaxStackSize",
                "()I",
                false
        );

        for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
            if (insnNode.getOpcode() == Opcodes.IRETURN) {
                methodNode.instructions.insertBefore(insnNode, new InsnNode(Opcodes.POP));
                methodNode.instructions.insertBefore(insnNode, call.clone(null));
                changed = true;
            }
        }
        return changed;
    }
}
