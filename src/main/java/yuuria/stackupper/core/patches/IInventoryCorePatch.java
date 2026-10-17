package yuuria.stackupper.core.patches;

import org.objectweb.asm.tree.*;
import yuuria.stackupper.core.IStackUpPatch;
import yuuria.stackupper.core.StackUpClassTracker;
import yuuria.stackupper.core.StackUpperTransformer;
import yuuria.stackupper.core.utils.PatchConstant64;

import java.util.Arrays;
import java.util.List;

public class IInventoryCorePatch implements IStackUpPatch {
    private static final String INTERFACE_REQUIRED = "net.minecraft.inventory.IInventory";
    private static final List<String> LIMIT_METHODS = Arrays.asList("getInventoryStackLimit", "func_70297_j_");

    @Override
    public boolean canExecute(ClassNode classNode) { return this.canExecute(classNode, INTERFACE_REQUIRED); }

    @Override
    public boolean canExecute(ClassNode classNode, String tName) {
        return StackUpClassTracker.isImplements(tName, INTERFACE_REQUIRED);
    }

    @Override
    public void execute(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if (!isLimitMethod(method)) continue;

            if (PatchConstant64.patch(method)) {
                StackUpperTransformer.LOGGER.info("Patched {}.{} with IInventoryCorePatch", classNode.name, method.name);
            }
        }
    }

    private boolean isLimitMethod(MethodNode method) {
        if (!"()I".equals(method.desc)) return false;
        return LIMIT_METHODS.contains(method.name);
    }

}