package yuuria.stackupper.core;

import org.objectweb.asm.tree.ClassNode;

public interface IStackUpPatch {
    boolean canExecute(ClassNode classNode);

    boolean canExecute(ClassNode classNode, String tName);

    void execute(ClassNode classNode);
}