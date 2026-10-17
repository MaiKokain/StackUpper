package yuuria.stackupper.core.patches;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import yuuria.stackupper.core.IStackUpPatch;
import yuuria.stackupper.core.StackUpClassTracker;
import yuuria.stackupper.core.StackUpperTransformer;
import yuuria.stackupper.core.utils.PatchConstant64;

import java.util.Arrays;
import java.util.List;

public class SlotCorePatch implements IStackUpPatch {
    private static final String TARGET = "net.minecraft.inventory.Slot";
    private static final List<String> TARGET_METHODS = Arrays.asList("getItemStackLimit", "func_178170_b", "getSlotStackLimit", "func_75219_a");
    @Override
    public boolean canExecute(ClassNode classNode) {
        return this.canExecute(classNode, TARGET);
    }

    @Override
    public boolean canExecute(ClassNode classNode, String tName) {
        return StackUpClassTracker.isExtends(tName, TARGET);
    }

    @Override
    public void execute(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            if (TARGET_METHODS.contains(methodNode.name)) {
                if (PatchConstant64.patch(methodNode)) {
                    StackUpperTransformer.LOGGER.info("Patched {}.{} with SlotCorePatch", classNode.name, methodNode.name);
                }
            }
        }
    }
}
