package yuuria.stackupper.coremod.transformer;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.objectweb.asm.tree.MethodNode;
import yuuria.stackupper.coremod.ASMUtils;

import java.util.Set;

public class SlotLimit implements ITransformer<MethodNode> {

    @Override
    public MethodNode transform(MethodNode methodNode, ITransformerVotingContext iTransformerVotingContext) {
        return ASMUtils.fixSlotLimit(methodNode);
    }

    @Override
    public TransformerVoteResult castVote(ITransformerVotingContext iTransformerVotingContext) {
        return TransformerVoteResult.YES;
    }

    @Override
    public Set<Target<MethodNode>> targets() {
        return Set.of(
                Target.targetMethod("net.neoforged.neoforge.items.IItemHandler", "getSlotLimit", "(I)I"),
                Target.targetMethod("net.neoforged.neoforge.transfer.ItemAccessResourceHandler", "getCapacity", "()I"),
                Target.targetMethod("mekanism.common.inventory.slot.BasicInventorySlot", "getLimit", "(Lnet/minecraft/world/item/ItemStack;)I")
        );
    }

    @Override
    public TargetType<MethodNode> getTargetType() {
        return TargetType.METHOD;
    }
}
