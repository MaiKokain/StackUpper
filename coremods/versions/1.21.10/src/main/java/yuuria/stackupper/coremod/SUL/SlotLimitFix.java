package yuuria.stackupper.coremod.SUL;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.tree.MethodNode;
import yuuria.stackupper.coremod.ASMUtils;

import java.util.List;
import java.util.Set;

public class SlotLimitFix extends SimpleMethodProcessor {
    private final Set<Target> targets;

    public SlotLimitFix(List<Target> targetList)
    {
        this.targets = Set.copyOf(targetList);
    }

    @Override
    public void transform(MethodNode input, SimpleTransformationContext context) {
        ASMUtils.fixSlotLimit(input);
    }

    @Override
    public Set<Target> targets() {
        return targets;
    }

    @Override
    public ProcessorName name() {
        return new ProcessorName("stackupper.coremods", "slot_limit_fix");
    }
}
