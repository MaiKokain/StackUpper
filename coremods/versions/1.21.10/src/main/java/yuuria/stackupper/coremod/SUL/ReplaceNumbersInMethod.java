package yuuria.stackupper.coremod.SUL;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Set;

public class ReplaceNumbersInMethod extends SimpleMethodProcessor {
    private final Set<Target> targets;
    private final Set<Integer> toReplace;
    private final Integer replaceWith;
//    private final String className;

    public ReplaceNumbersInMethod(Set<Integer> toReplace, Integer replaceWith, List<Target> targetList)
    {
        this.targets = Set.copyOf(targetList);
        this.toReplace = toReplace;
        this.replaceWith = replaceWith;
    }

    @Override
    public void transform(MethodNode input, SimpleTransformationContext context) {
        for (AbstractInsnNode insnNode : input.instructions) {
            if (insnNode.getOpcode() == Opcodes.BIPUSH && toReplace.contains(((IntInsnNode) insnNode).operand)) {
                input.instructions.insertBefore(
                        insnNode,
                        new IntInsnNode(insnNode.getOpcode(), replaceWith)
                );
                input.instructions.remove(insnNode);
            }
        }
    }

    @Override
    public Set<Target> targets() {
        return targets;
    }

    @Override
    public ProcessorName name() {
        return new ProcessorName("stackupper.coremods", "replace_numbers_in_method");
    }
}
