package yuuria.stackupper.coremod.transformer;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.objectweb.asm.tree.ClassNode;
import yuuria.stackupper.coremod.ASMUtils;

import java.util.Set;

public class ReplaceClass implements ITransformer<ClassNode> {

    @Override
    public ClassNode transform(ClassNode methodNode, ITransformerVotingContext iTransformerVotingContext) {
        return ASMUtils.replaceClass(methodNode);
    }

    @Override
    public TransformerVoteResult castVote(ITransformerVotingContext iTransformerVotingContext) {
        return TransformerVoteResult.YES;
    }

    @Override
    public Set<Target<ClassNode>> targets() {
        return Set.of(
                Target.targetClass("net.minecraft.core.component.DataComponents")
        );
    }

    @Override
    public TargetType<ClassNode> getTargetType() {
        return TargetType.CLASS;
    }
}
