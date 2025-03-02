package yuuria.stackupper.stackupper.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import yuuria.stackupper.stackupper.Constants;

@Mixin(value = ItemEntity.class, remap = false)
public class ItemEntityMixin {
    @ModifyConstant(
            method = "merge(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",
            constant = @Constant(intValue = 64)
    )
    private static int fixMergeAmount(int value)
    {
        return Constants.globalStackLimitSupplier.get();
    }
}
