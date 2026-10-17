package yuuria.stackupper.core.patches;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import yuuria.stackupper.core.IStackUpPatch;
import yuuria.stackupper.core.StackUpperTransformer;
import yuuria.stackupper.core.utils.PatchIReturn;

import java.util.Arrays;
import java.util.List;

public class IReturnCorePatch implements IStackUpPatch {
    private static final List<String> STATIC_CLASS = Arrays.asList("slimeknights.mantle.tileentity.TileInventory");
    private static final List<String> LIMIT_METHODS = Arrays.asList("getInventoryStackLimit", "func_70297_j_");

    @Override
    public boolean canExecute(ClassNode classNode) {
        return STATIC_CLASS.contains(classNode.name.replace('/', '.'));
    }

    @Override
    public boolean canExecute(ClassNode classNode, String tName) {
        return STATIC_CLASS.contains(tName);
    }

    @Override
    public void execute(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            if (!isLimitMethod(methodNode)) continue;
            if (PatchIReturn.patch(methodNode)) {
                StackUpperTransformer.LOGGER.info("Patched {}.{} with IReturnCorePatch", classNode.name, methodNode.name);
            }
        }
    }

    private boolean isLimitMethod(MethodNode method) {
//        if (!"()I".equals(method.desc)) return false;
        return LIMIT_METHODS.contains(method.name);
    }
}
