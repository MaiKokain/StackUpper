/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package yuria.transformerlib;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BuiltinTransformers
{
    //private static final int[]  ICONST_VALUES = new int[] {-1, 0, 1, 2, 3, 4, 5};
    private static Supplier<Integer> globalStackSizeProducer;
    //private static Supplier<Boolean> transferRateBehaviourProducer;


    public static void fixSlotLimitMethodReturnValue(ClassNode classNode, MethodNode methodNode, Map<String, String> parameters)
    {
        for (AbstractInsnNode insn : methodNode.instructions)
        {
            //insert call to increaseStackSizeHelper before every return
            if(insn.getOpcode() == Opcodes.IRETURN)
            {
                if(parameters.containsKey("debug"))
                    methodNode.instructions.insertBefore(insn, createLog("Modified stack limit from " + classNode.name + "." + methodNode.name + " (line " +  findLineNumber(insn) + ")"));

                methodNode.instructions.insertBefore(insn,
                                                     new MethodInsnNode(Opcodes.INVOKESTATIC,
                                                                        "yuria/transformerlib/BuiltinTransformers",
                                                                        "increaseStackSizeHelper",
                                                                        "(I)I"
                                                     )
                );
            }
        }
    }

    @SuppressWarnings("unused")
    public static int increaseStackSizeHelper(int original)
    {
        //Only increase stacksize if original is 64.
        //increasing <64 may break stuff like upgrade slots with low slot limits
        //increasing >64 may increase stack size twice
        if (original == 99)
            return globalStackSizeProducer.get() * (original / 99);
        else
            return original;
    }

    public static void replace64ConstantWithNewStackSize(ClassNode classNode, MethodNode methodNode, Map<String, String> parameters)
    {
        AbstractInsnNode insn = methodNode.instructions.getFirst();

        while(insn != null)
        {
            if(insn.getOpcode() == Opcodes.BIPUSH && ((IntInsnNode)insn).operand == 64)
            {
                if (parameters.containsKey("debug"))
                    methodNode.instructions.insertBefore(insn,
                                                         createLog("Modified 64 constant in " + classNode.name + "." +
                                                                           methodNode.name + " (line " +
                                                                           findLineNumber(insn) + ")")
                    );

                methodNode.instructions.insertBefore(insn,
                                                     new MethodInsnNode(Opcodes.INVOKESTATIC,
                                                                        "yuria/transformerlib/BuiltinTransformers",
                                                                        "globalMaxStackSizeHelper",
                                                                        "()I"
                                                     )
                );

                //save next instruction
                AbstractInsnNode tempNext = insn.getNext();

                //remove the old node
                methodNode.instructions.remove(insn);

                insn = tempNext;
            }
            else
            {
                insn = insn.getNext();
            }
        }
    }

    public static void replaceIntReturnWithNewStack(ClassNode classNode, MethodNode methodNode, Map<String, String> parameters)
    {
        for (AbstractInsnNode insn : methodNode.instructions) {
            if (insn.getOpcode() == Opcodes.IRETURN) {
                methodNode.instructions.insertBefore(insn,
                        new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "yuria/transformerlib/BuiltinTransformers",
                                "globalMaxStackSizeHelper",
                                "()I"
                                )
                        );
            }
        }
//        AbstractInsnNode insn = methodNode.instructions.getFirst();
//        while (insn != null) {
//            if (insn.getOpcode() == Opcodes.IRETURN) {
//                methodNode.instructions.insertBefore(insn,
//                        new MethodInsnNode(
//                                Opcodes.INVOKESTATIC,
//                                "yuria/stackupper/config/StackSize",
//                                "getMaxStackSize",
//                                "()I"
//                        )
//                );
//            }
//        }
    }

    /**
     * Replaces a specific int value with another one, and optionally only replaces some occurrences
     *
     * @param classNode
     * @param methodNode
     * @param parameters
     */
    public static void replaceIntegerConstantWithValue(ClassNode classNode, MethodNode methodNode, Map<String, String> parameters)
    {
        AbstractInsnNode insn = methodNode.instructions.getFirst();

        int           target         = Integer.parseInt(parameters.get("target"));
        Integer       replacement    = Integer.parseInt(parameters.get("replacement"));
        boolean       replaceWithGlobalStackSize = Boolean.parseBoolean(parameters.get("useGlobal"));
        int           currentOrdinal = 0;
        List<Integer> ordinals       = null;

        //e.g. ordinal="1 2 3 7"
        if (parameters.containsKey("ordinal"))
        {
            //split up the ordinal list by spaces or commas and then convert them into integers
            ordinals = Arrays.stream(parameters.get("ordinal").split("[\\s,]+")).map(Integer::parseInt).collect(
                    Collectors.toList());
        }

        while (insn != null)
        {
            if ((insn.getOpcode() == Opcodes.BIPUSH && ((IntInsnNode) insn).operand == target) ||
                        (insn.getOpcode() == Opcodes.LDC && ((LdcInsnNode) insn).cst.equals(target)))
            {
                if (ordinals != null && !ordinals.contains(currentOrdinal))
                {
                    insn = insn.getNext();
                    currentOrdinal++;

                    continue;
                }

                if (parameters.containsKey("debug"))
                    methodNode.instructions.insertBefore(insn,
                                                         createLog(
                                                                 "Modified " + target + " constant to " + (replaceWithGlobalStackSize ? BuiltinTransformers.globalMaxStackSizeHelper() : replacement) +
                                                                         " in " + classNode.name + "." +
                                                                         methodNode.name + " (line " +
                                                                         findLineNumber(insn) + ")")
                    );

                if (replaceWithGlobalStackSize) {
                    methodNode.instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "yuria/transformerlib/BuiltinTransformers", "globalMaxStackSizeHelper", "()I"));
                } else {
                    methodNode.instructions.insertBefore(insn, new LdcInsnNode(replacement));
                }

                //save next instruction
                AbstractInsnNode tempNext = insn.getNext();

                //remove the old node
                methodNode.instructions.remove(insn);

                insn = tempNext;
                currentOrdinal++;
            }
            else
            {
                insn = insn.getNext();
            }
        }
    }
    
    //todo: not sure if this is worth implementing. every transfer rate patch works differently.

