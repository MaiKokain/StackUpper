package yuuria.stackupper.core.patches;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import yuuria.stackupper.core.IStackUpPatch;
import yuuria.stackupper.core.StackUpperTransformer;
import yuuria.stackupper.core.utils.PatchConstant64;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constant64CorePatch implements IStackUpPatch {
    private static final Map<String, String[]> methodAndClass;
    static {
        methodAndClass = new HashMap<>();
        methodAndClass.put("org.cyclops.colossalchests.tileentity.TileColossalChest", new String[]{"constructInventory"});
        methodAndClass.put("net.minecraft.inventory.Slot", new String[]{"getItemStackLimit", "func_178170_b", "getSlotStackLimit", "func_75219_a"});
    }

    @Override
    public boolean canExecute(ClassNode classNode) {
        return methodAndClass.containsKey(classNode.name.replace("/", "."));
    }

    @Override
    public boolean canExecute(ClassNode classNode, String tName) {
        return methodAndClass.containsKey(tName);
    }

    @Override
    public void execute(ClassNode classNode) {
        String cleanedName = classNode.name.replace("/", ".");
        for (MethodNode methodNode : classNode.methods) {
            if (Arrays.stream(methodAndClass.get(cleanedName)).anyMatch(s -> s.equals(methodNode.name))) {
                if (PatchConstant64.patch(methodNode)) {
                    StackUpperTransformer.LOGGER.info("Patched {}.{} with Constant64CorePatch", cleanedName, methodNode.name);
                }
            }
        }
    }
}
