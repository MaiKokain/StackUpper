package yuuria.stackupper.coremod.transformer;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.objectweb.asm.tree.MethodNode;
import yuuria.stackupper.coremod.ASMUtils;

import java.util.Set;

public class Replace64WithInteger implements ITransformer<MethodNode> {

    @Override
    public MethodNode transform(MethodNode methodNode, ITransformerVotingContext iTransformerVotingContext) {
        return ASMUtils.replace64(methodNode);
    }

    @Override
    public TransformerVoteResult castVote(ITransformerVotingContext iTransformerVotingContext) {
        return TransformerVoteResult.YES;
    }

    @Override
    public Set<Target<MethodNode>> targets() {
        return Set.of(
                Target.targetMethod("ironfurnaces.tileentity.furnaces.BlockIronFurnaceTileBase", "canSmelt", "(Lnet/minecraft/world/item/crafting/RecipeHolder;)Z"),
                Target.targetMethod("ironfurnaces.tileentity.furnaces.BlockIronFurnaceTileBase", "smelt", "(Lnet/minecraft/world/item/crafting/RecipeHolder;)V"),
                Target.targetMethod("ironfurnaces.tileentity.furnaces.BlockIronFurnaceTileBase", "smeltItemMult", "(Lnet/minecraft/world/item/crafting/RecipeHolder;I)V"),
                Target.targetMethod("ironfurnaces.tileentity.furnaces.BlockIronFurnaceTileBase", "smeltFactoryItemMult","(Lnet/minecraft/world/item/crafting/RecipeHolder;II)V")
        );
    }

    @Override
    public TargetType<MethodNode> getTargetType() {
        return TargetType.METHOD;
    }
}
