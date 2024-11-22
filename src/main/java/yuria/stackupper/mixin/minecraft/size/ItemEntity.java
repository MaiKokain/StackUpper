package yuria.stackupper.mixin.minecraft.size;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = net.minecraft.world.entity.item.ItemEntity.class, remap = false)
public class ItemEntity {
    @ModifyConstant(
            method = "merge(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",
            constant = @Constant(intValue = 64)
    )
    private static int StackLimit(int val)
    {

        return Integer.MAX_VALUE;
    }
}
