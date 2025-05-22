package yuuria.stackupper.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
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

    public static MethodNode replace64(MethodNode methodNode)
    {
//        for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
//            if (abstractInsnNode.getOpcode() == Opcodes.BIPUSH && ((IntInsnNode) abstractInsnNode).operand == 64)
//            {
//                methodNode.instructions.insertBefore(
//                        abstractInsnNode,
//                        new MethodInsnNode(
//                                Opcodes.INVOKESTATIC,
//                                "yuuria/stackupper/coremod/Utils",
//                                "getGlobalMaxStackSizeProducer",
//                                "()I"
//                        )
//                );
//
//            }
//        }
//        return methodNode;

        AbstractInsnNode insnNode = methodNode.instructions.getFirst();

        while (insnNode != null)
        {
            if (insnNode.getOpcode() == Opcodes.BIPUSH && ((IntInsnNode) insnNode).operand == 64)
            {
                SUCoreMod.logger.info("replacing bipush 64");
                methodNode.instructions.insertBefore(
                        insnNode,
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "yuuria/stackupper/coremod/Utils",
                                "getGlobalMaxStackSizeProducer",
                                "()I"
                        )
                );

                AbstractInsnNode next = insnNode.getNext();

                methodNode.instructions.remove(insnNode);

                insnNode = next;
            } else {
                insnNode = insnNode.getNext();
            }
        }
        return methodNode;
    }
}
