package yuuria.stackupper.core.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import yuuria.stackupper.core.IStackUpPatch;
import yuuria.stackupper.core.StackUpClassTracker;
import yuuria.stackupper.core.StackUpperTransformer;
import yuuria.stackupper.core.utils.PatchConstant64;

import java.util.Arrays;
import java.util.List;

public class IItemHandlerCorePatch implements IStackUpPatch {
    private static final String INTERFACE_REQUIRED = "net/minecraftforge/items/IItemHandler";
    private static final List<String> LIMIT_METHODS = Arrays.asList("getSlotLimit");
    private static final List<String> LIMIT_DESC = Arrays.asList("(I)I", "()I");

    @Override
    public boolean canExecute(ClassNode classNode) {
        if ((classNode.access & Opcodes.ACC_INTERFACE) != 0) return false;

        for (String iface : classNode.interfaces)
        {
            if (iface.equals(INTERFACE_REQUIRED)) return true;
        }

        String name = classNode.name.replace('/', '.');
        return StackUpClassTracker.isImplements(name, "net.minecraftforge.items.IItemHandler");
    }

    @Override
    public boolean canExecute(ClassNode classNode, String tName) {
        return this.canExecute(classNode);
    }

    @Override
    public void execute(ClassNode classNode) {
        if ((classNode.access & Opcodes.ACC_INTERFACE) != 0) return;

        for (MethodNode method : classNode.methods) {
            if (!isLimitMethod(method)) continue;
            if ((method.access & (Opcodes.ACC_ABSTRACT | Opcodes.ACC_NATIVE)) != 0) return;

            if (PatchConstant64.patch(method)) {
                StackUpperTransformer.LOGGER.info("Patched {}.{} with IItemHandlerCorePatch", classNode.name, method.name);
            }
        }
    }

    private boolean isLimitMethod(MethodNode methodNode)
    {
        if (!LIMIT_DESC.contains(methodNode.desc)) return false;
        return LIMIT_METHODS.contains(methodNode.name);
    }
}