//    public static void increaseTransferRate(ClassNode classNode, MethodNode methodNode, Map<String, String> parameters)
//    {
//        final Set<Integer> constants = Arrays.stream(parameters.get("const").split(","))
//                                       .map(Integer::parseInt)
//                                       .collect(Collectors.toSet());
//
//        AbstractInsnNode insn = methodNode.instructions.getFirst();
//
//        while(insn != null)
//        {
//            if(isIntConstant(insn))
//            {
//                int constant = getIntValue(insn);
//
//                if(constants.contains(constant))
//                {
//                    if(parameters.containsKey("debug"))
//                        methodNode.instructions.insertBefore(insn, createLog("Modified transfer rate " + constant + " constant in " + classNode.name + "." + methodNode.name + " (line " +  findLineNumber(insn) + ")"));
//
//                }
//            }
//            else
//            {
//                insn = insn.getNext();
//            }
//        }
//
//    }
//
//    private static boolean isIntConstant(AbstractInsnNode insn)
//    {
//        int op = insn.getOpcode();
//
//        return (op >= Opcodes.ICONST_M1 && op <= Opcodes.ICONST_5) || op == Opcodes.BIPUSH || (insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof Integer);
//    }
//
//    private static int getIntValue(AbstractInsnNode insn)
//    {
//        int op = insn.getOpcode();
//
//        if(op >= Opcodes.ICONST_M1 && op <= Opcodes.ICONST_5)
//            return ICONST_VALUES[op - 2];
//        else if(op == Opcodes.BIPUSH)
//            return ((IntInsnNode) insn).operand;
//        else if(op == Opcodes.LDC)
//            return (int)((LdcInsnNode)insn).cst;
//        else
//            throw new IllegalArgumentException("Insn is not an int constant");
//    }
    
    //@SuppressWarnings("unused")
//    public static int increaseTransferRateHelper(int original)
//    {
//        if(!transferRateBehaviourProducer.get())
//            return original;
//        else
//            return globalStackSizeProducer.get();
//    }
//
    
    public static void setGlobalStackSizeProducer(Supplier<Integer> globalStackSizeProducer)
    {
        BuiltinTransformers.globalStackSizeProducer = globalStackSizeProducer;
    }
    
    private static InsnList createLog(String message)
    {
        InsnList insns = new InsnList();
        
        insns.add(new LdcInsnNode(message));
        
        insns.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                     "yuria/transformerlib/BuiltinTransformers", "logHelper", "(Ljava/lang/String;)V"));
        
        return insns;
    }
    
    //must be public or else it won't be able to be called from other classes (i.e. the ones it is inserted into)
    @SuppressWarnings("unused")
    public static void logHelper(String message)
    {
        TransformerLib.LOGGER.debug(message);
    }
    
    //must also be public
    @SuppressWarnings("unused")
    public static int globalMaxStackSizeHelper()
    {
        return globalStackSizeProducer.get();
    }
    
    private static int findLineNumber(AbstractInsnNode insnNode)
    {
        while(!(insnNode instanceof LineNumberNode) && insnNode != null)
        {
            insnNode = insnNode.getPrevious();
        }
        
        return ((LineNumberNode) Objects.requireNonNull(insnNode)).line;
    }
    
    private static LabelNode findLabel(AbstractInsnNode insnNode)
    {
        while(!(insnNode instanceof LabelNode) && insnNode != null)
        {
            insnNode = insnNode.getPrevious();
        }
        
        return ((LabelNode) Objects.requireNonNull(insnNode));
    }
}
