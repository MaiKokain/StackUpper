package yuuria.stackupper.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMUtils {
    public static MethodNode fixSlotLimit(MethodNode methodNode)
    {
        for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
            if (abstractInsnNode.getOpcode() == Opcodes.IRETURN) {
                methodNode.instructions.insertBefore(
                        abstractInsnNode,
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "yuuria/stackupper/coremod/Utils",
                                "increaseStackSize",
                                "(I)I"
                        )
                );
            }
        }
        return methodNode;
    }
}
