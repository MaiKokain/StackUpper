package yuuria.stackupper.core.patches;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import yuuria.stackupper.core.IStackUpPatch;
import yuuria.stackupper.core.StackUpperTransformer;
import yuuria.stackupper.core.utils.PatchIReturn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IReturnCorePatch implements IStackUpPatch {
    private static final Map<String, String[]> methodAndClass;
    static {
        methodAndClass = new HashMap<>();
        methodAndClass.put("slimeknights.mantle.tileentity.TileInventory", new String[]{"getInventoryStackLimit", "func_70297_j_"});
        methodAndClass.put("de.ellpeck.actuallyadditions.mod.tile.TileEntityInventoryBase", new String[]{"getMaxStackSize"});
    }

    @Override
    public boolean canExecute(ClassNode classNode) {
        return methodAndClass.containsKey(classNode.name.replace('/', '.'));
    }

    @Override
    public boolean canExecute(ClassNode classNode, String tName) {
        return methodAndClass.containsKey(tName);
    }

    @Override
    public void execute(ClassNode classNode) {
        String cName = classNode.name.replace("/", ".");
        for (MethodNode methodNode : classNode.methods) {
            if (Arrays.stream(methodAndClass.get(cName)).anyMatch(s -> s.equals(methodNode.name))) {
                if (PatchIReturn.patch(methodNode)) {
                    StackUpperTransformer.LOGGER.info("Patched {}.{} with ModsConstant64CorePatch", cName, methodNode.name);
                }
            }
        }
    }
}
