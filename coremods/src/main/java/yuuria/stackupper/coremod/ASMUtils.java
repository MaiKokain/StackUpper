package yuuria.stackupper.coremod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ASMUtils {
    public static MethodNode fixSlotLimit(MethodNode methodNode)
    {
        for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
            if (abstractInsnNode.getOpcode() == Opcodes.IRETURN) {
//                if (methodNode.)
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

    public static MethodNode returnI32(MethodNode methodNode)
    {
        for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
            if (abstractInsnNode.getOpcode() == Opcodes.IRETURN) {
                methodNode.instructions.insertBefore(
                        abstractInsnNode,
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "yuuria/stackupper/coremod/Utils",
                                "return32I",
                                "()I"
                        )
                );
            }
        }
        return methodNode;
    }

    public static MethodNode replace64(MethodNode methodNode)
    {
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

    public static ClassNode replaceClass(ClassNode classNode)
    {
        for (MethodNode methodNode : classNode.methods) {
            AbstractInsnNode insnNode = methodNode.instructions.getFirst();

            while (insnNode != null) {
                if (insnNode.getOpcode() == Opcodes.BIPUSH && (((IntInsnNode) insnNode).operand == 64 || ((IntInsnNode) insnNode).operand == 99)) {
                    SUCoreMod.logger.info("replacing bipush 64 from class");
                    methodNode.instructions.insertBefore(
                            insnNode,
                            new LdcInsnNode(Integer.MAX_VALUE)
                    );

                    AbstractInsnNode next = insnNode.getNext();
                    methodNode.instructions.remove(insnNode);
                    insnNode = next;
                } else {
                    insnNode = insnNode.getNext();
                }
            }
        }

        return classNode;
    }
}
